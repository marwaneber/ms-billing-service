package com.example.billingservice.Repository;

import com.example.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;


@RepositoryRestResource
@CrossOrigin(origins="http://localhost:3000")
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    public Collection<ProductItem> findByBillId(Long id);
}
