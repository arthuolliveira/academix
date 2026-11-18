# Academix

Sistema acadêmico web desenvolvido como trabalho da disciplina de **Engenharia de Requisitos**. O projeto implementa, com código simples e comentado, o login e três telas funcionais integradas ao banco de dados. As demais telas existem como protótipos de interface com dados fictícios.

## Tecnologias

- Java 21
- Spring Boot 3.3 (Spring Web, Spring Data JPA)
- Thymeleaf (HTML renderizado no servidor)
- PostgreSQL
- Maven
- HTML5 e um único arquivo CSS (sem JavaScript)

## Perfis de usuário

| Perfil | Descrição |
|---|---|
| **Discente** | Aluno: consulta seu desempenho acadêmico |
| **Docente** | Professor: registra frequência e lança notas |
| **Gestor** | Supervisor: acompanha ocorrências e dados institucionais |

## Funcionalidades

### Telas funcionais (com persistência no PostgreSQL)

| Tela | Perfil | Requisitos |
|---|---|---|
| Login com matrícula ou CPF, sessão e logout | Todos | RF-01 |
| Diário de classe e frequência | Docente | RF-03, RN-02 |
| Lançamento de notas e cálculo da média | Docente | RF-04, RN-03 |
| Boletim: notas, médias, faltas e % de presença | Discente | RF-02, RN-01, RN-02, RN-03 |

Regras implementadas:

- **Notas** aceitas apenas de 0,0 a 10,0; valores fora da faixa são recusados e nada é gravado.
- **Média** simples `(nota1 + nota2) / 2`, calculada pelo Java antes de salvar.
- **% de faltas** `= 100 × faltas ÷ total de aulas`; **% de presença** `= 100 − % de faltas`. Sem aulas registradas, o boletim exibe "Sem base de cálculo".
- **RN-01:** o aluno vê somente os próprios registros, pois a consulta usa o usuário guardado na sessão.

### Telas de protótipo (HTML e CSS, dados fictícios)

- **Discente:** Entrega de Trabalhos (RF-05), Materiais de Aula (RF-06), Atestado de Matrícula (RF-07), Grade Horária (RF-11), Histórico Escolar (RF-12), Canal de Denúncias (RF-08)
- **Docente:** Publicar Materiais (RF-06), Correção e Feedback de Trabalhos (RF-16)
- **Gestor:** Painel de Ocorrências (RF-09), Gestão de Discentes (RF-13), Alocação Docente (RF-14), Relatórios Institucionais (RF-15)
- **Acesso público:** Validação de Atestado (RF-07) e Denúncia Anônima (RF-08)

## Como executar

### Pré-requisitos

- JDK 21
- PostgreSQL em execução na porta 5432
- Eclipse (ou outra IDE) com suporte a Maven

### Passos

1. Crie um banco vazio chamado `academix`:

   ```sql
   CREATE DATABASE academix;
   ```

2. Em `src/main/resources/application.properties`, informe o usuário e a senha do seu PostgreSQL:

   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=SUA_SENHA
   ```

3. Importe o projeto como **Existing Maven Project** e execute a classe `AcademixApplication`. Também é possível rodar pelo terminal:

   ```bash
   mvn spring-boot:run
   ```

4. Acesse [http://localhost:8080](http://localhost:8080).

As tabelas são criadas automaticamente (`ddl-auto=update`) e a classe `CargaInicial` insere os dados de teste na primeira execução.

## Usuários de teste

Senha de todos: `123`. O login aceita matrícula ou CPF.

| Perfil | Nome | Matrícula | CPF |
|---|---|---|---|
| Gestor | Gestor Academix | 2026001 | 11111111111 |
| Docente | Prof. Elton | 2026002 | 22222222222 |
| Discente | Diogo | 2026003 | 33333333333 |
| Discente | Mailson | 2026004 | 44444444444 |

> A senha é guardada em texto simples apenas para fins didáticos. Em um sistema real, use criptografia (por exemplo, BCrypt).

## Estrutura do projeto

```
src/main/java/br/ucsal/academix/
├── AcademixApplication.java   # ponto de entrada
├── CargaInicial.java          # dados de teste
├── model/                     # entidades JPA (tabelas)
├── repository/                # acesso ao banco (Spring Data JPA)
└── controller/                # rotas e regras de cada perfil

src/main/resources/
├── application.properties
├── static/css/estilo.css      # folha de estilo única
└── templates/                 # páginas Thymeleaf (discente, docente, gestor, publico)
```

## Fluxo de dados

```
HTML (formulário/link) → Controller → Repository → PostgreSQL
                                  ↓
            Model (dados) → Thymeleaf → HTML final no navegador
```

1. O navegador envia uma requisição (`GET` ou `POST`) para uma rota.
2. O **Controller** valida a sessão e as regras de negócio.
3. O **Repository** lê ou grava no PostgreSQL.
4. O Controller coloca os dados no `Model` e escolhe o template.
5. O **Thymeleaf** monta o HTML com `th:text`, `th:if` e `th:each`.

## Autores

- Preencha com os nomes da equipe
