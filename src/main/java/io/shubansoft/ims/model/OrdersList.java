package io.shubansoft.ims.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersList {

    private List<Orders> ordersList;

    public static OrdersList emptyOrdersList(){
        return OrdersList.builder().ordersList(Collections.emptyList()).build();
    }

}
