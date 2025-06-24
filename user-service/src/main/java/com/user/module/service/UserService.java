package com.user.module.service;

import com.user.module.model.UserEntity;
import com.user.module.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepo;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserService(UserRepository userRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepo = userRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    public UserEntity save(UserEntity user) {
        if(Objects.isNull(user)) {
           return null;
        }
        final UserEntity[] saved = new UserEntity[1];
        if(Objects.nonNull(user.getId())) {
            userRepo.findById(user.getId()).ifPresentOrElse(existingData -> {
                System.out.println("user found: " + existingData.getName());
                existingData.setEmail(user.getEmail());
                existingData.setName(user.getName());
                saved[0] = userRepo.save(existingData);
            }, () -> {
                saved[0] = userRepo.save(user);
            });
        } else {
            saved[0] = userRepo.save(user);
        }

        kafkaTemplate.send("user-topic", "User created: " + saved[0].getName()).thenAccept(result -> System.out.println("✅ Kafka sent: " + result.getRecordMetadata()))
                .exceptionally(ex -> {
                    System.out.println("❌ Kafka send failed: " + ex.getMessage());
                    return null;
                });
        return saved[0];
    }

    public List<UserEntity> findAll() {
        return userRepo.findAll();
    }
}
