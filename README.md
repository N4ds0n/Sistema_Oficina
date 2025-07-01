# 📦 Sistema de Gerenciamento de Oficina Mecânica

> Projeto de Programação Orientada a Objetos desenvolvido para uma Oficina Mecânica, com foco em controle de serviços, estoque, agendamentos, colaboradores e fluxo financeiro da empresa.

## 📋 Descrição

Este sistema foi desenvolvido com base em um cenário realista, simulando a gestão completa de uma **oficina mecânica com loja de peças e serviços automotivos**. A aplicação inclui:

* Controle de clientes, veículos e colaboradores (inclusive autenticação).
* Sistema de agendamento com verificação de disponibilidade de elevadores.
* Gerenciamento de ordens de serviço e status dos veículos.
* Controle de estoque e registro de compras com fornecedores.
* Geração de extratos, notas fiscais e balanços financeiros mensais.
* Salvamento e recuperação dos dados em arquivos `.json`.

## 🛠 Funcionalidades

* [x] Incluir, editar e remover clientes (com CPF pseudo-anonimizado).
* [x] Incluir, editar e remover colaboradores e administradores.
* [x] Alterar senha do administrador.
* [x] Realizar e cancelar agendamentos (com retenção de 20% no cancelamento).
* [x] Verificar e atualizar estoque da loja.
* [x] Receber produtos de fornecedores e atualizar o estoque.
* [x] Gerar relatórios diários e mensais de vendas e serviços.
* [x] Gerar balanço financeiro mensal com estatísticas.
* [x] Autenticação de usuários com restrição de permissões.
* [x] Impressão e salvamento de extratos por cliente.
* [x] Controle de ponto e gastos gerais da oficina.

## 🧱 Estrutura do Projeto

* Programação orientada a objetos com boas práticas de encapsulamento, herança e polimorfismo.
* Interface `Comparator` aplicada para ordenação de agendamentos e clientes por diferentes critérios.
* Uso de atributos `static` para controle de instâncias e gerenciamento de recursos compartilhados.
* Classes implementadas: `Cliente`, `Veiculo`, `Funcionario`, `Administrador`, `Produto`, `Estoque`, `Agendamento`, `OrdemDeServico`, `RelatorioFinanceiro`, `Sistema`, entre outras.
* Persistência de dados em arquivos `.json` utilizando a biblioteca **Gson**.
* Uso de vetores fixos para modelar os 3 elevadores da oficina.

## 🧾 Tecnologias Utilizadas

* Java (JDK 17+)
* Biblioteca Gson para manipulação de JSON
* Programação orientada a objetos
* JavaDoc para documentação do projeto

## 📚 Documentação

* ✅ Diagrama de Casos de Uso (UML)
* ✅ Diagrama de Classes completo com atributos e métodos
* ✅ Fluxo de eventos (cenários) para todos os casos de uso
* ✅ JavaDoc gerado com explicações de todas as classes e métodos

## 💡 Conceitos Aplicados

* Encapsulamento
* Herança e polimorfismo
* Classes abstratas e interfaces
* Vetores e listas
* Manipulação de arquivos
* Estruturação modular e reutilizável

## 📈 Melhorias Futuras

* [ ] Interface gráfica (GUI) com JavaFX ou Swing.
* [ ] Integração com banco de dados.
* [ ] Envio de e-mails automáticos ao cliente após o agendamento ou entrega.

## 🔐 Acesso e Permissões

* **Administrador**: acesso completo ao sistema.
* **Colaborador**: acesso restrito (sem permissão para visualizar despesas e balanços financeiros).

---

## 👤 Autor

* **Nádson Nascimento Santos**
  Aluno do curso de Sistemas de Informação na UFVJM
  E-mail: [seuemail@exemplo.com](santosnadson329@gmail.com)

