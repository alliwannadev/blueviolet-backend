package com.blueviolet.backend.modules.option.helper;

import com.blueviolet.backend.modules.option.domain.ProductOptionCombination;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductOptionCombinationTestHelper {

    private final EntityManager em;

    @Transactional(readOnly = true)
    public ProductOptionCombination getOneByCombinationCode(String combinationCode) {
        return em.createQuery(
                                """
                                select  poc
                                from    ProductOptionCombination poc
                                where   poc.combinationCode = :combinationCode
                                """,
                                ProductOptionCombination.class)
                .setParameter("combinationCode", combinationCode)
                .getSingleResult();

    }
}
