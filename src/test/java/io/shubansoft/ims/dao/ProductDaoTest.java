package io.shubansoft.ims.dao;

import com.google.common.collect.Iterables;
import io.shubansoft.ims.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductDaoTest {

    @Autowired
    private ProductDao dao;

    @Test
    public void test_product_insert(){
        final Product product = insert("Test","Desc",2);
        assertNotNull(product);
        assertNotNull(product.getProductId());
        assertNotNull(product.getProductName());
        assertNotNull(product.getProductDescription());
        assertEquals("Test",product.getProductName());
        assertEquals("Desc",product.getProductDescription());
    }

    @Test
    public void test_product_findAll(){
        final Product p1 = insert("P1","P1 Desc", 1);
        final Product p2 = insert("P2","P2 Desc", 3);
        assertNotNull(p1);
        assertNotNull(p2);

        final Iterable<Product> products = dao.findAll();
        assertNotNull(products);
        assertTrue(Iterables.size(products) > 0);
        assertEquals(1, StreamSupport.stream(products.spliterator(),false).filter(p->p.getProductName().equals("P1")).findFirst().get().getProductQuantity());
        assertEquals(3, StreamSupport.stream(products.spliterator(),false).filter(p->p.getProductName().equals("P2")).findFirst().get().getProductQuantity());
    }

    @Test
    public void test_product_findById(){
        final Product p1 = insert("P1","P1 Desc", 1);
        assertNotNull(p1);
        assertNotNull(p1.getProductId());

        final Optional<Product> productById = dao.findById(p1.getProductId());
        assertEquals(true,productById.isPresent());
        assertEquals(p1.getProductId(), productById.get().getProductId());
        assertEquals(p1.getProductQuantity(), productById.get().getProductQuantity());

    }

    @Test
    public void test_product_delete(){
        final Product p1 = insert("P1","P1 Desc", 1);
        assertNotNull(p1);
        assertNotNull(p1.getProductId());

        dao.delete(p1);
        final Iterable<Product> products = dao.findAll();
        assertNotNull(products);
        assertTrue(Iterables.size(products) > 0);
        assertEquals(false, StreamSupport.stream(products.spliterator(),false)
                .filter(p->p.getProductId().equals(p1.getProductId())).findFirst().isPresent());

    }

    @Test
    public void test_product_update(){
        final Product p1 = insert("P1","P1 Desc", 1);
        assertNotNull(p1);
        assertNotNull(p1.getProductId());

        dao.save(p1.toBuilder().productQuantity(4).build());
        final Optional<Product> updatedProduct = dao.findById(p1.getProductId());
        assertNotNull(updatedProduct);
        assertEquals(4, updatedProduct.get().getProductQuantity());

    }

    private Product insert(
            final String productName,
            final String productDesc,
            final int productQuantity){
        return dao.save(Product.builder()
                .productName(productName)
                .productDescription(productDesc)
                .productQuantity(productQuantity)
                .build());
    }
}