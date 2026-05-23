package com.example;

import com.example.proto.AgeReply;
import com.example.proto.AgeRequest;
import com.example.proto.AgeServiceGrpc;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.time.LocalDate;
import java.time.Period;

@SpringBootTest(classes = {AgeServiceTest.TestConfig.class})
@EnableAutoConfiguration
public class AgeServiceTest {

    @Autowired
    AgeServiceGrpc.AgeServiceBlockingStub ageServiceBlockingStub;

    @Test
    public void testAgeCalculate() {
        LocalDate birthDate = LocalDate.of(1987, 12, 12);
        int expectedAge = Period.between(birthDate, LocalDate.now()).getYears();
        AgeReply age = ageServiceBlockingStub.calculateAge(AgeRequest.newBuilder().setDay(12).setMonth(12).setYear(1987).build());
        Assertions.assertEquals(expectedAge, age.getMessage());
    }

    @Test
    public void testFutureBirthDateReturnsError() {
        LocalDate future = LocalDate.now().plusYears(1);
        AgeRequest request = AgeRequest.newBuilder()
                .setDay(future.getDayOfMonth())
                .setMonth(future.getMonthValue())
                .setYear(future.getYear())
                .build();
        Assertions.assertThrows(StatusRuntimeException.class,
                () -> ageServiceBlockingStub.calculateAge(request));
    }

    @Test
    public void testInvalidDateReturnsError() {
        AgeRequest request = AgeRequest.newBuilder().setDay(31).setMonth(2).setYear(2000).build();
        Assertions.assertThrows(StatusRuntimeException.class,
                () -> ageServiceBlockingStub.calculateAge(request));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        AgeServiceGrpc.AgeServiceBlockingStub ageServiceBlockingStub(GrpcChannelFactory channels) {
            return AgeServiceGrpc.newBlockingStub(channels.createChannel("0.0.0.0:9090"));
        }
    }
}
