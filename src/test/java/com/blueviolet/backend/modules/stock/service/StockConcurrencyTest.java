package com.blueviolet.backend.modules.stock.service;

import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockConcurrencyTest {

    @Autowired private StockService stockService;
    @Autowired private StockRepository stockRepository;
    Long savedStockId;

    @BeforeEach
    public void beforeEach() {
        Stock savedStock = stockRepository.save(
                Stock.of(
                        1L,
                        "아디다스 티셔츠 (색상: 블랙, 사이즈: M)",
                        150L
                )
        );
        savedStockId = savedStock.getStockId();
    }

    @DisplayName("특정 상품에 대한 재고를 1개 감소하는 요청을 동시에 150개 전달하면 해당 상품의 재고는 0개여야 한다.")
    @Test
    public void given100RequestsAndStockQuantity_whenDecreaseStockQuantityAtTheSameTime_thenQuantityIsZero() throws InterruptedException {
        int requestCount = 150;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(requestCount);

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseQuantityByStockId(savedStockId, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock foundStock = stockRepository.findById(savedStockId).orElseThrow();
        assertThat(foundStock.getQuantity()).isEqualTo(0L);
    }

    @DisplayName("특정 상품에 대한 재고를 1개 증가하는 요청을 동시에 150개 전달하면 해당 상품의 재고는 300개여야 한다.")
    @Test
    public void given100RequestsAndStockQuantity_whenIncreaseStockQuantityAtTheSameTime_thenQuantityIsZero() throws InterruptedException {
        int requestCount = 150;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(requestCount);

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.increaseQuantityByStockId(savedStockId, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock foundStock = stockRepository.findById(savedStockId).orElseThrow();
        assertThat(foundStock.getQuantity()).isEqualTo(300L);
    }
}
