package com.grafica.system.repository;

import com.grafica.system.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    @Query("SELECT c FROM Cliente c ORDER BY c.totalCompras DESC")
    List<Cliente> findTopByTotalCompras();
}