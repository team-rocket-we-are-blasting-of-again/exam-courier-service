package com.teamrocket.config;

import com.teamrocket.proto.CustomerServiceGrpc;
import com.teamrocket.proto.CustomerServiceGrpc.CustomerServiceBlockingStub;
import com.teamrocket.proto.UserGrpc;
import com.teamrocket.proto.UserGrpc.UserBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Value("${auth-grpc-service.host}")
    private String AUTH_gRPC_Host;

    @Value("${auth-grpc-service.port}")
    private int AUTH_gRPC_Port;

    @Value("${cust-grpc-service.host}")
    private String CUST_gRPC_Host;

    @Value("${cust-grpc-service.port}")
    private int CUST_gRPC_Port;

    @Bean
    public RestTemplate getRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public UserBlockingStub userBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(AUTH_gRPC_Host, AUTH_gRPC_Port).usePlaintext().build();
        return UserGrpc.newBlockingStub(channel);
    }

    @Bean
    public CustomerServiceBlockingStub customerServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(CUST_gRPC_Host, CUST_gRPC_Port).usePlaintext().build();
        return CustomerServiceGrpc.newBlockingStub(channel);
    }

}
