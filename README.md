# 🔧 Sistema de Gestão para Oficina Mecânica

Este projeto é um sistema completo de gestão para uma oficina mecânica, desenvolvido em Java como parte de um trabalho prático da disciplina de Programação Orientada a Objetos (POO). O sistema gerencia **agendamentos de serviços**, **cadastros de clientes e funcionários**, **controle de estoque e vendas**, **despesas da oficina**, **elevadores**, **ordens de serviço** e **relatórios financeiros**.

## 🚀 Funcionalidades

- ✅ **Autenticação de Usuários** (Administrador e Colaboradores)
- 👤 **Gerenciamento de Clientes**: incluir, editar, excluir e pseudo-anonimizar CPF
- 🧑‍🔧 **Gestão de Funcionários**: colaboradores e administradores, com alteração de senha
- 📅 **Agendamentos**: com verificação de disponibilidade e status do veículo (Recebido, Em manutenção, Pronto, Entregue)
- ⚙️ **Ordem de Serviço**: associação entre cliente, veículo, mecânico e peças/serviços utilizados
- 🛒 **Loja de Peças**: controle de estoque, verificação de produtos, recebimento de fornecedores
- 📉 **Controle de Despesas**: lançamentos diversos (limpeza, material, café, etc.)
- 💰 **Relatórios e Balanço Mensal**: relatório diário e mensal de serviços/vendas, com estatísticas básicas
- 🧾 **Emissão de Nota/Extrato**: para cada serviço ou venda realizada
- 🔄 **Persistência em Arquivos JSON**: clientes, agendamentos, veículos, estoque, colaboradores, etc.
- ⚖️ **Cancelamento com Multa**: cancelamentos retêm 20% do valor do agendamento
- 📈 **Comparator**: comparação de clientes e agendamentos por diferentes critérios
- 🛠️ **Gestão de 3 Elevadores**: estrutura estática com controle fixo dos elevadores da oficina

## 🧩 Arquitetura do Sistema

- Orientado a Objetos (POO)
- Encapsulamento, herança e polimorfismo aplicados
- Subclasses com construtores utilizando `super`
- Sobrescrita do método `toString()` em todas as classes
- Contador de objetos `Veículo` com duas estratégias:
  - `private static` com getters/setters
  - `protected static` com acesso direto

## 📂 Estrutura de Classes (Resumo)

- `Sistema.java`
- `Cliente.java`
- `Colaborador.java`
- `Administrador.java`
- `Veiculo.java`
- `Agendamento.java`
- `OrdemDeServico.java`
- `Produto.java`
- `Estoque.java`
- `RelatorioDeVendas.java`
- `Despesa.java`
- `Elevador.java`
- Utilitários para leitura/gravação em arquivos JSON

## 💾 Persistência de Dados

Todos os dados do sistema são armazenados em arquivos `.json` utilizando bibliotecas externas para manipulação com segurança e eficiência. Os arquivos são carregados na inicialização e salvos após alterações.

## 📊 Relatórios

- Relatórios de vendas e serviços por dia e por mês
- Balanço mensal com:
  - Total de receitas
  - Total de despesas
  - Lucro/prejuízo
  - Quantidade de atendimentos

## 🔐 Controle de Acesso

- **Administrador**: acesso total ao sistema
- **Colaborador**: acesso limitado (sem visualização de despesas e relatórios financeiros)

## 🛠️ Tecnologias Utilizadas

- Linguagem: **Java**
- Paradigmas: **POO (Programação Orientada a Objetos)**
- Persistência: **JSON (Leitura e gravação de arquivos)**
- Comparações: **Interface Comparator**
- Estruturas de dados: **Listas dinâmicas, vetores estáticos**

---

## 📌 Observações

Este sistema foi idealizado e implementado com foco no controle interno de uma oficina real, simulando o ambiente de trabalho e integrando conceitos de programação aprendidos ao longo da disciplina. 

---

## 🤝 Contribuições

Sinta-se livre para abrir issues, sugerir melhorias ou adaptar este projeto para a realidade da sua oficina.

