package com.lucasmanoel.habitos.business;

import com.lucasmanoel.habitos.business.converter.HabitosConverter;
import com.lucasmanoel.habitos.business.converter.HabitosUpdateConverter;
import com.lucasmanoel.habitos.business.dto.CheckinDTORecords;
import com.lucasmanoel.habitos.business.dto.HabitosDTOResponse;
import com.lucasmanoel.habitos.infrastructure.entity.CheckinEntity;
import com.lucasmanoel.habitos.infrastructure.entity.HabitosEntity;
import com.lucasmanoel.habitos.infrastructure.exceptions.ConflictException;
import com.lucasmanoel.habitos.infrastructure.exceptions.ResourceNotFoundException;
import com.lucasmanoel.habitos.infrastructure.exceptions.UnauthorizedException;
import com.lucasmanoel.habitos.infrastructure.repository.CheckinRepository;
import com.lucasmanoel.habitos.infrastructure.repository.HabitosRepository;
import com.lucasmanoel.habitos.infrastructure.security.TokenConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitosServiceTest {

    @InjectMocks
    HabitosService habitosService;

    @Mock
    HabitosRepository habitosRepository;

    @Mock
    CheckinRepository checkinRepository;

    @Mock
    HabitosConverter habitosConverter;

    @Mock
    HabitosUpdateConverter habitosUpdateConverter;

    @Mock
    TokenConfig jwtUtil;

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9eyJzdWIiOiJAIiwiHVR";
    String habitoId = "abc123";
    String email = "lucas@gmail.com";

    HabitosEntity habitoAtivo = HabitosEntity.builder()
            .habitosID(habitoId)
            .nome("Teste")
            .email(email)
            .ativo(true)
            .build();

    CheckinEntity checkinHoje = CheckinEntity.builder()
            .habitosID(habitoId)
            .data(LocalDate.now())
            .build();

    @BeforeEach
    void setup() {
        lenient().when(jwtUtil.extrairEmailToken(token.substring(7))).thenReturn(email);
    }

    @Test
    void deveEfetuarCheckinComSucesso() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));
        when(checkinRepository.existsByHabitosIDAndData(habitoId, LocalDate.now())).thenReturn(false);
        when(checkinRepository.findByHabitosID(habitoId)).thenReturn(new ArrayList<>(List.of(checkinHoje)));

        int streak = habitosService.efetuarCheckin(token, habitoId);

        assertEquals(1, streak);
        verify(checkinRepository).save(any(CheckinEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoHabitosNaoEncontrado() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> habitosService.efetuarCheckin(token, habitoId));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEDono() {
        habitoAtivo.setEmail("emailerrado@teste");
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));

        assertThrows(UnauthorizedException.class,
                () -> habitosService.efetuarCheckin(token, habitoId));
    }

    @Test
    void deveLancarExcecaoQuandoCheckinJaFoiFeito() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));
        when(checkinRepository.existsByHabitosIDAndData(habitoId, LocalDate.now())).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> habitosService.efetuarCheckin(token, habitoId));
    }

    @Test
    void deveRetonarStreakZeroQuandoListaVazia() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));
        when(checkinRepository.findByHabitosID(habitoId)).thenReturn(new ArrayList<>());

        int streak = habitosService.calcularStreak(token, habitoId);
        assertEquals(0, streak);
    }

    @Test
    void deveDeletarHabitoComSucesso() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));

        habitosService.deletaHabito(token, habitoId);
        verify(habitosRepository).deleteById(habitoId);
        verify(checkinRepository).deleteByHabitosID(habitoId);
    }

    @Test
    void deveAlterarStatusHabitoComSucesso() {
        habitoAtivo.setAtivo(false);
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));


        habitosService.alterarStatusHabito(token, habitoId, true);
        verify(habitosRepository).save(habitoAtivo);
    }

    @Test
    void deveBuscarHistoricoCheckinComSucesso() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));
        when(checkinRepository.findByHabitosIDAndDataAfter(habitoId, LocalDate.now().minusDays(30)))
                .thenReturn(new ArrayList<>(List.of(checkinHoje)));

        List<CheckinDTORecords> resultado = habitosService.historicoCheckin(token, habitoId);
        assertEquals(1, resultado.size());
    }

    @Test
    void deveLancarExcecaoQuandoHistoricoVazio() {
        when(habitosRepository.findById(habitoId)).thenReturn(Optional.of(habitoAtivo));
        when(checkinRepository.findByHabitosIDAndDataAfter(habitoId, LocalDate.now().minusDays(30))).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class,
                () -> habitosService.historicoCheckin(token, habitoId));
    }

    @Test
    void deveBuscarListaDeHabitosComSucesso() {
        HabitosDTOResponse habito = HabitosDTOResponse.builder()
                .status(true)
                .descricao("descricao")
                .habitosID(habitoId)
                .nome("nome")
                .build();

        ArrayList<HabitosDTOResponse> lista = new ArrayList<>(List.of(habito));
        List<HabitosEntity> entities = new ArrayList<>(List.of(habitoAtivo));

        when(habitosRepository.findByEmail(email)).thenReturn(entities);
        when(habitosConverter.paraListaDTOResponse(entities)).thenReturn(lista);

        List<HabitosDTOResponse> resultado = habitosService.buscaHabitosPorEmail(token);
        assertEquals(1, resultado.size());
    }

    @Test
    void deveLancarExcecaoQuandoListaVazia() {
        when(habitosRepository.findByEmail(email)).thenReturn(new ArrayList<>());
        when(habitosConverter.paraListaDTOResponse(any())).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class,
                () -> habitosService.buscaHabitosPorEmail(token));
    }
}
