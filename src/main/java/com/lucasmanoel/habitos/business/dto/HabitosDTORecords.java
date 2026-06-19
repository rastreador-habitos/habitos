package com.lucasmanoel.habitos.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HabitosDTORecords(
        @NotBlank(message = "O nome do hábito é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,
        @NotBlank(message = "A descrição do hábito é obrigatório.")
        String descricao) {
}
