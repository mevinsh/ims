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
import java.util.Optional;

import static com.deliveredtechnologies.rulebook.RuleState.NEXT;

@Rule(order = 3)
public class OneOffOrderRule {

    @Given
    private List<ProductOrder> productOrders;

    @Result
    private Orders orders;

    @When
    public boolean when() {
        final ProductOrder productOrder = productOrders.get(0);
        return productOrder.getOneOffOrder() != null
                && ( productOrder.getOneOffOrder() > 0
                && productOrder.getOneOffOrder() > productOrder.getProductMinQuantity());
    }

    @Then
    public RuleState then() {
        final ProductOrder productOrder = productOrders.get(0);
        if(this.orders != null && this.orders.getProductId().equals(productOrder.getProductId())){
            final Integer newQuantity = this.orders.getProductQuantity() + calcNewQuantity(this.orders.getProductQuantity(),productOrder.getProductMinQuantity(),productOrder.getOneOffOrder());
            this.orders = this.orders.toBuilder().productQuantity(newQuantity).build();
        }
        else {
            this.orders = Orders.builder()
                    .productId(productOrder.getProductId())
                    .orderDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                    .productQuantity(calcNewQuantity(productOrder.getProductQuantity(),productOrder.getProductMinQuantity(),productOrder.getOneOffOrder()))
                    .build();
        }

        return NEXT;
    }

    private Integer calcNewQuantity(
            final int productQuantity,
            final int productMinQuantity,
            final int productOneOffOrder){
        if(productOneOffOrder == 0 )
            return null;
        if(productQuantity >= productOneOffOrder){
            final int pd = productQuantity - productOneOffOrder;
            if(pd >= productMinQuantity)
                return null;

            return productMinQuantity - pd;
        }

        final int requiredStock = productMinQuantity + productOneOffOrder;
        return requiredStock - productQuantity;

    }

}

