package com.blueviolet.backend.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RequiredArgsConstructor
@Configuration
public class QuerydslConfig {

    @PersistenceContext(unitName = "primaryPersistenceUnit")
    private final EntityManager primaryEntityManager;

    @PersistenceContext(unitName = "secondaryPersistenceUnit")
    private final EntityManager secondaryEntityManager;

    @Primary
    @Bean
    public JPAQueryFactory primaryQueryFactory() {
        return new JPAQueryFactory(primaryEntityManager);
    }

    @Bean
    public JPAQueryFactory secondaryQueryFactory() {
        return new JPAQueryFactory(secondaryEntityManager);
    }
}
