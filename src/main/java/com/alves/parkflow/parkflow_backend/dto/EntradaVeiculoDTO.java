package com.alves.parkflow.parkflow_backend.dto;

import com.alves.parkflow.parkflow_backend.domain.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EntradaVeiculoDTO {

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    @NotNull(message = "Tipo do veículo é obrigatório")
    private TipoVeiculo tipo;

    // Getters e setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public TipoVeiculo getTipo() { return tipo; }
    public void setTipo(TipoVeiculo tipo) { this.tipo = tipo; }
}
