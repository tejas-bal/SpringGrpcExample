package com.example;

import com.example.proto.AgeReply;
import com.example.proto.AgeRequest;
import com.example.proto.AgeServiceGrpc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;

@SpringBootTest(classes = {AgeServiceTest.TestConfig.class})
@EnableAutoConfiguration
public class AgeServiceTest {

    @Autowired
    AgeServiceGrpc.AgeServiceBlockingStub ageServiceBlockingStub;

    @Test
    public void testAgeCalculate() {
        AgeReply age = ageServiceBlockingStub.calculateAge(AgeRequest.newBuilder().setDay(12).setMonth(12).setYear(1987).build());
        Assertions.assertEquals(age.getMessage(), 37);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        AgeServiceGrpc.AgeServiceBlockingStub ageServiceBlockingStub(GrpcChannelFactory channels) {
            return AgeServiceGrpc.newBlockingStub(channels.createChannel("0.0.0.0:9090"));
        }
    }
}
