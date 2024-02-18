package ru.sanctio.productmicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.sanctio.productmicroservice.service.dto.CreateProductDto;
import ru.sanctio.productmicroservice.service.event.ProductCreatedEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductDto createProductDto) {
        //TODO save DB
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent =
                new ProductCreatedEvent(productId,
                        createProductDto.getTitle(),
                        createProductDto.getPrice(),
                        createProductDto.getQuantity());

        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
                kafkaTemplate.send(
                "product-created-events-topic",
                productId,
                productCreatedEvent);

        //синхронная
//        SendResult<String, ProductCreatedEvent> stringProductCreatedEventSendResult = future.get();

        //асинхронная обработка
        future.whenComplete((result, exception) -> {
            if(exception != null) {
                LOGGER.error("Failed to send message: {}", exception.getMessage());
            } else {
                LOGGER.info("Message send successfully: {}", result.getRecordMetadata());
            }
        });

        LOGGER.info("Return: {}", productId);
        return productId;
    }
}
