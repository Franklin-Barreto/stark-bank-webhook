# Stark Bank Webhook Challenge

Aplicação Spring Boot responsável por emitir lotes periódicos de Invoices na Sandbox da Stark Bank e transferir o valor líquido de cada Invoice creditada.

## Tecnologias

- Java 25
- Spring Boot 4
- PostgreSQL
- Liquibase
- Docker Compose
- SDK Java oficial da Stark Bank
- JUnit 5 e Testcontainers

## Pré-requisitos

- JDK 25
- Maven 3.9+
- Docker

## Execução local

Copie o arquivo de exemplo e ajuste apenas os valores necessários:

```bash
cp .env.example .env
```

Suba o PostgreSQL:

```bash
docker compose up -d postgres
```

Execute a aplicação:

```bash
mvn spring-boot:run
```

Verifique a saúde da aplicação:

```bash
curl http://localhost:8080/actuator/health
```

## Testes e qualidade

O comando abaixo executa os testes, aplica a formatação e verifica a cobertura mínima configurada no JaCoCo:

```bash
mvn verify
```

Os testes de integração usam um PostgreSQL descartável iniciado pelo Testcontainers.

## Configuração

| Variável | Padrão local | Finalidade |
| --- | --- | --- |
| `DB_HOST` | `localhost` | Host do PostgreSQL |
| `DB_PORT` | `5432` | Porta do PostgreSQL |
| `DB_NAME` | `starkbank` | Nome do banco |
| `DB_USER` | `starkbank` | Usuário do banco |
| `DB_PASSWORD` | `starkbank` | Senha do banco |
| `SERVER_PORT` | `8080` | Porta HTTP da aplicação |
| `STARKBANK_ENVIRONMENT` | `sandbox` | Ambiente da API Stark Bank |
| `STARKBANK_PROJECT_ID` | sem padrão | Identificador do Project |
| `STARKBANK_PRIVATE_KEY` | sem padrão | Chave privada do Project |

O arquivo `.env` e arquivos `*.pem` são ignorados pelo Git. Credenciais reais não devem ser versionadas.