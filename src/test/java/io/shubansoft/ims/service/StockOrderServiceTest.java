package io.shubansoft.ims.service;

import com.google.common.collect.ImmutableMap;
import io.shubansoft.ims.dao.OrderDao;
import io.shubansoft.ims.dao.ProductDao;
import io.shubansoft.ims.dao.ProductOrderRuleDao;
import io.shubansoft.ims.model.OrdersList;
import io.shubansoft.ims.model.Product;
import io.shubansoft.ims.model.ProductOrderRule;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StockOrderServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ProductOrderRuleDao productOrderRuleDao;

    @InjectMocks
    private StockOrderService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void processOrders_minStockLevel() {
        when(productOrderRuleDao.findAll()).thenReturn(Lists.newArrayList(
                ProductOrderRule.builder().productId(1).minProductQuantity(10).build(),
                ProductOrderRule.builder().productId(2).minProductQuantity(5).oneOffOrder(30).build(),
                ProductOrderRule.builder().productId(3).minProductQuantity(3).isBlocked(true).build()));
        when(productDao.findAll()).thenReturn(Lists.newArrayList(Product.builder().productId(1).productQuantity(4).build()));

        final OrdersList ordersList = service.processOrders(Optional.empty(),true);
        assertNotNull(ordersList);
        assertEquals(6,ordersList.getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());

    }

    @Test
    void processOrders_oneOffOrder1() {
        when(productOrderRuleDao.findAll()).thenReturn(Lists.newArrayList(
                ProductOrderRule.builder().productId(1).minProductQuantity(10).build(),
                ProductOrderRule.builder().productId(2).minProductQuantity(5).oneOffOrder(30).build(),
                ProductOrderRule.builder().productId(3).minProductQuantity(3).isBlocked(true).build()));
        when(productDao.findAll()).thenReturn(Lists.newArrayList(Product.builder().productId(2).productQuantity(20).build()));

        final OrdersList ordersList = service.processOrders(Optional.empty(),true);
        assertNotNull(ordersList);
        assertEquals(15,ordersList.getOrdersList().stream().filter(e->e.getProductId().equals(2)).findFirst().get().getProductQuantity());

    }

    @Test
    void processOrders_oneOffOrder2() {
        when(productOrderRuleDao.findAll()).thenReturn(Lists.newArrayList(
                ProductOrderRule.builder().productId(1).minProductQuantity(10).build(),
                ProductOrderRule.builder().productId(2).minProductQuantity(5).oneOffOrder(30).build(),
                ProductOrderRule.builder().productId(3).minProductQuantity(3).isBlocked(true).build()));
        when(productDao.findAll()).thenReturn(Lists.newArrayList(Product.builder().productId(2).productQuantity(15).build()));

        final OrdersList ordersList = service.processOrders(Optional.empty(),true);
        assertNotNull(ordersList);
        assertEquals(20,ordersList.getOrdersList().stream().filter(e->e.getProductId().equals(2)).findFirst().get().getProductQuantity());

    }

    @Test
    void processOrders_oneOffOrder3() {
        when(productOrderRuleDao.findAll()).thenReturn(Lists.newArrayList(
                ProductOrderRule.builder().productId(1).minProductQuantity(10).build(),
                ProductOrderRule.builder().productId(2).minProductQuantity(0).oneOffOrder(2).build(),
                ProductOrderRule.builder().productId(3).minProductQuantity(3).isBlocked(true).build()));
        when(productDao.findAll()).thenReturn(Lists.newArrayList(Product.builder().productId(2).productQuantity(2).build()));

        final OrdersList ordersList = service.processOrders(Optional.empty(),true);
        assertNotNull(ordersList);
        assertNull(ordersList.getOrdersList().stream().filter(e->e.getProductId().equals(2)).findFirst().get().getProductQuantity());

    }

    @Test
    void processOrders_isBlocked() {
        when(productOrderRuleDao.findAll()).thenReturn(Lists.newArrayList(
                ProductOrderRule.builder().productId(3).minProductQuantity(3).isBlocked(true).build()));
        when(productDao.findAll()).thenReturn(Lists.newArrayList(Product.builder().productId(3).productQuantity(2).build()
        ));

        final OrdersList ordersList = service.processOrders(Optional.empty(),true);
        assertNotNull(ordersList);
        assertEquals(false,ordersList.getOrdersList().stream().filter(e->e.getProductId().equals(3)).findFirst().isPresent());

    }
}