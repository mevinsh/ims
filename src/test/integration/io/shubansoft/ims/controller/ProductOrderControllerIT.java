package io.shubansoft.ims.controller;

import io.shubansoft.ims.dao.OrderDao;
import io.shubansoft.ims.dao.ProductOrderRuleDao;
import io.shubansoft.ims.model.OrdersList;
import io.shubansoft.ims.model.Product;
import io.shubansoft.ims.model.ProductOrderRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URL;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductOrderControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductOrderRuleDao productOrderRuleDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    public void test_processOrders_testIsTrue() throws Exception {
        final ResponseEntity<OrdersList> response =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders?test=true").toString(),
                        OrdersList.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrdersList());
        assertEquals(4, response.getBody().getOrdersList().size());
        assertTrue(response.getBody().getOrdersList().stream().allMatch(e->e.getOrderId() == null));
        assertEquals(3, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());
        assertEquals(2, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(2)).findFirst().get().getProductQuantity());
        assertEquals(23, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(4)).findFirst().get().getProductQuantity());
        assertEquals(3, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(5)).findFirst().get().getProductQuantity());
    }

    @Test
    public void test_processOrders_testIsEmpty() throws Exception {
        final ResponseEntity<OrdersList> response =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders").toString(),
                        OrdersList.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrdersList());
        assertEquals(4, response.getBody().getOrdersList().size());
        assertTrue(response.getBody().getOrdersList().stream().allMatch(e->e.getOrderId() == null));
        assertEquals(3, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());
        assertEquals(2, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(2)).findFirst().get().getProductQuantity());
        assertEquals(23, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(4)).findFirst().get().getProductQuantity());
        assertEquals(3, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(5)).findFirst().get().getProductQuantity());
    }

    @Test
    public void test_processOrders_testIsFalse() throws Exception {
        final ResponseEntity<OrdersList> response =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders?test=false").toString(),
                        OrdersList.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrdersList());
        assertEquals(4, response.getBody().getOrdersList().size());
        assertTrue(response.getBody().getOrdersList().stream().allMatch(e->e.getOrderId() != null));
        assertEquals(3, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());
        assertEquals(2, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(2)).findFirst().get().getProductQuantity());
        assertEquals(23, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(4)).findFirst().get().getProductQuantity());
        assertEquals(3, response.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(5)).findFirst().get().getProductQuantity());
    }

    @Test
    public void test_processOrders_testProductQuantityUpdate() throws Exception {

        final ResponseEntity<Product> productResponse =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/product/1").toString(),
                        Product.class);
        final Product origProductResponse = productResponse.getBody(); //product quantity = 1
        assertEquals(1, origProductResponse.getProductQuantity());

        final Iterable<ProductOrderRule> pors = productOrderRuleDao.findAll();
        assertNotNull(pors);
        final ProductOrderRule por = StreamSupport.stream(pors.spliterator(),false).filter(e->e.getProductId().equals(1)).findFirst().get();
        assertEquals(4, por.getMinProductQuantity());

        final ResponseEntity<OrdersList> origOrdersResponse =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders").toString(),
                        OrdersList.class);
        //Check Original Order for Product --> 4 - 1 = 3
        assertEquals(3, origOrdersResponse.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());

        final int qtyIncrease = 2;
        //update quantity
        restTemplate.put(new URL("http://localhost:"+port+"/product/1").toString(),
                origProductResponse.toBuilder()
                        .productQuantity(origProductResponse.getProductQuantity()+qtyIncrease) // 1+ 2 = 3
                        .build());

        final ResponseEntity<Product> response2 =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/product/1").toString(),
                        Product.class);
        assertEquals(3, response2.getBody().getProductQuantity());

        final ResponseEntity<OrdersList> updOrdersResponse =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders").toString(),
                        OrdersList.class);
        //Check Original Order for Product --> 4 - 3 = 1
        assertEquals(1, updOrdersResponse.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());

    }

    @Test
    public void test_processOrders_testProductIsBlockedUpdate() throws Exception {

        final ResponseEntity<Product> productResponse =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/product/1").toString(),
                        Product.class);
        final Product origProductResponse = productResponse.getBody(); //product quantity = 1
        assertEquals(1, origProductResponse.getProductQuantity());

        final Iterable<ProductOrderRule> pors = productOrderRuleDao.findAll();
        assertNotNull(pors);
        final ProductOrderRule por = StreamSupport.stream(pors.spliterator(),false).filter(e->e.getProductId().equals(1)).findFirst().get();
        assertNull( por.getIsBlocked());

        final ResponseEntity<OrdersList> origOrdersResponse =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders").toString(),
                        OrdersList.class);
        //Check Original Order for Product --> 4 - 1 = 3
        assertEquals(3, origOrdersResponse.getBody().getOrdersList().stream().filter(e->e.getProductId().equals(1)).findFirst().get().getProductQuantity());

        //update productOrderRule
        final ProductOrderRule updatedPor = productOrderRuleDao.save(por.toBuilder().isBlocked(true).build());

        final ResponseEntity<OrdersList> updOrdersResponse =
                restTemplate.getForEntity(new URL("http://localhost:"+port+"/processOrders").toString(),
                        OrdersList.class);
        //Product is now blocked
        assertFalse(updOrdersResponse.getBody().getOrdersList().stream().filter(e -> e.getProductId().equals(1)).findFirst().isPresent());

    }

}