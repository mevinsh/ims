package io.shubansoft.ims.dao;

import io.shubansoft.ims.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductDao extends PagingAndSortingRepository<Product,Integer> {
}
