package com.lucasmanoel.habitos.controller;

import com.lucasmanoel.habitos.business.HabitosService;
import com.lucasmanoel.habitos.business.dto.CheckinDTORecords;
import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class HabitosController {

    private final HabitosService habitosService;

    @PostMapping
    @Operation(summary = "Cadastrar Habito", description = "Cadastro novo habito")
    @ApiResponse(responseCode = "201", description = "Habito cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<HabitosDTORecords> cadastroHabito(@RequestHeader("Authorization") String token, @Valid @RequestBody HabitosDTORecords dto){
        return ResponseEntity.status(HttpStatus.CREATED).body((habitosService.cadastroHabito(token, dto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Alterar Status do Hábito", description = "Ativa ou desativa um hábito")
    @ApiResponse(responseCode = "200", description = "Habito Alterado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public void alterarStatusHabito(
            @RequestHeader("Authorization") String token,
            @PathVariable String id,
            @RequestParam boolean ativo
    ) {
        habitosService.alterarStatusHabito(token, id, ativo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta Habito", description = "Deleta Habito do sistema")
    @ApiResponse(responseCode = "204", description = "Habito Deletado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public void deletaHabito(@RequestHeader("Authorization") String token,@PathVariable String id){
        habitosService.deletaHabito(token, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Altera Habito", description = "Altera dados do habito")
    @ApiResponse(responseCode = "200", description = "Habito alterado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<HabitosDTORecords> alteraHabito (@RequestHeader("Authorization") String token, @Valid @RequestBody HabitosDTORecords dto,
                                                           @PathVariable String id){
        return ResponseEntity.ok(habitosService.alteraHabito(token, dto, id));
    }

    @PostMapping("/checkin")
    @Operation(summary = "Check-in", description = "Check-in diario")
    @ApiResponse(responseCode = "201", description = "Check-in realizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<Integer> efetuarCheckin(@RequestHeader("Authorization") String token,@RequestParam String id){
        return ResponseEntity.ok(habitosService.efetuarCheckin(token, id));
    }

    @GetMapping
    @Operation(summary = "Historico Check-in", description = "Busca historico de check-ins")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<List<CheckinDTORecords>> historicoCheckin(@RequestHeader("Authorization") String token,@RequestParam String habitoId){
        return ResponseEntity.ok(habitosService.historicoCheckin(token, habitoId));
    }

    @GetMapping("/streak")
    @Operation(summary = "Sequencia", description = "Busca sequencia de check-ins")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<Integer> calcularStreak(@RequestHeader("Authorization") String token,@RequestParam String habitoId){
        return ResponseEntity.ok(habitosService.calcularStreak(token, habitoId));
    }

}
