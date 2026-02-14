package com.grafica.system.repository;

import com.grafica.system.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByAtivoTrue();

    @Query("SELECT p FROM Produto p ORDER BY p.totalVendas DESC")
    List<Produto> findTopByTotalVendas();
}
