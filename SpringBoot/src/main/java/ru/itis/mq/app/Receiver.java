package ru.itis.mq.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Receiver {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageRepository messageRepository;

    @RabbitListener(queues = "demo-queue")
    public void process(byte[] messageAsBytes) {
        String jsonMessage = new String(messageAsBytes);
        try {
            MessageDto messageDto = objectMapper.readValue(jsonMessage, MessageDto.class);
            Message message = Message.builder()
                    .email(messageDto.getEmail())
                    .name(messageDto.getName())
                    .phoneNumber(messageDto.getPhoneNumber())
                    .message(messageDto.getMessage())
                    .build();
            message = messageRepository.save(message);
            System.out.println("Message from db: ");
            System.out.println(message);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
