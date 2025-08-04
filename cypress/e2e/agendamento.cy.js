// O describe agrupa testes relacionados.
describe('Fluxo de Agendamento de Coleta', () => {

  // O 'it' define um caso de teste específico.
  it('deve preencher e submeter o formulário com sucesso', () => {
    // 1. Visita a página inicial da aplicação.
    // A baseUrl "http://localhost:8080" será configurada no cypress.config.js
    cy.visit('/');

    // 2. Preenche os campos do formulário.
    cy.get('#nomeCidadao').type('Cidadão Teste E2E');
    cy.get('#email').type('cidadao.teste@email.com');
    cy.get('#telefoneCidadao').type('(41) 99999-8888');
    cy.get('#rua').type('Rua do Teste Automatizado');
    cy.get('#numero').type('123');
    cy.get('#bairro').type('Bairro dos Testes');
    cy.get('#cidade').type('Curitiba');
    cy.get('#cep').type('80000-000');

    // 3. Seleciona opções de rádio e checkbox.
    cy.get('input[name="tipoImovel"][value="Casa"]').check();
    cy.get('input[name="tiposMaterial"][value="1"]').check(); // Assume que o ID do "Papel" é 1
    cy.get('input[name="tiposMaterial"][value="2"]').check(); // Assume que o ID do "Plástico" é 2

    // 4. Seleciona uma data válida no futuro.
    const dataFutura = new Date();
    dataFutura.setDate(dataFutura.getDate() + 5); // Adiciona 5 dias
    const dataFormatada = dataFutura.toISOString().substring(0, 16); // Formato YYYY-MM-DDTHH:mm
    cy.get('#dataHoraSugerida').type(dataFormatada);

    // 5. Submete o formulário.
    cy.get('button[type="submit"]').click();

    // 6. Verifica o resultado.
    // Confirma que a URL mudou para a página de sucesso.
    cy.url().should('include', '/sucesso');
    // Confirma que a página de sucesso contém o texto esperado.
    cy.contains('Agendamento solicitado com sucesso!').should('be.visible');
    cy.contains('Cidadão Teste E2E').should('be.visible');
  });
});
