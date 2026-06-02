package com.lucasmanoel.habitos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lucasmanoel.habitos.business.HabitosService;
import com.lucasmanoel.habitos.business.dto.CheckinDTORecords;
import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HabitosControllerTest {

    @InjectMocks
    HabitosController habitosController;

    @Mock
    HabitosService habitosService;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    final String TOKEN = "Bearer token_de_teste_123";
    final String HABITO_ID = "abc123";
    HabitosDTORecords habitoDTO;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders
                .standaloneSetup(habitosController)
                .build();
    }

    @Test
    void deveCadastrarHabitoERetornar201() throws Exception {
        HabitosDTORecords dto = new HabitosDTORecords("Beber água", "2 litros por dia");
        when(habitosService.cadastroHabito(TOKEN, dto)).thenReturn(dto);

        mockMvc.perform(post("/habitos")
                        .header("Authorization", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Beber água"));

        verify(habitosService).cadastroHabito(TOKEN, dto);
    }

    @Test
    void deveAlterarStatusHabitoERetornar200() throws Exception {
        mockMvc.perform(patch("/habitos/{id}",HABITO_ID)
                .header("Authorization", TOKEN)
                .param("ativo", "true"))
                .andExpect(status().isOk());

        verify(habitosService).alterarStatusHabito(TOKEN, HABITO_ID, true);
    }

    @Test
    void deveDeletarHabitoERetonar204() throws Exception {
        mockMvc.perform(delete("/habitos/{id}",HABITO_ID)
                .header("Authorization", TOKEN))
                .andExpect(status().isNoContent());

        verify(habitosService).deletaHabito(TOKEN, HABITO_ID);
    }

    @Test
    void deveEfetuarCheckinERetornar200ComStreak() throws Exception {
        when(habitosService.efetuarCheckin(TOKEN, HABITO_ID)).thenReturn(5);

        mockMvc.perform(post("/habitos/checkin")
                .header("Authorization", TOKEN)
                .param("id", HABITO_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(habitosService).efetuarCheckin(TOKEN, HABITO_ID);
    }

    @Test
    void deveBuscarHistoricoCheckinERetornar200() throws Exception {
        List<CheckinDTORecords> historico = List.of(
                new CheckinDTORecords(LocalDate.now()),
                new CheckinDTORecords(LocalDate.now().minusDays(1))
        );
        when(habitosService.historicoCheckin(TOKEN, HABITO_ID)).thenReturn(historico);

        mockMvc.perform(get("/habitos")
                        .header("Authorization", TOKEN)
                        .param("habitoId", HABITO_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(habitosService).historicoCheckin(TOKEN, HABITO_ID);
    }
}
