package io.shubansoft.ims.model.rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Result;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import io.shubansoft.ims.model.Orders;
import io.shubansoft.ims.model.ProductOrder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.deliveredtechnologies.rulebook.RuleState.NEXT;

@Rule(order = 2)
public class ProductStockLevelRule {

    @Given
    private List<ProductOrder> productOrders;

    @Result
    private Orders orders;

    @When
    public boolean when() {
        final ProductOrder productOrder = productOrders.get(0);
        return productOrder.getProductQuantity() < productOrder.getProductMinQuantity()
                && (productOrder.getProductMaxQuantity() == null || productOrder.getProductQuantity() < productOrder.getProductMaxQuantity());
    }

    @Then
    public RuleState then() {
        final ProductOrder productOrder = productOrders.get(0);
        final Integer requiredQuantity = productOrder.getProductMinQuantity() - productOrder.getProductQuantity();
        if(this.orders != null && this.orders.getProductId().equals(productOrder.getProductId())){
            final Integer newQuantity = this.orders.getProductQuantity() + requiredQuantity;
            this.orders = this.orders.toBuilder().productQuantity(newQuantity).build();
        }
        else {
            this.orders = Orders.builder()
                    .productId(productOrder.getProductId())
                    .orderDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                    .productQuantity(requiredQuantity)
                    .build();
        }
        return NEXT;
    }
}
