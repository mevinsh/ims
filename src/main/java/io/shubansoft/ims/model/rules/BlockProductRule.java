package io.shubansoft.ims.model.rules;

import com.deliveredtechnologies.rulebook.RuleState;
import com.deliveredtechnologies.rulebook.annotation.Given;
import com.deliveredtechnologies.rulebook.annotation.Result;
import com.deliveredtechnologies.rulebook.annotation.Rule;
import com.deliveredtechnologies.rulebook.annotation.Then;
import com.deliveredtechnologies.rulebook.annotation.When;
import io.shubansoft.ims.model.Orders;
import io.shubansoft.ims.model.ProductOrder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.deliveredtechnologies.rulebook.RuleState.BREAK;

@Rule(order = 1)
@Slf4j
public class BlockProductRule {

    @Given
    private List<ProductOrder> productOrders;

    @Result
    private Orders orders;

    @When
    public boolean when() {
        final ProductOrder productOrder = productOrders.get(0);
        return productOrder.getIsBlocked() != null && productOrder.getIsBlocked();
    }

    @Then
    public RuleState then() {
        log.info("Product "+ productOrders.get(0).getProductId()+" is Blocked");
        this.orders = null;
        return BREAK;
    }
}
