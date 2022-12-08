package com.teamrocket.model.camunda;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskVariables {

    String workerId;
    Variables variables;
}
