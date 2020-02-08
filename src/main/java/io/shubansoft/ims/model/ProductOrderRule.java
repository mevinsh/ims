package io.shubansoft.ims.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCTORDERRULE")
public class ProductOrderRule {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "PRODUCTORDERRULEID")
    private Integer productOrderRuleId;

    @Column(name = "PRODUCTID")
    private Integer productId;

    @Column(name = "MINPRODUCTQUANTITY")
    private Integer minProductQuantity;

    @Column(name = "MAXPRODUCTQUANTITY")
    private Integer maxProductQuantity;

    @Column(name = "ISBLOCKED")
    private Boolean isBlocked;

    @Column(name = "ONEOFFORDER")
    private Integer oneOffOrder;
}
