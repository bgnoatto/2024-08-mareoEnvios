package com.bsoft.mercadoEnvios.repository;

import com.bsoft.mercadoEnvios.model.ShippingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShippingItemRepository extends JpaRepository<ShippingItem, Integer> {

	// se define una consulta JPQL para obtener los IDs de los productos y sus conteos totales, ordenados de mayor a menor.
    @Query("SELECT si.product.id AS productId, SUM(si.productCount) AS totalCount " +
           "FROM ShippingItem si " +
           "GROUP BY si.product.id " +
           "ORDER BY totalCount DESC")
    List<Object[]> findTopSentProducts();
}
