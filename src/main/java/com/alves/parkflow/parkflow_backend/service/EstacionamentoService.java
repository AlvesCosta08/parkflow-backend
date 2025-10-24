package com.alves.parkflow.parkflow_backend.service;

import com.alves.parkflow.parkflow_backend.domain.RegistroEstacionamento;
import com.alves.parkflow.parkflow_backend.domain.Vaga;
import com.alves.parkflow.parkflow_backend.dto.EntradaVeiculoDTO;
import com.alves.parkflow.parkflow_backend.exception.EstacionamentoException;
import com.alves.parkflow.parkflow_backend.repository.RegistroEstacionamentoRepository;
import com.alves.parkflow.parkflow_backend.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class EstacionamentoService {

    private final VagaRepository vagaRepository;
    private final RegistroEstacionamentoRepository registroRepository;

    public EstacionamentoService(VagaRepository vagaRepository, RegistroEstacionamentoRepository registroRepository) {
        this.vagaRepository = vagaRepository;
        this.registroRepository = registroRepository;
    }

    @Transactional
    public RegistroEstacionamento registrarEntrada(EntradaVeiculoDTO dto) {
        String placa = dto.getPlaca().toUpperCase().trim();
        if (registroRepository.findByPlacaAndPagoIsFalse(placa).isPresent()) {
            throw new EstacionamentoException("Veículo já está estacionado!");
        }

        Vaga vaga = vagaRepository.findFirstDisponivelByTipo(dto.getTipo())
                .orElseThrow(() -> new EstacionamentoException("Nenhuma vaga disponível para " + dto.getTipo()));

        vaga.setOcupada(true);
        vagaRepository.save(vaga);

        RegistroEstacionamento registro = new RegistroEstacionamento();
        registro.setPlaca(placa);
        registro.setTipoVeiculo(dto.getTipo());
        registro.setEntrada(LocalDateTime.now());
        registro.setVaga(vaga);

        return registroRepository.save(registro);
    }

    @Transactional
    public RegistroEstacionamento registrarSaida(String placa) {
        String placaNormalizada = placa.toUpperCase().trim();

        RegistroEstacionamento registro = registroRepository
                .findByPlacaAndPagoIsFalse(placaNormalizada)
                .orElseThrow(() -> new EstacionamentoException("Veículo com placa " + placaNormalizada + " não está estacionado ou já foi pago."));

        LocalDateTime agora = LocalDateTime.now();
        long horas = ChronoUnit.HOURS.between(registro.getEntrada(), agora);
        if (horas == 0) horas = 1;

        BigDecimal valor = new BigDecimal("5.00").multiply(BigDecimal.valueOf(horas));

        registro.setSaida(agora);
        registro.setValorPago(valor);
        registro.setPago(true);

        Vaga vaga = registro.getVaga();
        vaga.setOcupada(false);
        vagaRepository.save(vaga);

        return registroRepository.save(registro);
    }
}