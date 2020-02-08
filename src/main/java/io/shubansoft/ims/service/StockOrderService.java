package io.shubansoft.ims.service;

import com.deliveredtechnologies.rulebook.Fact;
import com.deliveredtechnologies.rulebook.FactMap;
import com.deliveredtechnologies.rulebook.NameValueReferableMap;
import com.deliveredtechnologies.rulebook.Result;
import com.deliveredtechnologies.rulebook.model.runner.RuleBookRunner;
import io.shubansoft.ims.dao.OrderDao;
import io.shubansoft.ims.dao.ProductDao;
import io.shubansoft.ims.dao.ProductOrderRuleDao;
import io.shubansoft.ims.model.Orders;
import io.shubansoft.ims.model.OrdersList;
import io.shubansoft.ims.model.Product;
import io.shubansoft.ims.model.ProductOrderRule;
import io.shubansoft.ims.model.ProductOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.shubansoft.ims.model.OrdersList.emptyOrdersList;

@Service
@Slf4j
public class StockOrderService {

    private final RuleBookRunner ruleBook;

    private final ProductDao productDao;

    private final ProductOrderRuleDao productOrderRuleDao;

    private final OrderDao orderDao;

    @Inject
    public StockOrderService(
            final ProductDao productDao,
            final ProductOrderRuleDao productOrderRuleDao,
            final OrderDao orderDao) {
        this.orderDao = orderDao;
        this.ruleBook = new RuleBookRunner("io.shubansoft.ims.model.rules");
        this.productDao = productDao;
        this.productOrderRuleDao = productOrderRuleDao;
    }

    public OrdersList processOrders(
            final Optional<Integer> productId,
            final Boolean test) {

        final Iterable<ProductOrderRule> productOrderRules = productOrderRuleDao.findAll();
        if(productOrderRules == null) {
            log.info("No Rules available for order");
            return emptyOrdersList();
        }

        final Iterable<Product> products = productDao.findAll();
        if(products == null)
            return emptyOrdersList();

        final Map<Integer, ProductOrderRule> rulesMap =
                StreamSupport.stream(productOrderRules.spliterator(),false)
                        .collect(Collectors.toMap(ProductOrderRule::getProductId, Function.identity()));

        final Iterable<Product> list = productId.isPresent()
                ?  StreamSupport.stream(products.spliterator(),false)
                .filter(p -> p.getProductId().equals(productId.get()))
                .collect(Collectors.toList())
                : products;

        final List<Orders> orders = new ArrayList<>();
        list.forEach(p->{
            if(rulesMap.containsKey(p.getProductId())){
                final ProductOrderRule por = rulesMap.get(p.getProductId());
                final NameValueReferableMap<ProductOrder> facts = new FactMap<>();
                facts.put(new Fact<>(ProductOrder.builder()
                        .productId(por.getProductId())
                        .productQuantity(p.getProductQuantity())
                        .productMinQuantity(por.getMinProductQuantity())
                        .productMaxQuantity(por.getMaxProductQuantity())
                        .isBlocked(por.getIsBlocked())
                        .oneOffOrder(por.getOneOffOrder())
                        .build()));
                ruleBook.run(facts);
                final Optional<Result> result = ruleBook.getResult();
                if(result.isPresent()){
                    final Orders ord = (Orders)result.get().getValue();
                    if(ord != null)
                        orders.add(ord);
                }
            }
        });

        if(orders != null && !test){
            return OrdersList.builder()
                    .ordersList(orders.stream()
                            .map(o->orderDao.save(o))
                            .collect(Collectors.toList()))
                    .build();
        }

        return OrdersList.builder()
                .ordersList(Collections.unmodifiableList(orders)
                ).build();

    }
}
