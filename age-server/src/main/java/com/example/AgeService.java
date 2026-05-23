package com.example;

import com.example.proto.AgeReply;
import com.example.proto.AgeServiceGrpc;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

@Service
public class AgeService extends AgeServiceGrpc.AgeServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AgeService.class);

    @Override
    public void calculateAge(com.example.proto.AgeRequest request,
                             io.grpc.stub.StreamObserver<com.example.proto.AgeReply> responseObserver) {
        int day = request.getDay();
        int month = request.getMonth();
        int year = request.getYear();

        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid birth date: " + e.getMessage())
                    .asRuntimeException());
            return;
        }

        LocalDate currentDate = LocalDate.now();
        if (birthDate.isAfter(currentDate)) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Birth date cannot be in the future")
                    .asRuntimeException());
            return;
        }

        logger.info("Request Birth Date: {}", birthDate);
        int age = Period.between(birthDate, currentDate).getYears();
        logger.info("Calculated Age: {}", age);
        AgeReply reply = AgeReply.newBuilder().setMessage(age).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
