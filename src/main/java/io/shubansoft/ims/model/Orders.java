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
import javax.persistence.Transient;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Orders {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ORDERID")
    private Integer orderId;

    @Column(name = "ORDERDATE")
    private Date orderDate;

    @Column(name = "PRODUCTID")
    private Integer productId;

    @Column(name = "PRODUCTQUANTITY")
    private Integer productQuantity;

    @Transient
    private String productName;
}
