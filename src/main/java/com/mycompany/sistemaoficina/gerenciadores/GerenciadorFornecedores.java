package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.Fornecedor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gerencia todas as operacoes relacionadas aos Fornecedores da oficina.
 * @author santo
 */
public class GerenciadorFornecedores {
    
    private final List<Fornecedor> listaFornecedores;
    private static final String ARQUIVO_FORNECEDORES_JSON = "fornecedores.json";

   /**
    * Construtor. Carrega os fornecedores do arquivo JSON ao iniciar.
    */
    public GerenciadorFornecedores() {
        this.listaFornecedores = carregarFornecedores();
    }

    /**
     * Retorna a lista de fornecedores.
     * @return A lista de objetos Fornecedor.
     */
    public List<Fornecedor> getListaFornecedores() {
        return this.listaFornecedores;
    }
    
    /**
     * Exibe o menu principal para a gestao de fornecedores e processa a escolha do usuario.
     * Permite o acesso as funcionalidades de cadastro, listagem e edicao.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void gerenciarFornecedores(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR FORNECEDORES ===");
            System.out.println("1. Cadastrar Novo Fornecedor");
            System.out.println("2. Listar Todos os Fornecedores");
            System.out.println("3. Editar Fornecedor");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um numero valido.");
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    cadastrarNovoFornecedor(scanner);
                    break;
                case 2:
                    listarFornecedores();
                    break;
                case 3:
                    editarFornecedor(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    /**
     * Conduz o fluxo de trabalho para cadastrar um novo fornecedor.
     * Pede ao usuario os dados necessarios, cria o objeto Fornecedor e o adiciona a lista,
     * persistindo as alteracoes no final.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void cadastrarNovoFornecedor(Scanner scanner) {
        System.out.println("\n--- Cadastrar Novo Fornecedor ---");
        try {
            System.out.print("Nome Fantasia: ");
            String nomeFantasia = scanner.nextLine();
            System.out.print("Razao Social: ");
            String razaoSocial = scanner.nextLine();
            System.out.print("CNPJ: ");
            String cnpj = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();

            int novoId = gerarProximoIdFornecedor();
            Fornecedor novoFornecedor = new Fornecedor(novoId, nomeFantasia, razaoSocial, cnpj, telefone, email);
            
            this.listaFornecedores.add(novoFornecedor);
            salvarFornecedores();
            System.out.println("Fornecedor '" + nomeFantasia + "' cadastrado com sucesso! ID: " + novoId);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro durante o cadastro: " + e.getMessage());
        }
    }

    /**
     * Conduz o fluxo de trabalho para editar os dados de um fornecedor existente.
     * Permite que o usuario altere os campos do fornecedor, deixando em branco para manter o valor atual.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void editarFornecedor(Scanner scanner) {
        System.out.println("\n--- Editar Fornecedor ---");
        listarFornecedores();
        if (this.listaFornecedores.isEmpty()) {
            return;
        }

        System.out.print("Digite o ID do fornecedor para editar: ");
        try {
            int idFornecedor = Integer.parseInt(scanner.nextLine());
            Fornecedor fornecedor = buscarFornecedorPorId(idFornecedor);

            if (fornecedor != null) {
                System.out.print("Novo Nome Fantasia (Atual: " + fornecedor.getNomeFantasia() + "): ");
                String nomeFantasia = scanner.nextLine();
                if (!nomeFantasia.isBlank()) fornecedor.setNomeFantasia(nomeFantasia);

                System.out.print("Nova Razao Social (Atual: " + fornecedor.getRazaoSocial() + "): ");
                String razaoSocial = scanner.nextLine();
                if (!razaoSocial.isBlank()) fornecedor.setRazaoSocial(razaoSocial);

                System.out.print("Novo CNPJ (Atual: " + fornecedor.getCnpj() + "): ");
                String cnpj = scanner.nextLine();
                if (!cnpj.isBlank()) fornecedor.setCnpj(cnpj);

                System.out.print("Novo Telefone (Atual: " + fornecedor.getTelefone() + "): ");
                String telefone = scanner.nextLine();
                if (!telefone.isBlank()) fornecedor.setTelefone(telefone);

                System.out.print("Novo Email (Atual: " + fornecedor.getEmail() + "): ");
                String email = scanner.nextLine();
                if (!email.isBlank()) fornecedor.setEmail(email);

                salvarFornecedores();
                System.out.println("Fornecedor atualizado com sucesso!");
            } else {
                System.out.println("Fornecedor com ID " + idFornecedor + " nao encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }
    
    /**
     * Exibe no console a lista de todos os fornecedores cadastrados.
     * Apresenta o ID e o nome fantasia de cada um para facil identificacao.
     */
    public void listarFornecedores() {
        System.out.println("\n--- Lista de Fornecedores ---");
        if (this.listaFornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado.");
            return;
        }
        for (Fornecedor fornecedor : this.listaFornecedores) {
            System.out.println("ID: " + fornecedor.getIdFornecedor() + " | Nome: " + fornecedor.getNomeFantasia());
        }
        System.out.println("-----------------------------");
    }

    /**
     * Busca um fornecedor na lista em memoria pelo seu ID unico.
     * @param idFornecedor O ID do fornecedor a ser procurado.
     * @return O objeto {@code Fornecedor} se encontrado, ou {@code null} caso contrario.
     */
    public Fornecedor buscarFornecedorPorId(int idFornecedor) {
        for (Fornecedor fornecedor : this.listaFornecedores) {
            if (fornecedor.getIdFornecedor() == idFornecedor) {
                return fornecedor;
            }
        }
        return null;
    }

    /**
     * Gera um novo ID sequencial para um novo fornecedor.
     * O ID e baseado no maior ID existente na lista, incrementado de 1.
     * @return O proximo ID inteiro disponivel.
     */
    private int gerarProximoIdFornecedor() {
        if (listaFornecedores.isEmpty()) {
            return 1;
        }
        return listaFornecedores.stream()
                .mapToInt(Fornecedor::getIdFornecedor)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Persiste a lista atual de fornecedores no arquivo fornecedores.json.
     * Utiliza a biblioteca Gson para serializar a lista de objetos.
     */
    private void salvarFornecedores() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_FORNECEDORES_JSON)) {
            gson.toJson(this.listaFornecedores, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar fornecedores: " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de fornecedores a partir do arquivo fornecedores.json.
     * Se o arquivo nao for encontrado, inicializa uma lista vazia.
     * @return Uma {@code List<Fornecedor>} com os dados carregados ou uma lista vazia.
     */
    private List<Fornecedor> carregarFornecedores() {
        try (Reader reader = new FileReader(ARQUIVO_FORNECEDORES_JSON)) {
            List<Fornecedor> fornecedores = new Gson().fromJson(reader, new TypeToken<List<Fornecedor>>(){}.getType());
            System.out.println("GerenciadorFornecedores: " + (fornecedores != null ? fornecedores.size() : 0) + " fornecedores carregados.");
            return fornecedores != null ? fornecedores : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorFornecedores: Arquivo 'fornecedores.json' nao encontrado.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar fornecedores: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
}
