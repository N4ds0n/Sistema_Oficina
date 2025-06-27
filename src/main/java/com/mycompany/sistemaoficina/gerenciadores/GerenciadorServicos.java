package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.Servico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gerencia o catálogo de serviços oferecidos pela oficina.
 * Permite criar, listar, editar e remover serviços, com persistência em JSON.
 * @author santo
 */
public class GerenciadorServicos {

    private List<Servico> listaServicos;
    private static final String ARQUIVO_SERVICOS_JSON = "servicos.json";
    private boolean dadosForamModificados;
 
    /**
     * Retorna a lista de todos os servicos cadastrados no catalogo.
     * @return A lista de objetos Servico.
     */
    public List<Servico> getListaServicos(){
        return this.listaServicos;
    }

    /**
     * Construtor da classe GerenciadorServicos.
     * Carrega os servicos previamente salvos do arquivo JSON ao ser instanciado.
     */
    public GerenciadorServicos() {
        this.listaServicos = carregarServicos();
        this.dadosForamModificados = false;
    }
    
  /**
     * Exibe o menu principal para a gestao do catalogo de servicos.
     * Permite ao usuario acessar as funcionalidades de cadastro, listagem e edicao.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void gerenciarServicos(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR CATALOGO DE SERVICOS ===");
            System.out.println("1. Cadastrar Novo Servico");
            System.out.println("2. Listar Todos os Servicos");
            System.out.println("3. Editar Servico");
            System.out.println("4. Salvar alteracoes em Json");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um numero valido.");
                opcao = -1;
            }

            switch (opcao) {
                case 1: cadastrarNovoServico(scanner); break;
                case 2: listarServicos(); break;
                case 3: editarServico(scanner); break;
                case 4:
                    confirmarESalvar(scanner);
                case 0:
                if (dadosForamModificados) {
                        System.out.print("Voce tem alteracoes nao salvas. Deseja salva-las antes de sair? (S/N): ");
                        String resposta = scanner.nextLine();
                        if (resposta.equalsIgnoreCase("S")) {
                            salvarServicos();
                        }
                    }
                    System.out.println("Voltando ao menu principal...");
                    break;
                default: System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    /**
     * Conduz o fluxo de trabalho para cadastrar um novo servico no catalogo.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void cadastrarNovoServico(Scanner scanner) {
        System.out.println("\n--- Cadastrar Novo Servico ---");
        try {
            System.out.print("Descricao do servico (ex: Troca de Oleo): ");
            String descricao = scanner.nextLine();
            System.out.print("Valor do servico (ex: 80.00): ");
            double valor = Double.parseDouble(scanner.nextLine());

            int novoId = gerarProximoIdServico();
            Servico novoServico = new Servico(novoId, descricao, valor);
            this.listaServicos.add(novoServico);
            this.dadosForamModificados = true;
            System.out.println("Servico '" + descricao + "' cadastrado com sucesso! ID: " + novoId);
            System.out.println("Lembre-se de salvar as alteracoes no menu.");
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato. O valor deve ser um numero valido.");
        }
    }
    
    /**
     * Conduz o fluxo de trabalho para editar um servico existente.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void editarServico(Scanner scanner) {
        System.out.println("\n--- Editar Servico ---");
        listarServicos();
        if (this.listaServicos.isEmpty()) return;

        System.out.print("Digite o ID do servico para editar: ");
        try {
            int idServico = Integer.parseInt(scanner.nextLine());
            Servico servico = buscarServicoPorId(idServico);

            if (servico != null) {
                System.out.print("Nova descricao (Deixe em branco para nao alterar): ");
                String novaDescricao = scanner.nextLine();
                if (!novaDescricao.isBlank()) {
                    servico.setDescricao(novaDescricao);
                    this.dadosForamModificados = true;
                }

                System.out.print("Novo valor (Deixe em branco para nao alterar): ");
                String novoValorStr = scanner.nextLine();
                if (!novoValorStr.isBlank()) {
                    servico.setValor(Double.parseDouble(novoValorStr));
                    this.dadosForamModificados = true;
                }
                
                 if (dadosForamModificados) {
                    System.out.println("Servico atualizado em memoria!");
                    System.out.println("Lembre-se de salvar as alteracoes no menu.");
                } else {
                    System.out.println("Nenhuma alteracao foi feita.");
                }
                
            } else {
                System.out.println("Servico com ID " + idServico + " nao encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID ou valor invalido.");
        }
}

    /**
     * Pede ao usuario a confirmacao para salvar as alteracoes pendentes no arquivo JSON.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
private void confirmarESalvar(Scanner scanner) {
        if (!dadosForamModificados) {
            System.out.println("Nenhuma alteracao pendente para salvar.");
            return;
        }
        System.out.print("Deseja salvar todas as alteracoes no arquivo servicos.json? (S/N): ");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            salvarServicos();
            System.out.println("Alteracoes salvas com sucesso!");
        } else {
            System.out.println("Operacao cancelada. As alteracoes continuam apenas em memoria.");
        }
    }

 /**
     * Exibe no console a lista de todos os servicos oferecidos pela oficina.
     */
public void listarServicos() {
        System.out.println("\n--- Catalogo de Servicos Oferecidos ---");
        if (this.listaServicos.isEmpty()) {
            System.out.println("Nenhum servico cadastrado.");
            return;
        }
        for (Servico servico : this.listaServicos) {
            System.out.println(servico);
        }
        System.out.println("---------------------------------------");
    }

/**
     * Busca um servico na lista pelo seu ID unico.
     * @param idServico O ID do servico a ser procurado.
     * @return O objeto {@code Servico} se encontrado, ou {@code null}.
     */
public Servico buscarServicoPorId(int idServico) {
        for (Servico servico : this.listaServicos) {
            if (servico.getIdServico() == idServico) {
                return servico;
            }
        }
        return null;
    }

/**
     * Apenas exibe a lista de servicos. Nao permite edicao.
     * Metodo destinado a consulta rapida por funcionarios.
     */
public void consultarCatalogoServicos(){
    listarServicos();
}

/**
     * Gera um novo ID sequencial para um novo servico.
     * @return O proximo ID inteiro disponivel.
     */
   private int gerarProximoIdServico() {
        if (listaServicos.isEmpty()) {
            return 1;
        }
        return listaServicos.stream()
                .mapToInt(Servico::getIdServico)
                .max()
                .orElse(0) + 1;
    }

   /**
     * Persiste a lista atual de servicos no arquivo servicos.json.
     * Apos salvar, marca que nao ha mais dados modificados pendentes.
     */
    private void salvarServicos() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_SERVICOS_JSON)) {
            gson.toJson(this.listaServicos, writer);
            this.dadosForamModificados = false; // <-- MUDANÇA 7: Reseta a flag apos salvar
        } catch (IOException e) {
            System.err.println("Erro ao salvar servicos: " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de servicos a partir do arquivo servicos.json.
     * @return Uma {@code List<Servico>} com os dados carregados ou uma lista vazia.
     */
    private List<Servico> carregarServicos() {
        try (Reader reader = new FileReader(ARQUIVO_SERVICOS_JSON)) {
            Gson gson = new Gson();
            List<Servico> servicos = gson.fromJson(reader, new TypeToken<List<Servico>>(){}.getType());
            System.out.println("GerenciadorServicos: " + (servicos != null ? servicos.size() : 0) + " servicos carregados.");
            return servicos != null ? servicos : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorServicos: Arquivo 'servicos.json' nao encontrado. Iniciando com lista vazia.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar servicos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

