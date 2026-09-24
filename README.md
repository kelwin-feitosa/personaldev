# PersonalDev

Aplicação pessoal voltada para organização, acompanhamento de atividades, desenvolvimento de hábitos de estudo e gerenciamento de conhecimento.

O projeto também tem como objetivo servir como um ambiente prático para aplicar e aprofundar conceitos de Engenharia de Software, desenvolvimento backend, banco de dados, testes e, futuramente, Machine Learning.

## 🎯 Objetivo

O PersonalDev busca ajudar o usuário a:

- Organizar objetivos e atividades;
- Registrar o que pretende fazer e o que realmente fez;
- Registrar seu estado atual, como energia, motivação e tempo disponível;
- Reduzir a procrastinação através de sugestões de atividades alternativas;
- Registrar conhecimentos aprendidos;
- Revisar conhecimentos de forma adaptativa;
- Utilizar os dados acumulados para futuramente melhorar as sugestões através de estatística e Machine Learning.

A ideia é que o sistema evolua de regras simples para abordagens mais inteligentes conforme dados reais forem acumulados.

---

## 🧠 Conceito

O sistema será baseado principalmente em dois fluxos.

### Gestão de atividades

```text
Objetivos
    ↓
Atividades
    ↓
Estado atual
    ↓
Sugestão de atividade
    ↓
Execução
    ↓
Registro dos resultados
    ↓
Melhores sugestões futuras
```

### Gestão de conhecimento

```text
Conhecimento aprendido
    ↓
Revisões
    ↓
Avaliação de desempenho
    ↓
Estimativa de domínio
    ↓
Próxima revisão
    ↓
Histórico de aprendizagem
```

---

## 🏗️ Arquitetura

A aplicação segue inicialmente uma organização baseada em domínio, aplicação e apresentação.

```text
src/
├── main/
│   ├── java/
│   │   └── com/kelwin/personaldev/
│   │       ├── domain/
│   │       │   ├── model/
│   │       │   └── repository/
│   │       │
│   │       ├── application/
│   │       │   └── service/
│   │       │
│   │       └── presentation/
│   │           ├── controller/
│   │           ├── dto/
│   │           └── exception/
│   │
│   └── resources/
│       ├── db/
│       │   └── migration/
│       ├── application.properties
│       └── application-dev.properties
│
└── test/
```

A estrutura poderá evoluir conforme novas necessidades surgirem.

---

## 📦 Modelo inicial do domínio

O modelo inicial é composto pelas seguintes entidades:

```text
User
 ├── Goal
 │    └── Activity
 │          └── ActivityExecution
 │
 ├── Knowledge
 │      └── KnowledgeReview
 │
 └── UserState
```

### Entidades

#### User

Representa o usuário da aplicação.

Principais informações:

- Nome;
- E-mail;
- Data de criação.

#### Goal

Representa um objetivo que o usuário deseja alcançar.

Possui informações como:

- Título;
- Descrição;
- Status;
- Prioridade;
- Prazo.

#### Activity

Representa uma atividade relacionada ou não a um objetivo.

Possui informações como:

- Título;
- Descrição;
- Duração estimada;
- Dificuldade;
- Prioridade;
- Estado ativo/inativo.

#### ActivityExecution

Representará uma execução real de uma atividade, permitindo comparar o planejado com o realizado.

#### Knowledge

Representa um conhecimento que o usuário aprendeu e explicou com suas próprias palavras.

#### KnowledgeReview

Representará uma revisão de um conhecimento, armazenando informações sobre desempenho, dificuldade percebida e confiança.

#### UserState

Representará o estado do usuário em determinado momento, incluindo informações como:

- Motivação;
- Energia;
- Tempo disponível;
- Estresse;
- Foco.

---

## 🛠️ Tecnologias

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Web MVC
- Bean Validation
- Lombok

### Banco de dados

- PostgreSQL 17
- Docker
- Docker Compose
- Flyway

### Testes

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc

### Documentação

- OpenAPI
- Swagger UI

### Ferramentas

- Maven
- Git
- GitHub
- GitHub Actions

### Futuramente

- React
- Machine Learning
- Estatística aplicada aos dados de uso

---

## 🔄 Integração Contínua

O projeto utiliza GitHub Actions para executar automaticamente o processo de validação a cada push e pull request direcionado às branches principais.

O pipeline atualmente:

1. Obtém o código do repositório;
2. Configura o JDK 21;
3. Utiliza o Maven Wrapper;
4. Executa o processo de build e testes através do `mvnw verify`.

Workflow:

```text
Push / Pull Request
        ↓
GitHub Actions
        ↓
JDK 21
        ↓
Maven
        ↓
Build + Testes
        ↓
Resultado
```

O CI será evoluído conforme o projeto ganhar novas ferramentas de qualidade, testes de integração e outras verificações.

---

## 🗄️ Banco de dados

O PostgreSQL é executado através do Docker Compose.

A aplicação utiliza variáveis de ambiente para configurar a conexão:

```env
DB_NAME=
DB_USERNAME=
DB_PASSWORD=
DB_PORT=
```

O arquivo `.env` não deve ser versionado.

Para facilitar a configuração de novos ambientes, o projeto possui um `.env.example`.

### Porta

O PostgreSQL utiliza a porta `5434` no host para evitar conflito com outros bancos PostgreSQL locais.

```text
Host: 5434
Container: 5432
```

---

## 🔄 Migrações

O banco de dados é versionado utilizando Flyway.

As migrations ficam em:

```text
src/main/resources/db/migration/
```

Migration inicial:

```text
V1__create_initial_schema.sql
```

O Hibernate está configurado com:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Isso significa que o Hibernate não é responsável por criar ou alterar o banco.

A responsabilidade pela evolução do schema fica com o Flyway.

O Hibernate apenas verifica se as entidades estão compatíveis com o banco existente.

---

## 📖 Documentação da API

A API utiliza OpenAPI e Swagger UI para documentação e testes manuais dos endpoints.

Com a aplicação em execução:

```text
http://localhost:8080/swagger-ui.html
```

A especificação OpenAPI está disponível em:

```text
http://localhost:8080/v3/api-docs
```

### Endpoints atuais

#### Users

```text
POST   /users
GET    /users
GET    /users/{id}
```

#### Goals

```text
POST   /goals
GET    /goals
GET    /goals/{id}
```

#### Activities

```text
POST   /activities
GET    /activities
GET    /activities/{id}
```

---

## ⚠️ Validação e tratamento de erros

As requisições utilizam Bean Validation através de anotações como:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Positive`

O projeto possui tratamento global para:

- Recursos não encontrados → `404 Not Found`;
- Recursos duplicados → `409 Conflict`;
- Erros de validação → `400 Bad Request`;
- Erros inesperados → `500 Internal Server Error`.

---

## 🚀 Como executar

### 1. Clonar o projeto

```bash
git clone <URL_DO_REPOSITORIO>

cd personaldev
```

### 2. Configurar o ambiente

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

Preencha as variáveis do `.env`.

Exemplo:

```env
DB_NAME=personaldev
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_PORT=5434
```

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

```bash
mvnw.cmd spring-boot:run
```

Durante a inicialização, o Flyway executará as migrations pendentes e o Hibernate validará o schema.

---

## 🧪 Testes

Os testes são desenvolvidos junto com as funcionalidades da aplicação.

A estrutura atual inclui testes para os Services e Controllers:

```text
src/test/java/com/kelwin/personaldev/
├── application/
│   └── service/
│       ├── UserServiceTest
│       ├── GoalServiceTest
│       └── ActivityServiceTest
│
└── presentation/
    └── controller/
        ├── UserControllerTest
        ├── GoalControllerTest
        └── ActivityControllerTest
```

### Service Tests

Os testes dos Services verificam principalmente:

- Criação de recursos;
- Busca de recursos;
- Recursos inexistentes;
- Recursos duplicados;
- Relacionamentos entre entidades.

### Controller Tests

Os testes dos Controllers utilizam `MockMvc` para verificar:

- Requisições HTTP;
- Status HTTP;
- JSON de resposta;
- Validação das requisições;
- Tratamento de exceções.

Os testes das camadas são isolados para evitar testar a mesma responsabilidade mais de uma vez.

Para executar os testes:

```bash
./mvnw test
```

Para executar o ciclo completo de build:

```bash
./mvnw verify
```

---

## 🗺️ Roadmap

### MVP 1 — Estrutura básica

- [x] Configuração inicial do projeto
- [x] PostgreSQL
- [x] Docker Compose
- [x] Variáveis de ambiente
- [x] Profiles
- [x] Flyway
- [x] Migration inicial
- [x] Entidade `User`
- [x] Entidade `Goal`
- [x] Entidade `Activity`
- [x] Repositories
- [x] Services
- [x] DTOs
- [x] Controllers
- [x] Validações
- [x] Tratamento global de exceções
- [x] Swagger/OpenAPI
- [x] Testes dos Services
- [x] Testes dos Controllers
- [x] GitHub Actions

### MVP 2 — Registro e acompanhamento

- [ ] `ActivityExecution`
- [ ] `Knowledge`
- [ ] `KnowledgeReview`
- [ ] `UserState`
- [ ] Histórico de execução
- [ ] Sistema de revisão de conhecimento

### MVP 3 — Recomendações

Inicialmente, as recomendações serão baseadas em regras determinísticas.

Depois, conforme houver dados suficientes:

```text
Regras
  ↓
Estatística
  ↓
Machine Learning
```

A utilização de Machine Learning será feita apenas quando existir uma necessidade clara e dados suficientes para justificar sua utilização.

### MVP 4 — Frontend

- [ ] Interface web
- [ ] Dashboard
- [ ] Gestão de objetivos
- [ ] Gestão de atividades
- [ ] Registro de execução
- [ ] Gestão de conhecimentos
- [ ] Revisões
- [ ] Visualização de histórico

---

## 📚 Objetivo de aprendizado

Além de ser uma aplicação de uso pessoal, o PersonalDev será utilizado como projeto de estudo para praticar conceitos de Engenharia de Software.

Entre eles:

- Modelagem de domínio;
- Arquitetura de aplicações;
- APIs REST;
- Persistência de dados;
- Banco de dados relacionais;
- Migrações;
- Validação;
- Testes automatizados;
- Integração contínua;
- Qualidade de código;
- Organização de projetos;
- Estatística;
- Machine Learning.

A complexidade do projeto será aumentada gradualmente, evitando adicionar tecnologias sem uma necessidade concreta.

---

## 📌 Status

**Em desenvolvimento — fase inicial de estruturação.**

Atualmente o projeto possui:

- Estrutura inicial de domínio;
- PostgreSQL executando via Docker;
- Configuração por variáveis de ambiente;
- Profile de desenvolvimento;
- Flyway configurado;
- Migration inicial;
- Validação do schema através do Hibernate;
- Repositories;
- Services;
- DTOs;
- Controllers REST;
- Validação das requisições;
- Tratamento global de exceções;
- Documentação com Swagger/OpenAPI;
- Testes automatizados dos Services;
- Testes automatizados dos Controllers;
- Pipeline de CI com GitHub Actions.