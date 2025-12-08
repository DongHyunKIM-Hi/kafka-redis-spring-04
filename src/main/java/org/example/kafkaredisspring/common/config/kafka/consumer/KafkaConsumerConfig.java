package org.example.kafkaredisspring.common.config.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.kafkaredisspring.common.model.kafka.event.PaymentCompletedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // =====================================================================================
    // 공통 Consumer 설정 생성
    // =====================================================================================
    private Map<String, Object> baseConsumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return props;
    }

    private ConsumerFactory<String, PaymentCompletedEvent> buildConsumerFactory(String groupId) {
        JsonDeserializer<PaymentCompletedEvent> deserializer = new JsonDeserializer<>(PaymentCompletedEvent.class);

        return new DefaultKafkaConsumerFactory<>(
            baseConsumerProps(groupId),
            new StringDeserializer(),
            deserializer
        );
    }

    // =====================================================================================
    // 공통 DLT ErrorHandler
    // =====================================================================================
    @Bean
    public CommonErrorHandler commonErrorHandlerWithDLT(
        KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedKafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(paymentCompletedKafkaTemplate);

        // → 1초 간격으로 2회 재시도 (총 3회)
        FixedBackOff backOff = new FixedBackOff(1000L, 2L);

        return new DefaultErrorHandler(recoverer, backOff);
    }


    // =====================================================================================
    // Product Ranking Consumer
    // =====================================================================================

    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> productRankingConsumerFactory() {
        return buildConsumerFactory("product-ranking-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> productRankingKafkaListenerContainerFactory(
        CommonErrorHandler commonErrorHandlerWithDLT) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent>();
        factory.setConsumerFactory(productRankingConsumerFactory());
        factory.setCommonErrorHandler(commonErrorHandlerWithDLT);
        return factory;
    }


    // =====================================================================================
    // Payment History Consumer
    // =====================================================================================

    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> paymentHistoryConsumerFactory() {
        return buildConsumerFactory("payment-history-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentHistoryKafkaListenerContainerFactory(
        CommonErrorHandler commonErrorHandlerWithDLT) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent>();
        factory.setConsumerFactory(paymentHistoryConsumerFactory());
        factory.setCommonErrorHandler(commonErrorHandlerWithDLT);
        return factory;
    }


    // =====================================================================================
    // Delivery Consumer
    // =====================================================================================

    @Bean
    public ConsumerFactory<String, PaymentCompletedEvent> deliveryConsumerFactory() {
        return buildConsumerFactory("delivery-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> deliveryKafkaListenerContainerFactory(
        CommonErrorHandler commonErrorHandlerWithDLT) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent>();
        factory.setConsumerFactory(deliveryConsumerFactory());
        factory.setCommonErrorHandler(commonErrorHandlerWithDLT);
        return factory;
    }

}

