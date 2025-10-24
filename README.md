# 🅿️ ParkFlow Backend

Sistema inteligente de gerenciamento de estacionamento desenvolvido com **Spring Boot 3** e **Java 17**.  
Permite registrar entrada e saída de veículos, calcular valor por tempo estacionado e gerenciar vagas em tempo real — tudo com **boas práticas de engenharia de software**, **testes automatizados** e **arquitetura limpa**.

> ✨ **Ideal para demonstrar competências em Spring Boot, JPA, testes e design de APIs RESTful** — perfeito para candidaturas a **Desenvolvedor Java Jr.**.

---

## 🚀 Funcionalidades

- ✅ **Registro de entrada** de veículos (carro ou moto)
- ✅ **Alocação automática** de vaga disponível
- ✅ **Registro de saída** com cálculo de valor (R$5,00/hora, mínimo 1h)
- ✅ **Liberação automática** da vaga após pagamento
- ✅ **Validação de placa** e estado do veículo
- ✅ **Tratamento global de erros** com mensagens claras e estruturadas

---

## 🛠 Tecnologias

| Camada | Tecnologia |
|-------|-----------|
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 3.3+ |
| **Web** | Spring Web (REST API) |
| **Persistência** | Spring Data JPA + Hibernate |
| **Banco (dev)** | H2 Database (em memória) |
| **Banco (prod)** | PostgreSQL (suporte configurado) |
| **Testes** | JUnit 5, AssertJ, Spring Boot Test |
| **Qualidade** | Validação com Bean Validation, tratamento de exceções |
| **Build** | Maven |

---

## ▶️ Como rodar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/AlvesCosta08/parkflow-backend.git
   cd parkflow-backend

```
parkflow/
├── domain/
│   ├── Vaga.java
│   ├── RegistroEstacionamento.java
│   └── TipoVeiculo.java
├── dto/
│   ├── EntradaVeiculoDTO.java
│   └── SaidaVeiculoDTO.java
├── repository/
│   ├── VagaRepository.java
│   └── RegistroEstacionamentoRepository.java
├── service/
│   └── EstacionamentoService.java
├── controller/
│   └── EstacionamentoController.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── EstacionamentoException.java
└── ParkFlowApplication.java
```


## 🧪 Testes 

O projeto inclui testes unitários e de integração para garantir a confiabilidade das funcionalidades principais. Todos os testes utilizam **JUnit 5**, **AssertJ** e **Spring Boot Test**.

### Cenários testados

| Cenário | Descrição |
|--------|----------|
| ✅ **Entrada bem-sucedida** | Registra veículo e ocupa vaga corretamente |
| ✅ **Veículo já estacionado** | Rejeita nova entrada com placa já ativa |
| ✅ **Vaga indisponível** | Lança exceção quando não há vaga do tipo solicitado |
| ✅ **Saída com cálculo de valor** | Calcula valor com base no tempo (mínimo R$5,00) |
| ✅ **Saída de veículo inexistente** | Rejeita saída com placa não estacionada |
| ✅ **Saída de veículo já pago** | Impede nova saída após pagamento |

### Como rodar os testes

```
./mvnw test
```

Próximos passos" ou "Roadmap

🗺 Roadmap
Adicionar script data.sql para inicializar vagas (A1–A10, M1–M5)
Configurar GitHub Actions para CI (build + testes automáticos)
Criar frontend Angular 17+ (parkflow-frontend)
Migrar para PostgreSQL em ambiente de produção
Implementar autenticação JWT (login de operador)
Adicionar relatórios diários (faturamento, ocupação)