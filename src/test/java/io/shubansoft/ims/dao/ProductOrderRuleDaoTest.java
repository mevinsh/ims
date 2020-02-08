package io.shubansoft.ims.dao;

import com.google.common.collect.Iterables;
import io.shubansoft.ims.model.ProductOrderRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductOrderRuleDaoTest {

    @Autowired
    private ProductOrderRuleDao dao;

    @Test
    public void test_productOrderRule_insert(){
        final ProductOrderRule por =
                dao.save(ProductOrderRule.builder()
                        .productId(1)
                        .minProductQuantity(5)
                        .build());
        assertNotNull(por);
        assertNotNull(por.getProductOrderRuleId());
        assertNotNull(por.getProductId());
        assertNotNull(por.getMinProductQuantity());
        assertEquals(5,por.getMinProductQuantity());
    }

    @Test
    public void test_productOrderRule_findAll(){
        final ProductOrderRule p1 = dao.save(ProductOrderRule.builder()
                .productId(1)
                .minProductQuantity(5)
                .build());
        final ProductOrderRule p2 = dao.save(ProductOrderRule.builder()
                .productId(2)
                .minProductQuantity(10)
                .maxProductQuantity(20)
                .isBlocked(true)
                .oneOffOrder(15)
                .build());
        assertNotNull(p1);
        assertNotNull(p2);

        final Iterable<ProductOrderRule> pors = dao.findAll();
        assertNotNull(pors);
        assertTrue(Iterables.size(pors) > 0);
        assertEquals(5, StreamSupport.stream(pors.spliterator(),false).filter(p->p.getProductOrderRuleId().equals(p1.getProductOrderRuleId())).findFirst().get().getMinProductQuantity());
        final ProductOrderRule por2 = StreamSupport.stream(pors.spliterator(),false).filter(p->p.getProductOrderRuleId().equals(p2.getProductOrderRuleId())).findFirst().get();
        assertEquals(10, por2.getMinProductQuantity());
        assertEquals(20, por2.getMaxProductQuantity());
        assertEquals(true, por2.getIsBlocked());
        assertEquals(15, por2.getOneOffOrder());
    }

    @Test
    public void test_productOrderRule_findById(){
        final ProductOrderRule p1 = dao.save(ProductOrderRule.builder()
                .productId(1)
                .minProductQuantity(5)
                .build());
        assertNotNull(p1);
        assertNotNull(p1.getProductOrderRuleId());

        final Optional<ProductOrderRule> porById = dao.findById(p1.getProductOrderRuleId());
        assertEquals(true,porById.isPresent());
        assertEquals(p1.getProductOrderRuleId(), porById.get().getProductOrderRuleId());
        assertEquals(p1.getMinProductQuantity(), porById.get().getMinProductQuantity());

    }

    @Test
    public void test_productOrderRule_delete(){
        final ProductOrderRule p1 = dao.save(ProductOrderRule.builder()
                .productId(1)
                .minProductQuantity(5)
                .build());
        assertNotNull(p1);
        assertNotNull(p1.getProductOrderRuleId());

        dao.delete(p1);
        final Iterable<ProductOrderRule> pors = dao.findAll();
        assertNotNull(pors);
        assertTrue(Iterables.size(pors) > 0);
        assertEquals(false, StreamSupport.stream(pors.spliterator(),false)
                .filter(p->p.getProductOrderRuleId().equals(p1.getProductOrderRuleId())).findFirst().isPresent());

    }

    @Test
    public void test_productOrderRule_update(){
        final ProductOrderRule p1 = dao.save(ProductOrderRule.builder()
                .productId(1)
                .minProductQuantity(5)
                .build());
        assertNotNull(p1);
        assertNotNull(p1.getProductOrderRuleId());
        assertEquals(5,p1.getMinProductQuantity());

        dao.save(p1.toBuilder().minProductQuantity(0).build());
        final Optional<ProductOrderRule> updatedPor = dao.findById(p1.getProductOrderRuleId());
        assertNotNull(updatedPor);
        assertEquals(0, updatedPor.get().getMinProductQuantity());

    }

}