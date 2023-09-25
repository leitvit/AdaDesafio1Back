package com.ada.consumer.model.repository;

import com.ada.consumer.model.entity.ConsumerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumerFeedbackRepository extends JpaRepository<ConsumerFeedback, UUID> {}
