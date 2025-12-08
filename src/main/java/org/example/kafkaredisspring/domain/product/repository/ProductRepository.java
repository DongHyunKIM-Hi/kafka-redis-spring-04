package org.example.kafkaredisspring.domain.product.repository;

import org.example.kafkaredisspring.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
