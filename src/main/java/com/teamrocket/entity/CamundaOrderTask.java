package com.teamrocket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CamundaOrderTask {
    private static final long serialVersionUID = 1L;

    @Id
    private int systemOrderId;
    private String processId;
    private String taskDefinitionKey;
    private String taskId;
    private String workerId;
}
