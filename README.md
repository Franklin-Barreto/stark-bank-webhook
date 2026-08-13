# Stark Bank Webhook Challenge

Aplicação Spring Boot que emite lotes de 8 a 12 Invoices a cada três horas na
Sandbox da Stark Bank e, ao receber `invoice.credited`, transfere o valor
recebido, descontada a eventual taxa da Invoice, para a conta definida no desafio.

A aplicação está publicada em:

- Health check: <https://stark-bank-webhook.duckdns.org/actuator/health>
- Webhook: <https://stark-bank-webhook.duckdns.org/webhooks/starkbank>

## Tecnologias

- Java 25 e Spring Boot 4
- PostgreSQL 17 e Liquibase
- Docker e Docker Compose
- SDK Java oficial da Stark Bank
- JUnit 5, Mockito e Testcontainers

## Arquitetura

O projeto é um monólito modular organizado por contexto:

~~~text
invoice
├── domain
├── application
└── infrastructure

transfer
├── domain
├── application
└── infrastructure

shared
└── infrastructure
~~~

As regras de negócio e os casos de uso não dependem do SDK da Stark Bank.
Integrações HTTP, persistência, scheduling e SDK ficam nas camadas de infraestrutura.

~~~text
Scheduler
  → gera um lote aleatório com 8 a 12 Invoices
  → emite o lote na Stark Bank

invoice.credited
  → valida a assinatura do webhook
  → calcula amount - fee
  → verifica se a Invoice já foi processada
  → cria a Transfer
  → registra a Invoice como processada
~~~

Uma conciliação opcional consulta eventos `invoice.credited` ainda não
entregues. Ela pode executar no início da aplicação e periodicamente, cobrindo
períodos em que o webhook esteve indisponível sem depender de um reinício.

## Decisões de resiliência

### Idempotência

O ID da Invoice é usado como `externalId` da Transfer e como chave primária da
tabela `processed_invoice_credit`. O banco local impede o reprocessamento de
webhooks repetidos, enquanto a Stark Bank protege a movimentação financeira
contra Transfers repetidas com o mesmo `externalId`.

Na Sandbox, a segunda Transfer é inicialmente criada com outro ID e status
`created`, mas posteriormente muda para `failed`; somente a primeira chega a
`success`. Portanto, a duplicidade não foi modelada como uma exceção síncrona.

### Falhas temporárias

Cada Transfer é criada com `resendingLimit = 5`. Falhas temporárias podem ser
retentadas pela Stark Bank. O acompanhamento de falhas definitivas, alertas e
intervenção operacional é uma evolução, pois o desafio não define essa política.

### Eventos não relacionados

Somente `invoice.credited` produz uma Transfer. Outros eventos válidos recebem
HTTP 200 e são ignorados. O processamento não depende da ordem dos eventos
anteriores da Invoice.

## Pré-requisitos

- JDK 25 e Maven 3.9+
- Docker com Docker Compose
- Project com permissão de administrador na Sandbox
- Chave privada secp256k1 do Project

## Configuração

~~~bash
cp .env.example .env
~~~

| Variável | Padrão | Finalidade |
| --- | --- | --- |
| `DB_HOST` | `localhost` | Host do PostgreSQL |
| `DB_PORT` | `5432` | Porta do PostgreSQL |
| `DB_NAME` | `starkbank` | Nome do banco |
| `DB_USER` | `starkbank` | Usuário do banco |
| `DB_PASSWORD` | `starkbank` | Senha do banco |
| `SERVER_PORT` | `8080` | Porta HTTP publicada |
| `STARKBANK_ENABLED` | `false` | Habilita a integração |
| `STARKBANK_ENVIRONMENT` | `sandbox` | Ambiente da Stark Bank |
| `STARKBANK_PROJECT_ID` | vazio | ID do Project |
| `STARKBANK_PRIVATE_KEY` | vazio | PEM direto, útil fora do Docker |
| `STARKBANK_PRIVATE_KEY_SOURCE` | arquivo local | PEM montado como secret |
| `STARKBANK_INVOICE_RUN_ON_STARTUP` | `false` | Emite um lote ao iniciar |
| `STARKBANK_INVOICE_SCHEDULING_ENABLED` | `false` | Habilita emissão periódica |
| `STARKBANK_INVOICE_SCHEDULING_INTERVAL` | `3h` | Intervalo entre lotes |
| `STARKBANK_EVENT_RECONCILIATION_RUN_ON_STARTUP` | `false` | Concilia eventos ao iniciar |
| `STARKBANK_EVENT_RECONCILIATION_SCHEDULING_ENABLED` | `false` | Habilita conciliação periódica |
| `STARKBANK_EVENT_RECONCILIATION_SCHEDULING_INTERVAL` | `15m` | Intervalo após cada conciliação |

O `.env` e arquivos `*.pem` são ignorados pelo Git.

## Execução local com Maven

~~~bash
docker compose up -d postgres

set -a
source ./.env
set +a

STARKBANK_PRIVATE_KEY="$(<./stark-bank-sandbox-private-key.pem)" \
  mvn spring-boot:run
~~~

Verifique com `curl http://localhost:8080/actuator/health`.

## Execução com Docker Compose

Defina no `.env`:

~~~env
STARKBANK_PRIVATE_KEY_SOURCE=./stark-bank-sandbox-private-key.pem
~~~

Suba os serviços:

~~~bash
docker compose up -d --build
docker compose ps
~~~

O PEM é montado somente como arquivo em
`/run/secrets/starkbank_private_key`. Ele não é copiado para a imagem nem
injetado como variável de ambiente. O PostgreSQL é publicado apenas em
`127.0.0.1`; os containers se comunicam pela rede interna do Compose.

Como a aplicação executa com usuário não-root (UID 1001), esse usuário precisa
de permissão de leitura no arquivo de origem. Em Linux, prefira uma ACL:

~~~bash
setfacl -m u:1001:r stark-bank-sandbox-private-key.pem
~~~

## Campanha de 24 horas

O período é controlado operacionalmente. Depois de validar aplicação, banco e
webhook, habilite:

~~~env
STARKBANK_INVOICE_SCHEDULING_ENABLED=true
STARKBANK_INVOICE_SCHEDULING_INTERVAL=3h
~~~

Recrie a aplicação e registre o horário de início:

~~~bash
docker compose up -d --force-recreate app
docker compose logs -f app
~~~

Após 24 horas, altere `STARKBANK_INVOICE_SCHEDULING_ENABLED=false` e recrie
`app`. O webhook permanece ativo para receber créditos posteriores.

## Testes e qualidade

~~~bash
mvn test
mvn verify
~~~

Os testes de persistência executam Liquibase e JPA contra PostgreSQL 17
descartável via Testcontainers. O `verify` também aplica a formatação e verifica
a cobertura mínima de 70% configurada no JaCoCo.

## Deploy manual na AWS

O bônus de cloud usa uma EC2 Ubuntu, dois containers e deploy manual:

~~~text
Internet
  → Caddy (80/443, HTTPS automático)
  → Spring Boot (8080)
  → PostgreSQL (rede interna)
~~~

Procedimento:

1. Criar EC2 com Elastic IP.
2. Liberar SSH somente para o responsável e HTTP/HTTPS publicamente.
3. Não liberar 8080 e 5432 no Security Group.
4. Instalar Docker, Docker Compose, Git e Caddy.
5. Clonar com deploy key somente leitura.
6. Criar `.env` e copiar o PEM diretamente para a EC2.
7. Subir com `docker compose up -d --build`.
8. Apontar o DNS para o Elastic IP.
9. Configurar o Caddy:

~~~caddyfile
stark-bank-webhook.duckdns.org {
    reverse_proxy localhost:8080
}
~~~

10. Registrar `/webhooks/starkbank` na Stark Bank para eventos de Invoice.

Credenciais, chaves e senhas não são armazenadas no repositório nem na imagem.

## Limitações e evoluções

- O encerramento da campanha de 24 horas é operacional.
- Falhas definitivas de Transfer exigiriam webhooks, persistência de status,
  alertas e conciliação próprios.
- A criação remota da Transfer e o registro local não são atômicos; o
  `externalId` mitiga duplicação financeira em retries.
