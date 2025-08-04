# Projeto de Agendamento de Coleta de Recicláveis

Este é um sistema web para agendamento de coletas de materiais recicláveis.

## Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
- [JDK 17](https://adoptium.net/) ou superior.
- [Node.js (LTS)](https://nodejs.org/en/) (que já inclui o npm).
- [PostgreSQL](https://www.postgresql.org/download/).

## Configuração do Banco de Dados

1. Crie um banco de dados no PostgreSQL chamado `reciclagem_db`.
2. Abra o arquivo `src/main/resources/application.properties`.
3. Altere as propriedades `spring.datasource.username` e `spring.datasource.password` com o seu usuário e senha do PostgreSQL.

## Rodando a Aplicação (Backend)

Para iniciar a aplicação Spring Boot, execute o seguinte comando no terminal, na raiz do projeto:


mvnw.cmd spring-boot:run

A aplicação estará disponível em `http://localhost:8080`.

## Rodando os Testes

### Testes de Backend (Unitários/API)

Para executar os testes do backend com JUnit e MockMvc, use o comando:

./mvn.cmd test

### Testes de Ponta a Ponta (E2E com Cypress)

1. Primeiro, instale as dependências do Node.js:
   
    npm install
   
2. Com a aplicação backend rodando, abra o Cypress Test Runner com o comando:
   
   npx cypress open
   
3. Na janela do Cypress, clique no teste `agendamento.cy.js` para executá-lo.

## Sugestão de melhoria no sistema

1. Enviar um e-mail ao cidadão contendo as informações do agendamento realizado no site para garantir que o mesmo tenha as informações necessárias.
2. No formulário de agendamento foi alterado o endereço para ser preenchido em seus respectivos campos, separando por: rua, número, bairro, CEP e uma validação para marcar se é casa ou apartamento.

## Especificações de Uso (Gherkin)

O documento que contém as especificações de uso estão no arquivo 'Especificações de Uso.txt' na pasta 'Relatórios'

## Plano de Testes

O plano de teste e sua estratégia está no arquivo 'Plano de testes.txt' na pasta 'Relatórios'

## Bug Identificado

Durante o teste foi identificado um bug que estava permitindo agendar com data menor que 2 dias úteis.
As informações estão no arquivo 'Relatório de Execução de Teste - Sistema de Agendamento de Reciclagem.docx' na pasta 'Relatórios'

## Requisito RQNF12

Todos os requisitos funcionais e não funcionais do sistema foram entregues.

## SonarQube 
Não foi utilizado.
