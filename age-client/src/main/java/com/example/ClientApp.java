package com.example;

import com.example.proto.AgeReply;
import com.example.proto.AgeRequest;
import com.example.proto.AgeServiceGrpc;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApp {

    private static final Logger logger = Logger.getLogger(ClientApp.class.getName());

    private final AgeServiceGrpc.AgeServiceBlockingStub blockingStub;

    public ClientApp(Channel channel) {
        blockingStub = AgeServiceGrpc.newBlockingStub(channel);
    }

    public Integer calculateAge() {
        AgeReply reply = blockingStub.calculateAge(AgeRequest.newBuilder().setDay(12).setMonth(12).setYear(1980).build());
        return reply.getMessage();
    }

    public static void main(String[] args) throws InterruptedException {
        String target = "localhost:9090";
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        try {
            ClientApp client = new ClientApp(channel);
            Integer age = client.calculateAge();
            logger.info("Age : " + age);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
