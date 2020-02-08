package io.shubansoft.ims.dao;

import io.shubansoft.ims.model.Orders;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "order", path = "order")
public interface OrderDao extends PagingAndSortingRepository<Orders, Integer> {
}
