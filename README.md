# PersonalDev

Aplicação pessoal para organização de objetivos, atividades, desenvolvimento de hábitos de estudo e gerenciamento de conhecimento.

O PersonalDev também funciona como um projeto prático de Engenharia de Software, permitindo aplicar conceitos de desenvolvimento backend, modelagem de domínio, bancos de dados relacionais, testes automatizados, integração contínua e, futuramente, estatística e Machine Learning.

## 🎯 Objetivo

O PersonalDev busca ajudar o usuário a:

- Organizar objetivos e atividades pessoais.
- Definir prioridades e acompanhar prazos.
- Registrar o que pretende fazer e o que realmente realizou.
- Comparar o planejamento com o tempo efetivamente utilizado.
- Gerenciar conhecimentos e materiais de estudo.
- Revisar conhecimentos de forma adaptativa.
- Construir um histórico de utilização que permita melhorar futuras recomendações.

O desenvolvimento segue uma abordagem incremental: primeiro são implementadas regras determinísticas e funcionalidades confiáveis; posteriormente, dados reais poderão ser utilizados para desenvolver mecanismos mais inteligentes de recomendação.

## 🧠 Conceito

O sistema possui dois fluxos principais.

### Gestão de atividades

```text
Objetivos
    ↓
Atividades
    ↓
Planejamento e priorização
    ↓
Execução
    ↓
Registro do tempo e dos resultados
    ↓
Histórico de utilização
    ↓
Recomendações futuras
```

### Gestão de conhecimento

```text
Conhecimento
    ↓
Revisão
    ↓
Avaliação de desempenho
    ↓
Atualização do intervalo de revisão
    ↓
Próxima revisão
    ↓
Histórico de aprendizagem
```

Atividades e conhecimentos são conceitos distintos. Uma atividade representa uma ação que o usuário pretende realizar; um conhecimento representa algo aprendido que se deseja preservar ao longo do tempo.

## 🏗️ Arquitetura

O backend é organizado em três camadas principais:

- **Domain:** entidades, enums e interfaces de repositório.
- **Application:** serviços que coordenam os casos de uso e aplicam regras de negócio.
- **Presentation:** controllers REST, DTOs, validação de entrada e tratamento de exceções.

Estrutura principal:

```text
src/
├── main/
│   ├── java/com/kelwin/personaldev/
│   │   ├── domain/
│   │   │   ├── model/enums/
│   │   │   └── repository/
│   │   ├── application/
│   │   │   └── service/
│   │   └── presentation/
│   │       ├── controller/
│   │       ├── dto/
│   │       └── exception/
│   └── resources/
│       ├── db/migration/
│       ├── application.properties
│       └── application-dev.properties
└── test/
    └── java/com/kelwin/personaldev/
```

A estrutura poderá evoluir conforme novas necessidades surgirem, sem introduzir complexidade arquitetural desnecessária.

## 📦 Modelo de domínio

O núcleo atual da aplicação inclui:

```text
User
├── Goal
│   ├── Goal
│   │   └── Goal...
│   └── Activity
│       └── ActivityExecution...
├── Knowledge
│   └── KnowledgeReview...
└── UserState (planejado)
```

As reticências representam a possibilidade de múltiplas entidades relacionadas. A hierarquia de `Goal` permite objetivos independentes, objetivos-pai e objetivos-filhos em diferentes níveis.

### User

Representa o usuário da aplicação, incluindo informações de identificação e criação.

### Goal

Representa um objetivo que o usuário deseja alcançar.

Principais características:

- Título e descrição.
- Status e prioridade.
- Prazo opcional.
- Tipo de prazo: fixo (`FIXED`) ou flexível (`FLEXIBLE`).
- Relacionamento opcional com um objetivo-pai.
- Relacionamento com objetivos-filhos.
- Associação com atividades.

As regras de negócio impedem ciclos na hierarquia, restringem o relacionamento pai-filho a objetivos do mesmo usuário e protegem contra a exclusão de objetivos que ainda possuem filhos.

O prazo flexível representa uma data que pode ser ajustada pelo usuário; o prazo fixo representa uma data que deve ser tratada como uma restrição mais rígida.

### Activity

Representa uma ação que o usuário pretende realizar, associada ou não a um objetivo.

Entre suas características estão:

- Título e descrição.
- Duração estimada.
- Dificuldade e prioridade.
- Status da atividade.
- Prazo e demais informações de planejamento previstas no modelo.

Uma atividade pode existir independentemente de um objetivo. Quando existe uma associação, o sistema valida se o objetivo pertence ao mesmo usuário.

### ActivityExecution

Registra uma execução real de uma atividade. Uma atividade pode possuir várias execuções, permitindo comparar estimativas com o tempo efetivamente gasto.

Exemplo:

```text
Activity: Estudar Spring Security
Duração estimada: 180 minutos

├── Execução 1: 60 minutos
├── Execução 2: 70 minutos
└── Execução 3: 50 minutos
```

O histórico de execuções fornece dados para acompanhar o progresso e, futuramente, melhorar as estimativas de duração.

### Knowledge

Representa um conhecimento que o usuário aprendeu e deseja manter.

O modelo permite gerenciar conhecimentos independentemente das atividades, favorecendo revisões recorrentes e a futura organização de relações entre conhecimentos e materiais de estudo.

### KnowledgeReview

Registra uma revisão de conhecimento e seu desempenho.

O sistema possui uma estrutura inicial de revisão adaptativa, incluindo o agendamento da próxima revisão e o intervalo entre revisões. A evolução desse mecanismo poderá considerar o histórico de desempenho para ajustar a frequência de revisão.

### UserState

Funcionalidade planejada para representar o contexto atual do usuário, como tempo disponível, energia, motivação e foco.

Essas informações poderão apoiar um futuro sistema de recomendação de atividades.

## 🛠️ Tecnologias

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- Lombok

### Banco de dados e persistência

- PostgreSQL
- Docker
- Docker Compose
- Flyway
- Hibernate

### Testes e qualidade

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- JaCoCo e ferramentas de análise de qualidade, conforme configuradas no projeto

### Documentação e ferramentas

- OpenAPI
- Swagger UI
- Maven
- Git e GitHub
- GitHub Actions

### Tecnologias futuras

- React para a interface web.
- Estatística aplicada ao histórico de utilização.
- Machine Learning, quando houver dados suficientes e uma necessidade concreta.

## 🗄️ Banco de dados e migrações

O PostgreSQL é executado por meio do Docker Compose. A aplicação utiliza variáveis de ambiente para configurar a conexão com o banco.

Exemplo de `.env`:

```env
DB_NAME=personaldev
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_PORT=5434
```

O banco utiliza a porta `5434` no host para evitar conflitos com outros bancos PostgreSQL locais. Dentro do container, a porta padrão é `5432`.

O arquivo `.env` não deve ser versionado. O repositório disponibiliza um `.env.example` para orientar a configuração de novos ambientes.

### Flyway

As migrações estão localizadas em:

```text
src/main/resources/db/migration/
```

As migrations versionadas controlam a evolução do esquema do banco. Novas alterações devem ser adicionadas em novas migrations, sem modificar migrations que já foram aplicadas em ambientes existentes.

O Hibernate utiliza:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Assim, o Hibernate verifica a compatibilidade entre as entidades e o esquema existente, enquanto o Flyway é responsável pela evolução do banco de dados.

## 📖 API REST

A aplicação disponibiliza recursos REST para os principais componentes do domínio:

- Usuários (`User`).
- Objetivos (`Goal`).
- Atividades (`Activity`).
- Execuções de atividades (`ActivityExecution`).
- Conhecimentos (`Knowledge`).
- Revisões de conhecimento (`KnowledgeReview`).

A documentação interativa é disponibilizada pelo Swagger UI.

Com a aplicação em execução:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

Os endpoints, parâmetros e formatos de requisição devem ser consultados na documentação gerada pela própria aplicação.

## ⚠️ Validação e tratamento de erros

O projeto utiliza Bean Validation, com anotações como `@NotBlank`, `@NotNull`, `@Email` e `@Positive`, conforme as regras de cada DTO.

O tratamento global de exceções contempla categorias como:

- Recursos inexistentes: `404 Not Found`.
- Recursos duplicados: `409 Conflict`.
- Violações de regras de negócio: `400 Bad Request`.
- Erros de validação: `400 Bad Request`.
- Erros inesperados: `500 Internal Server Error`.

As regras de negócio são aplicadas na camada de serviços, evitando depender exclusivamente da validação das requisições.

## 🧪 Testes automatizados

Os testes são desenvolvidos junto com as funcionalidades.

A estrutura de testes contempla as camadas de serviço e apresentação, incluindo testes para os principais recursos do domínio.

### Testes de serviços

Verificam comportamentos como:

- Criação, consulta, atualização e exclusão.
- Tratamento de recursos inexistentes e duplicados.
- Validação de relacionamentos entre entidades.
- Aplicação de regras de negócio.
- Restrições da hierarquia de objetivos.

### Testes de controllers

Utilizam MockMvc para verificar:

- Requisições HTTP.
- Status HTTP.
- Estrutura do JSON de resposta.
- Validação das requisições.
- Tratamento de exceções.

Para executar os testes:

```bash
./mvnw test
```

Para executar o ciclo de verificação do projeto:

```bash
./mvnw verify
```

## 🔄 Integração contínua

O projeto utiliza GitHub Actions para executar verificações automatizadas em eventos configurados no repositório, incluindo pushes e pull requests direcionados à branch `master`.

O pipeline contempla:

1. Checkout do repositório.
2. Configuração do JDK 21.
3. Cache de dependências do Maven.
4. Inicialização do PostgreSQL via Docker Compose.
5. Ativação do profile de teste.
6. Execução de `./mvnw verify`.
7. Encerramento dos containers ao final da execução.

O objetivo é detectar regressões e validar a integração entre código, banco de dados e testes antes de considerar uma alteração concluída.

## 🚀 Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/kelwin-feitosa/personaldev.git
cd personaldev
```

### 2. Configurar as variáveis de ambiente

```bash
cp .env.example .env
```

Preencha o arquivo `.env` com os valores adequados ao ambiente local.

### 3. Iniciar o PostgreSQL

```bash
docker compose up -d
```

### 4. Executar a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

Durante a inicialização, o Flyway executa as migrações pendentes e o Hibernate valida o esquema do banco.

## 🗺️ Roadmap

### Etapa 1 — Estrutura e persistência

- [x] Configuração inicial do projeto.
- [x] PostgreSQL e Docker Compose.
- [x] Variáveis de ambiente e profiles.
- [x] Flyway e migrações versionadas.
- [x] Entidades e repositórios principais.
- [x] Serviços e DTOs.
- [x] Controllers REST.
- [x] Validações e tratamento global de exceções.
- [x] Testes de serviços e controllers.
- [x] Documentação OpenAPI/Swagger.
- [x] Integração contínua.
- [x] Hierarquia de objetivos.
- [x] Tipos de prazo fixo e flexível.

### Etapa 2 — Acompanhamento e aprendizagem

- [x] Registro de execuções de atividades.
- [x] Histórico de execuções.
- [x] Gerenciamento de conhecimentos.
- [x] Registro de revisões.
- [x] Estrutura inicial de revisão adaptativa.
- [ ] Evoluir os critérios de adaptação dos intervalos.
- [ ] Ampliar as relações entre conhecimentos e materiais de estudo.
- [ ] Implementar o modelo de contexto do usuário (`UserState`).

### Etapa 3 — Recomendações

Inicialmente, as recomendações deverão utilizar regras determinísticas, considerando fatores como:

- Prioridade e prazo.
- Duração estimada.
- Tempo disponível.
- Contexto atual do usuário.
- Histórico de execuções.
- Necessidade de revisão de conhecimentos.

A evolução pretendida é:

```text
Regras determinísticas
        ↓
Coleta de dados reais
        ↓
Análise estatística
        ↓
Modelos adaptativos
        ↓
Machine Learning, se justificado
```

O objetivo é evitar a introdução de modelos complexos antes de existir uma base de dados adequada e uma necessidade demonstrável.

### Etapa 4 — Interface web

- [ ] Desenvolver a interface com React.
- [ ] Criar dashboard de objetivos e atividades.
- [ ] Disponibilizar registro e consulta de execuções.
- [ ] Criar telas para conhecimentos e revisões.
- [ ] Apresentar histórico e indicadores de progresso.
- [ ] Integrar as recomendações à interface.

## 📚 Objetivos de aprendizado

O PersonalDev também é um ambiente de prática para aprofundar conhecimentos em:

- Modelagem de domínio e regras de negócio.
- Arquitetura de aplicações.
- APIs REST.
- Persistência com JPA e Hibernate.
- Bancos de dados relacionais.
- Migrações de esquema.
- Validação e tratamento de erros.
- Testes automatizados.
- Integração contínua.
- Qualidade e manutenção de código.
- Análise estatística de dados.
- Sistemas de recomendação e Machine Learning.

A complexidade será adicionada gradualmente, priorizando soluções compreensíveis, testáveis e justificadas por necessidades reais.

## 📌 Status do projeto

**Em desenvolvimento — evolução do backend e das funcionalidades de acompanhamento e aprendizagem.**

O projeto já possui uma base de backend com persistência em PostgreSQL, migrações Flyway, entidades de domínio, serviços, endpoints REST, validações, testes automatizados e integração contínua.

Também contempla hierarquia de objetivos, prazos fixos e flexíveis, registro de execuções de atividades e um mecanismo inicial de revisão adaptativa de conhecimentos.

As próximas etapas concentram-se em amadurecer as regras de negócio, melhorar o acompanhamento do progresso, desenvolver o contexto do usuário e construir uma base sólida para futuras recomendações personalizadas.