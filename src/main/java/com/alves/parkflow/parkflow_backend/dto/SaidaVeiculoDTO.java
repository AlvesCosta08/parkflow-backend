package com.alves.parkflow.parkflow_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class SaidaVeiculoDTO {

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    // Getters e setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
