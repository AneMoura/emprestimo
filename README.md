# Sistema de Empréstimo de Equipamentos

API REST desenvolvida em Java com Spring Boot para controle de equipamentos, pessoas colaboradoras e empréstimos.

## Como rodar o projeto localmente

1. Certifique-se de ter o **Java 25** instalado.
2. No terminal, dentro da raiz do projeto, execute:
    - No Windows: `mvnw.cmd spring-boot:run`
    - No Linux/Mac: `./mvnw spring-boot:run`
3. A API ficará disponível em `http://localhost:8080`.

## Rotas da API

### Equipamentos
- `GET /equipamentos` — Lista todos os equipamentos (aceita filtro opcional `?tipo=`)
- `GET /equipamentos/{id}` — Busca um equipamento por ID
- `POST /equipamentos` — Cadastra um novo equipamento
- `PUT /equipamentos/{id}` — Atualiza nome, patrimônio e tipo de um equipamento
- `DELETE /equipamentos/{id}` — Remove um equipamento (caso nunca tenha sido emprestado)

### Colaboradores
- `GET /colaboradores` — Lista todas as pessoas colaboradoras
- `GET /colaboradores/{id}` — Busca uma pessoa colaboradora por ID
- `POST /colaboradores` — Cadastra uma nova pessoa colaboradora
- `GET /colaboradores/{id}/emprestimos` — Lista o histórico de empréstimos da pessoa colaboradora

### Empréstimos
- `GET /emprestimos` — Lista todos os empréstimos (aceita filtro opcional `?atrasados=true`)
- `GET /emprestimos/{id}` — Busca um empréstimo por ID
- `POST /emprestimos?equipamentoId=1&colaboradorId=2` — Realiza um novo empréstimo com prazo de 14 dias
- `PUT /emprestimos/{id}/devolucao` — Registra a devolução de um empréstimo

## Bônus implementados

Nenhum bônus implementado. O projeto entrega 100% do escopo obrigatório (entidades, rotas, regras de negócio R1 a R10, testes unitários com JUnit 5 e deploy no Render).
