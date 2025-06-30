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
 */
public class GerenciadorServicos {

    private List<Servico> listaServicos;
    private static final String ARQUIVO_SERVICOS_JSON = "servicos.json";
    private boolean dadosForamModificados;
    
    public List<Servico> getListaServicos(){
        return this.listaServicos;
    }

    /**
     * Construtor. Carrega os serviços do arquivo JSON ao iniciar.
     */
    public GerenciadorServicos() {
        this.listaServicos = carregarServicos();
        this.dadosForamModificados = false;
    }
    
    /**
     * Menu Catalogo de Serviços
     * @param scanner 
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
     * Métodos de fluxo de trabalho
     * @param scanner 
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

    // Métodos auxiliares de persistencia
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
 * Para consulta por funcionarios.
 */
public void consultarCatalogoServicos(){
    listarServicos();
}

   private int gerarProximoIdServico() {
        if (listaServicos.isEmpty()) {
            return 1;
        }
        return listaServicos.stream()
                .mapToInt(Servico::getIdServico)
                .max()
                .orElse(0) + 1;
    }

    private void salvarServicos() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_SERVICOS_JSON)) {
            gson.toJson(this.listaServicos, writer);
            this.dadosForamModificados = false; // <-- MUDANÇA 7: Reseta a flag apos salvar
        } catch (IOException e) {
            System.err.println("Erro ao salvar servicos: " + e.getMessage());
        }
    }

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

