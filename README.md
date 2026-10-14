# PersonalDev

O **PersonalDev** é uma aplicação em desenvolvimento para ajudar pessoas a **transformar intenções em ações pequenas e concretas, começar tarefas adiadas e retomá-las após interrupções**.

Em vez de tentar ser um gerenciador completo de produtividade, o primeiro MVP será um **assistente de ação e retomada**: simples, com poucos campos e sem punições por adiamentos.

> **Estado atual — reconstrução do MVP:** o domínio antigo e suas migrações foram removidos da branch `master`. A infraestrutura do projeto foi mantida, mas **as entidades, serviços, endpoints e a nova migração do MVP ainda não foram implementados**.

## 🎯 Objetivo e proposta

A experiência central planejada é:

```text
Algo que quero fazer
         ↓
Escolho a próxima ação concreta
         ↓
Defino uma situação em que vou começar
         ↓
Registro que comecei
         ↓
Concluo a pequena ação ou adio
         ↓
Se necessário, crio outro plano e retomo
```

Exemplo: a tarefa é **estudar Java**; a próxima ação é **resolver o primeiro exercício sobre listas**; a situação de início é **depois do almoço**. Se a ação for adiada, o histórico permanece e a pessoa pode criar um novo plano.

**Concluir uma pequena ação não conclui automaticamente a tarefa maior.** O PersonalDev não é uma ferramenta clínica e não promete eliminar ou tratar a procrastinação. A utilidade das funcionalidades será avaliada com usuários.

## ✅ Escopo do MVP

Funcionalidades **planejadas**:

- Criar, listar, consultar e concluir explicitamente tarefas.
- Definir uma **próxima ação executável** vinculada a uma tarefa.
- Informar um **gatilho de início** em linguagem simples, como “agora” ou “depois do almoço”.
- Registrar opcionalmente um obstáculo previsto.
- Registrar início, conclusão ou adiamento de cada plano.
- Retomar criando um novo plano, preservando o histórico anterior.
- Consultar planos e histórico de uma tarefa numa interface simples e adequada para celular.

**Fora do MVP:** hierarquia de objetivos, gerenciador detalhado de tempo, Pomodoro obrigatório, notificações, streaks, gamificação, dashboard complexo, gestão de conhecimentos, revisão espaçada, IA generativa e Machine Learning. Essas possibilidades só serão reconsideradas se houver valor demonstrável.

## 🧠 Domínio planejado

O domínio começará com dois conceitos:

### `Task`

Uma tarefa ampla que pode precisar de várias ações pequenas. Possui um título e estado `OPEN` ou `DONE`; sua conclusão é explícita.

### `StartPlan`

Um plano para executar uma próxima ação associada à tarefa. Campos inicialmente propostos:

- `nextAction`: ação concreta obrigatória.
- `startCue`: situação em que a pessoa pretende começar.
- `obstacle`: obstáculo previsto, opcional.
- `status`: `PLANNED`, `STARTED`, `COMPLETED` ou `POSTPONED`.
- Horários relevantes para criação e transições.

Transições previstas:

```text
PLANNED ──iniciar──> STARTED ──concluir──> COMPLETED
   │                     │
   └──adiar──> POSTPONED <──adiar──────────┘
```

Não será permitido concluir antes de começar. `COMPLETED` e `POSTPONED` encerram aquele plano; a retomada cria outro `StartPlan`. Essas regras serão implementadas e testadas primeiro em Java puro, antes da persistência.

## 🏗️ Arquitetura

O backend será um **monólito Spring Boot** com separação simples de responsabilidades:

- **Domain:** entidades, enums e interfaces de repositório.
- **Application:** serviços e casos de uso.
- **Presentation:** controllers REST, DTOs, validação e tratamento de erros.

Estrutura-alvo, **ainda não totalmente existente**:

```text
src/main/java/com/kelwin/personaldev/
├── PersonaldevApplication.java
├── domain/
│   ├── model/
│   │   ├── Task.java
│   │   ├── StartPlan.java
│   │   └── enums/
│   │       ├── TaskStatus.java
│   │       └── StartPlanStatus.java
│   └── repository/
│       ├── TaskRepository.java
│       └── StartPlanRepository.java
├── application/service/
│   ├── TaskService.java
│   └── StartPlanService.java
└── presentation/
    ├── controller/
    ├── dto/
    └── exception/
```

Os pacotes e arquivos serão criados conforme cada etapa for implementada, sem introduzir complexidade antecipadamente.

## 🛠️ Tecnologias

| Área | Ferramentas disponíveis no projeto |
| --- | --- |
| Backend | Java 21, Spring Boot 4.1.1, Spring Web MVC |
| Persistência | PostgreSQL, Spring Data JPA, Hibernate, Flyway |
| Ambiente | Docker, Docker Compose, Maven Wrapper |
| Testes | JUnit, Mockito, Spring Boot Test, MockMvc |
| API | Bean Validation, OpenAPI/Swagger UI |
| Integração contínua | GitHub Actions |

> A presença de dependências no `pom.xml` não significa que as funcionalidades correspondentes já foram implementadas.

## 💻 Ambiente local

### Pré-requisitos

- JDK 21
- Docker e Docker Compose
- Git

### Preparar o projeto

```bash
git clone https://github.com/kelwin-feitosa/personaldev.git
cd personaldev
cp .env.example .env
```

Exemplo de valores para um **ambiente local descartável**:

```dotenv
DB_NAME=personaldev
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_PORT=5434
```

O `.env` deve permanecer fora do versionamento. O `docker-compose.yml` mapeia a porta **5434** do host para a **5432** do container do PostgreSQL.

### Iniciar o banco

```bash
docker compose up -d --wait
```

**Atenção:** o domínio anterior foi excluído, e a nova migração `V1` ainda precisa ser criada. O banco pode iniciar, mas isso não disponibiliza as funcionalidades planejadas.

### Iniciar o Spring Boot

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

O Hibernate usa `spring.jpa.hibernate.ddl-auto=validate`. O Flyway será responsável pelo esquema da nova aplicação, começando por `V1__create_tasks_and_start_plans.sql`.

### Testar

```bash
./mvnw test
./mvnw verify
```

Os testes com o profile `test` esperam um PostgreSQL local na porta **5434**; disponibilize a configuração adequada para executar a suíte completa. O CI permanece configurado para `master` e executa `./mvnw verify` com um PostgreSQL iniciado pelo Docker Compose.

### API e Swagger

As dependências de OpenAPI/Swagger permanecem no projeto. **Ainda não existem endpoints do novo domínio**. Quando forem implementados, a documentação poderá ser consultada em `http://localhost:8080/swagger-ui.html` e `http://localhost:8080/v3/api-docs`.

## 🗺️ Roadmap

- [x] Preparar Spring Boot, Maven Wrapper, PostgreSQL, Docker Compose e CI.
- [x] Remover entidades, controllers, services, DTOs, testes acoplados e migrations do domínio antigo.
- [ ] Implementar `TaskStatus`, `StartPlanStatus`, `Task` e `StartPlan`.
- [ ] Criar testes de domínio para validação e transições de estados.
- [ ] Criar migration `V1` e mapear entidades com JPA.
- [ ] Implementar repositories e testes de persistência.
- [ ] Implementar serviços de tarefas, início, conclusão, adiamento e retomada.
- [ ] Criar endpoints REST, DTOs e testes de controllers.
- [ ] Construir uma interface web responsiva e simples.
- [ ] Testar usabilidade com participantes e dados fictícios.
- [ ] Implementar autenticação, autorização e isolamento de dados antes de disponibilizar o sistema para múltiplos usuários reais.
- [ ] Avaliar hospedagem e evolução do produto com base no uso observado.

## 📌 Princípios do projeto

1. **Resolver um problema específico:** tornar mais fácil começar e retomar.
2. **Manter o MVP enxuto:** evitar funcionalidades não validadas.
3. **Preservar histórico:** adiar não apaga tentativas anteriores.
4. **Testar regras de negócio:** não confundir código compilando com comportamento correto.
5. **Priorizar aprendizado:** implementação consciente, com entendimento e revisão.
6. **Evitar promessas sem evidência:** a eficácia do app ainda não foi demonstrada.
7. **Proteger dados:** até existir segurança adequada, utilizar somente ambiente local ou protótipos com dados fictícios.

---

**Status:** infraestrutura mantida na `master`, legado funcional removido e novo MVP ainda em implementação. **Próxima entrega:** domínio Java de `Task` e `StartPlan`, com testes automatizados.