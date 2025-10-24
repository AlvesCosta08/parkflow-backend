package com.alves.parkflow.parkflow_backend.repository;

import com.alves.parkflow.parkflow_backend.domain.RegistroEstacionamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistroEstacionamentoRepository extends JpaRepository<RegistroEstacionamento, Long> {
    Optional<RegistroEstacionamento> findByPlacaAndPagoIsFalse(String placa);
}
