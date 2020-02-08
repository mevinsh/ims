package io.shubansoft.ims.dao;

import io.shubansoft.ims.model.ProductOrderRule;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "productOrderRule", path = "productOrderRule")
public interface ProductOrderRuleDao extends PagingAndSortingRepository<ProductOrderRule,Integer> {
}
