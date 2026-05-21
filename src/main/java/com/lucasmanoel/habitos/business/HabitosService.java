package com.lucasmanoel.habitos.business;

import com.lucasmanoel.habitos.business.dto.CheckinDTORecords;
import com.lucasmanoel.habitos.business.dto.habitosDTORecords;
import com.lucasmanoel.habitos.business.mapper.HabitosConverter;
import com.lucasmanoel.habitos.business.mapper.HabitosUpdateConverter;
import com.lucasmanoel.habitos.infrasctruture.entity.CheckinEntity;
import com.lucasmanoel.habitos.infrasctruture.entity.HabitosEntity;
import com.lucasmanoel.habitos.infrasctruture.exceptions.ConflictException;
import com.lucasmanoel.habitos.infrasctruture.exceptions.ResourceNotFoundException;
import com.lucasmanoel.habitos.infrasctruture.exceptions.UnauthorizedException;
import com.lucasmanoel.habitos.infrasctruture.repository.CheckinRepository;
import com.lucasmanoel.habitos.infrasctruture.repository.HabitosRepository;
import com.lucasmanoel.habitos.infrasctruture.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitosService {
    private final JwtUtil jwtUtil;
    private  final HabitosConverter habitosConverter;
    private  final HabitosUpdateConverter habitosUpdateConverter;
    private  final HabitosRepository habitosRepository;
    private  final CheckinRepository checkinRepository;

    public habitosDTORecords cadastroHabito(String token, habitosDTORecords dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        HabitosEntity entity = HabitosEntity.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .email(email)
                .dataCriacao(LocalDateTime.now())
                .ativo(true)
                .build();

        return habitosConverter.paraHabitosDTO(habitosRepository.save(entity));
    }
    public void alterarStatusHabito(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestParam boolean ativo){
        if (ativo){
            ativaHabito(token, id);
        }else {
            desativaHabito(token, id);
        }
    }

    public void desativaHabito(String token,String id){

        HabitosEntity entity = habitosRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        if(!entity.getAtivo()){
            throw new ConflictException("Hábito já desativado");
        }
        entity.setAtivo(false);
        habitosRepository.save(entity);
    }
    public void ativaHabito(String token,String id){
        HabitosEntity entity = habitosRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        if(entity.getAtivo()){
            throw new ConflictException("Hábito já ativado");
        }
        entity.setAtivo(true);
        habitosRepository.save(entity);
    }

    public void deletaHabito(String token, String id){
        HabitosEntity entity = habitosRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        habitosRepository.deleteById(id);

    }

    public habitosDTORecords alteraHabito(String token, habitosDTORecords dto, String id){
        HabitosEntity entity = habitosRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        if (!entity.getAtivo()){
            throw new ConflictException("Hábito inativo");
        }
        habitosUpdateConverter.updateHabitos(dto, entity);
        return habitosConverter.paraHabitosDTO(habitosRepository.save(entity));
    }

    public int efetuarCheckin(String token, String id){
        HabitosEntity entity = habitosRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        if (!entity.getAtivo()){
            throw new ConflictException("Hábito desativado!");
        }
        if (checkinRepository.existsByHabitosIDAndData(id, LocalDate.now())){
            throw new ConflictException("Você já realizou Check-in neste hábito hoje");
        }
        CheckinEntity checkin = CheckinEntity.builder()
                .habitosID(entity.getHabitosID())
                .data(LocalDate.now())
                .build();
        checkinRepository.save(checkin);
        return calcularStreak(token ,entity.getHabitosID());
    }

    public List<CheckinDTORecords> historicoCheckin(String token, String habitoId){
        HabitosEntity entity = habitosRepository.findById(habitoId).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        LocalDate data = LocalDate.now().minusDays(30);
        List<CheckinEntity> checkins = checkinRepository.findByHabitosIDAndDataAfter(habitoId, data);
        if (checkins.stream().map(c -> new CheckinDTORecords(c.getData())).toList().isEmpty()){
            throw new ResourceNotFoundException("Você ainda não fez check-in nesse hábito");
        }
        return checkins.stream().map(c -> new CheckinDTORecords(c.getData())).toList();
   }

    public int calcularStreak(String token, String habitoId){
        HabitosEntity entity = habitosRepository.findById(habitoId).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado")
        );
        if (!jwtUtil.extrairEmailToken(token.substring(7)).equals(entity.getEmail())){
            throw new UnauthorizedException("Usuario não autenticado");
        }
        List<CheckinEntity> buscarLista = checkinRepository.findByHabitosID(habitoId);
        buscarLista.sort(Comparator.comparing(CheckinEntity::getData).reversed());

        LocalDate dataEsperada = LocalDate.now();
        int streak = 0;

        for (CheckinEntity checkin : buscarLista){
            if (checkin.getData().equals(dataEsperada)){
                streak++;
                dataEsperada = dataEsperada.minusDays(1);
            }else if (checkin.getData().isBefore(dataEsperada)){
                break;
            }
        }
        return streak;
   }
}
