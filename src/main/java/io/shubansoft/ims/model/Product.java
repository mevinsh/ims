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
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "PRODUCTID")
    private Integer productId;

    @Column(name = "PRODUCTNAME")
    private String productName;

    @Column(name = "PRODUCTDESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCTQUANTITY")
    private Integer productQuantity;

}
