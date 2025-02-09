package com.example;

import com.example.proto.AgeReply;
import com.example.proto.AgeServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        LocalDate birthDate = LocalDate.of(year, month, day);
        logger.info("Request Birth Date: {}", birthDate);
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();
        logger.info("Calculated Age: {}", age);
        AgeReply reply = AgeReply.newBuilder().setMessage(age).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
