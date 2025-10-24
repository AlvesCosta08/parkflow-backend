package com.alves.parkflow.parkflow_backend.repository;

import com.alves.parkflow.parkflow_backend.domain.TipoVeiculo;
import com.alves.parkflow.parkflow_backend.domain.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query("SELECT v FROM Vaga v WHERE v.ocupada = false AND v.tipo = :tipo ORDER BY v.numero")
    Optional<Vaga> findFirstDisponivelByTipo(TipoVeiculo tipo);

    List<Vaga> findByOcupada(boolean ocupada);
}
