# habitos

Microsserviço de rastreamento de habitos do sistema Rastreador de Habitos. Gerencia criacao de habitos, check-ins diarios, calculo de streaks e historico de registros.

## Stack

- Java 21
- Spring Boot
- MongoDB
- MapStruct
- JUnit 5 + Mockito
- Gradle
- SonarQube

## API

Todas as rotas exigem o header `Authorization` com um JWT valido emitido pelo servico `usuario`.

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | /habitos/listar | Listar todos os habitos do usuario autenticado |
| POST | /habitos | Criar um habito |
| PUT | /habitos/{id} | Atualizar um habito |
| PATCH | /habitos/{id}?ativo= | Ativar ou desativar um habito |
| DELETE | /habitos/{id} | Deletar um habito e seu historico de check-ins |
| POST | /habitos/checkin?id= | Registrar check-in diario |
| GET | /habitos/streak?habitoId= | Consultar streak atual em dias |
| GET | /habitos?habitoId= | Consultar historico dos ultimos 30 dias |

## Regras de negocio

- So e permitido um check-in por dia por habito.
- Check-in em habito inativo é rejeitado.
- A exclusao de um habito remove permanentemente todos os seus check-ins.
- Toda operacao de escrita valida a propriedade do token: usuarios so podem gerenciar seus proprios habitos.

## Executando localmente

Requer MongoDB em `localhost:27017`.

```bash
./gradlew bootRun
```

Servico sobe na porta **8081**.

## Executando com Docker

Este servico faz parte de um ambiente multi-container. Consulte o repositorio principal da organizacao [rastreador-habitos](https://github.com/rastreador-habitos) para o `docker-compose.yml` completo.
