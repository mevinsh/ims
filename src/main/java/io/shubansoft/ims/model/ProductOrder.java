package io.shubansoft.ims.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {

    private Integer productId;

    private Integer productQuantity;

    private Integer productMinQuantity;

    private Integer productMaxQuantity;

    private Boolean isBlocked;

    private Integer oneOffOrder;

    @Override
    public String toString() {
        return "ProductStockOrder{" +
                "productId=" + productId +
                ", productQuantity=" + productQuantity +
                ", productMinQuantity=" + productMinQuantity +
                ", productMaxQuantity=" + productMaxQuantity +
                ", isBlocked=" + isBlocked +
                ", oneOffOrder=" + oneOffOrder +
                '}';
    }
}
