package io.shubansoft.ims.dao;

import com.google.common.collect.Iterables;
import io.shubansoft.ims.model.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderDaoTest {

    @Autowired
    private OrderDao dao;

    @Test
    public void test_order_insert(){
        final Orders orders = dao.save(Orders.builder()
                .productId(1)
                .productQuantity(2)
                .orderDate(new Date())
                .build());
        assertNotNull(orders);
        assertNotNull(orders.getOrderId());
        assertNotNull(orders.getProductId());
        assertNotNull(orders.getProductQuantity());
        assertNotNull(orders.getOrderDate());
        assertEquals(1,orders.getProductId());
        assertEquals(2,orders.getProductQuantity());
    }

    @Test
    public void test_order_findAll(){
        final Orders o1 = dao.save(Orders.builder()
                .productId(1)
                .productQuantity(2)
                .orderDate(new Date())
                .build());
        final Orders o2 = dao.save(Orders.builder()
                .productId(2)
                .productQuantity(6)
                .orderDate(new Date())
                .build());
        assertNotNull(o1);
        assertNotNull(o2);

        final Iterable<Orders> orders = dao.findAll();
        assertNotNull(orders);
        assertTrue(Iterables.size(orders) > 0);
        assertEquals(2, StreamSupport.stream(orders.spliterator(),false).filter(p->p.getOrderId().equals(o1.getOrderId())).findFirst().get().getProductQuantity());
        assertEquals(6, StreamSupport.stream(orders.spliterator(),false).filter(p->p.getOrderId().equals(o2.getOrderId())).findFirst().get().getProductQuantity());
    }

    @Test
    public void test_order_findById(){
        final Orders o1 = dao.save(Orders.builder()
                .productId(2)
                .productQuantity(6)
                .orderDate(new Date())
                .build());
        assertNotNull(o1);
        assertNotNull(o1.getOrderId());

        final Optional<Orders> ordersById = dao.findById(o1.getOrderId());
        assertEquals(true,ordersById.isPresent());
        assertEquals(o1.getProductId(), ordersById.get().getProductId());
        assertEquals(o1.getProductQuantity(), ordersById.get().getProductQuantity());

    }

    @Test
    public void test_order_delete(){
        final Orders o1 = dao.save(Orders.builder()
                .productId(2)
                .productQuantity(6)
                .orderDate(new Date())
                .build());
        assertNotNull(o1);
        assertNotNull(o1.getOrderId());

        dao.delete(o1);
        final Iterable<Orders> orders = dao.findAll();
        assertNotNull(orders);
        assertTrue(Iterables.size(orders) == 0);

    }

    @Test
    public void test_product_update(){
        final Orders o1 = dao.save(Orders.builder()
                .productId(2)
                .productQuantity(6)
                .orderDate(new Date())
                .build());
        assertNotNull(o1);
        assertNotNull(o1.getOrderId());

        dao.save(o1.toBuilder().productQuantity(4).build());
        final Optional<Orders> updatedOrder = dao.findById(o1.getOrderId());
        assertNotNull(updatedOrder);
        assertEquals(4, updatedOrder.get().getProductQuantity());

    }

}