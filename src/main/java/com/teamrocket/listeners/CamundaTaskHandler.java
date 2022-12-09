package com.teamrocket.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.teamrocket.entity.CamundaOrderTask;
import com.teamrocket.enums.Topic;
import com.teamrocket.model.OrderCancelled;
import com.teamrocket.model.camunda.DeliveryTask;
import com.teamrocket.repository.CamundaRepo;
import com.teamrocket.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
@ExternalTaskSubscription(topicName = "claimOrder")
public class CamundaTaskHandler implements ExternalTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaTaskHandler.class);

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    private CamundaRepo camundaRepo;

    @Autowired
    private Gson GSON;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("New TASK {}", externalTask.getVariable("delivery_task").toString());

        String processId = externalTask.getProcessInstanceId();
        String taskDefinitionKey = externalTask.getActivityId();
        String taskId = externalTask.getId();
        String workerId = externalTask.getWorkerId();
        String delStr = externalTask.getVariable("delivery_task").toString();

        try {
            DeliveryTask deliveryTask = GSON.fromJson(delStr, DeliveryTask.class);
            CamundaOrderTask task =
                    new CamundaOrderTask(deliveryTask.getOrderId(), processId, taskDefinitionKey, taskId, workerId);
            try {
                CamundaOrderTask existingTask = camundaRepo.findById(deliveryTask.getOrderId()).get();
                if (existingTask.getProcessId().equals(task.getProcessId())) {
                    LOGGER.info("Repeated task from Camunda process: {} - omitting...", task.getProcessId());
                }
            } catch (NoSuchElementException e) {
                camundaRepo.save(task);
                LOGGER.info("New DELIVERY_TASK {}", deliveryTask);
                deliveryTask = deliveryService.publishNewDeliveryTask(deliveryTask);
            }
            LOGGER.info("TASK {} ready to by claimed", deliveryTask);
        } catch (JsonSyntaxException e) {
            String reason = new StringBuilder("Could not deserialize delivery_task ")
                    .append(delStr)
                    .append(" process id: ").append(processId)
                    .append(", task definition key: ").append(taskDefinitionKey)
                    .toString();
            kafkaTemplate.send(Topic.ORDER_CANCELED.toString(), new OrderCancelled(-1, reason));
        }
    }

}
