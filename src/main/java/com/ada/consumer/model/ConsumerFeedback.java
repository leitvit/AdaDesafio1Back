package com.ada.consumer.model;

import lombok.Data;
import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
public class ConsumerFeedback {

    public enum MessageType {
        ELOGIO, CRITICA, SUGESTAO
    }
    public enum status {
        RECEBIDO, PROCESSANDO, FINALIZADO
    }
    private String id;
    private String message;

    @Getter private MessageType feedbackType;
    private status feedbackStatus = status.RECEBIDO;

    @Override
    public String toString() {
        return "Feedback ID: " + id +
                "\nType: " + feedbackType +
                "\nStatus: " + feedbackStatus +
                "\nMessage: " + message;
    }

    private String generateId(String message) { //the use of a hash as the id eliminates the need to call databases or APIs, reducing latency
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(message.getBytes());
            byte[] digest = md.digest();

            // Convert the digest to a 10-character ID (hexadecimal representation)
            StringBuilder idBuilder = new StringBuilder();
            for (int i = 0; i < Math.min(10, digest.length); i++) {
                idBuilder.append(String.format("%02x", digest[i]));
            }

            return idBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

