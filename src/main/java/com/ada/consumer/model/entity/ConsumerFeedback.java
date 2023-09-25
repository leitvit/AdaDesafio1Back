package com.ada.consumer.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "consumer_feedback")
public class ConsumerFeedback {

    @Schema(description = "Tipo de feedback")
    public enum FeedbackType {
        ELOGIO,
        CRITICA,
        SUGESTAO
    }
    public enum Status {
        RECEBIDO, PROCESSANDO, FINALIZADO
    }

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(columnDefinition = "text", length = 1000)
    private String message;

    @Column
    private FeedbackType feedbackType;

    @Column
    private Status feedbackStatus = Status.RECEBIDO;

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

