package com.alves.parkflow.parkflow_backend.service;

import com.alves.parkflow.parkflow_backend.domain.RegistroEstacionamento;
import com.alves.parkflow.parkflow_backend.domain.TipoVeiculo;
import com.alves.parkflow.parkflow_backend.domain.Vaga;
import com.alves.parkflow.parkflow_backend.dto.EntradaVeiculoDTO;
import com.alves.parkflow.parkflow_backend.exception.EstacionamentoException;
import com.alves.parkflow.parkflow_backend.repository.RegistroEstacionamentoRepository;
import com.alves.parkflow.parkflow_backend.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;


import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(EstacionamentoService.class)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class EstacionamentoServiceTest {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private RegistroEstacionamentoRepository registroRepository;

    private EstacionamentoService service;

    @BeforeEach
    void setUp() {
        service = new EstacionamentoService(vagaRepository, registroRepository);

        // Cria vagas de exemplo
        Vaga vagaCarro = new Vaga();
        vagaCarro.setNumero("A1");
        vagaCarro.setTipo(TipoVeiculo.CARRO);
        vagaCarro.setOcupada(false);
        vagaRepository.save(vagaCarro);

        Vaga vagaMoto = new Vaga();
        vagaMoto.setNumero("M1");
        vagaMoto.setTipo(TipoVeiculo.MOTO);
        vagaMoto.setOcupada(false);
        vagaRepository.save(vagaMoto);
    }

    @Test
    void deveRegistrarEntradaComSucesso_QuandoVagaDisponivel() {
        // Dado
        EntradaVeiculoDTO dto = new EntradaVeiculoDTO();
        dto.setPlaca("ABC1234");
        dto.setTipo(TipoVeiculo.CARRO);

        // Quando
        RegistroEstacionamento registro = service.registrarEntrada(dto);

        // Então
        assertThat(registro).isNotNull();
        assertThat(registro.getPlaca()).isEqualTo("ABC1234");
        assertThat(registro.getTipoVeiculo()).isEqualTo(TipoVeiculo.CARRO);
        assertThat(registro.getVaga().isOcupada()).isTrue();
        assertThat(registro.getEntrada()).isNotNull();
    }

    @Test
    void deveLancarExcecao_QuandoTentarRegistrarEntradaDeVeiculoJaEstacionado() {
        // Dado
        EntradaVeiculoDTO dto = new EntradaVeiculoDTO();
        dto.setPlaca("XYZ9876");
        dto.setTipo(TipoVeiculo.CARRO);

        // Quando
        service.registrarEntrada(dto);

        // Então
        assertThatThrownBy(() -> service.registrarEntrada(dto))
                .isInstanceOf(EstacionamentoException.class)
                .hasMessage("Veículo já está estacionado!");
    }

    @Test
    void deveLancarExcecao_QuandoNaoHouverVagaDisponivel() {
        // Dado: ocupa a única vaga de carro
        EntradaVeiculoDTO dto1 = new EntradaVeiculoDTO();
        dto1.setPlaca("CARRO1");
        dto1.setTipo(TipoVeiculo.CARRO);
        service.registrarEntrada(dto1);

        // Tenta registrar outro carro
        EntradaVeiculoDTO dto2 = new EntradaVeiculoDTO();
        dto2.setPlaca("CARRO2");
        dto2.setTipo(TipoVeiculo.CARRO);

        // Então
        assertThatThrownBy(() -> service.registrarEntrada(dto2))
                .isInstanceOf(EstacionamentoException.class)
                .hasMessage("Nenhuma vaga disponível para CARRO");
    }

    @Test
    void deveRegistrarSaidaComSucesso_ECalcularValorCorreto() {
        // Dado: veículo entra
        EntradaVeiculoDTO entrada = new EntradaVeiculoDTO();
        entrada.setPlaca("SAIDA01");
        entrada.setTipo(TipoVeiculo.CARRO);
        service.registrarEntrada(entrada);

        // Quando: registra saída
        RegistroEstacionamento saida = service.registrarSaida("SAIDA01");

        // Então
        assertThat(saida).isNotNull();
        assertThat(saida.isPago()).isTrue();
        assertThat(saida.getValorPago()).isGreaterThanOrEqualTo(new BigDecimal("5.00"));
        assertThat(saida.getSaida()).isAfter(saida.getEntrada());
        assertThat(saida.getVaga().isOcupada()).isFalse();
    }

    @Test
    void deveLancarExcecao_QuandoRegistrarSaidaDeVeiculoNaoEstacionado() {
        // Então
        assertThatThrownBy(() -> service.registrarSaida("PLACA_INEXISTENTE"))
                .isInstanceOf(EstacionamentoException.class)
                .hasMessageContaining("não está estacionado");
    }

    @Test
    void deveLancarExcecao_QuandoRegistrarSaidaDeVeiculoJaPago() {
        // Dado: entra e sai uma vez
        EntradaVeiculoDTO entrada = new EntradaVeiculoDTO();
        entrada.setPlaca("JA_PAGO");
        entrada.setTipo(TipoVeiculo.CARRO);
        service.registrarEntrada(entrada);
        service.registrarSaida("JA_PAGO");

        // Quando: tenta sair de novo
        // Então
        assertThatThrownBy(() -> service.registrarSaida("JA_PAGO"))
                .isInstanceOf(EstacionamentoException.class)
                .hasMessageContaining("não está estacionado");
    }
}