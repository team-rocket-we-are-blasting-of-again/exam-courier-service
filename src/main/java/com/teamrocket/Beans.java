package com.teamrocket;

import com.teamrocket.proto.UserGrpc;
import com.teamrocket.proto.UserGrpc.UserBlockingStub;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.CourierService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Value("${grpc-service.host}")
    private String grpcHost;

    @Value("${grpc-service.port}")
    private int grpcPort;

    @Bean
    public RestTemplate getRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public UserBlockingStub userBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        return UserGrpc.newBlockingStub(channel);
    }

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
    }

    @Bean
    CourierService courierService(){
        return new CourierService();
    }


}