package org.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    //Generste topic
    @Bean
    public NewTopic billReminderTopic() {

        return TopicBuilder
                .name("bill-reminder-topic")
                .partitions(3)
                .replicas(3)
                .build();
    }
}
