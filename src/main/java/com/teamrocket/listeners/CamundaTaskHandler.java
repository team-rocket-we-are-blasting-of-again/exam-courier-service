package com.teamrocket.listeners;

import com.google.gson.Gson;
import com.teamrocket.entity.CamundaOrderTask;
import com.teamrocket.model.camunda.DeliveryTask;
import com.teamrocket.repository.CamundaRepo;
import com.teamrocket.service.CourierService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.NoSuchElementException;

@Component
@ExternalTaskSubscription(topicName = "claimOrder")
public class CamundaTaskHandler implements ExternalTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaTaskHandler.class);

    private Gson GSON = new Gson();

    @Autowired
    CourierService courierService;

    @Autowired
    private CamundaRepo camundaRepo;


    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        LOGGER.info("New TASK {}", externalTask.getId());

        DeliveryTask deliveryTask = GSON.fromJson(externalTask.getVariable("delivery_task").toString(), DeliveryTask.class);

        String processId = externalTask.getProcessInstanceId();
        String taskDefinitionKey = externalTask.getActivityId();
        String taskId = externalTask.getId();
        String workerId = externalTask.getWorkerId();
        CamundaOrderTask task =
                new CamundaOrderTask(deliveryTask.getOrderId(), processId, taskDefinitionKey, taskId, workerId);
        try {
            CamundaOrderTask existingTask = camundaRepo.findById(deliveryTask.getOrderId()).get();
            if (existingTask.getProcessId().equals(task.getProcessId())) {
                LOGGER.info("Repeated task from Camunda process: {} - omitting...", task.getProcessId());
            }
        } catch (NoSuchElementException e) {
            camundaRepo.save(task);

            LOGGER.info("New DELIVEY_TASK {}", deliveryTask.toString());
            System.out.println(new Date(
                    deliveryTask.getPickupTime()));
            // TODO handle accepted order

        }

    }


}
