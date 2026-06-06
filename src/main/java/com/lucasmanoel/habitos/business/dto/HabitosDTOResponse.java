package com.lucasmanoel.habitos.business.dto;

import lombok.Builder;

@Builder
public record HabitosDTOResponse (
        String nome,
        String descricao,
        Boolean status,
        String habitosID
){
}
