package com.hexagonal.mitocode.adapter.out.jpa;

import com.hexagonal.mitocode.adapter.out.jpa.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz de repositorio de Spring Data JPA para la entidad Order
 */
public interface SpringDataOrderRepository extends JpaRepository<OrderJpaEntity, String> {

}
