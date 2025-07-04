package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.Fornecedor;
import com.mycompany.sistemaoficina.Produto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gerencia todas as operacoes relacionadas ao estoque de produtos da oficina.
 * Controla o inventario de pecas, incluindo adicao, atualizacao e persistencia de dados,
 * alem de interagir com o GerenciadorFornecedores para vincular produtos a sua origem.
 * @author santo
 */
public class GerenciadorEstoque {

    private List<Produto> listaProdutos;
    private static final String ARQUIVO_ESTOQUE_JSON = "estoque.json";
    private boolean dadosForamModificados;
    private final GerenciadorFornecedores gerenciadorFornecedores;

    /**
     * Retorna a lista de produtos atualmente em memoria.
     * @return A lista de objetos Produto.
     */
public List<Produto> getListaProdutos(){
    return this.listaProdutos;
}

/**
     * Construtor da classe GerenciadorEstoque.
     * Carrega os produtos previamente salvos do arquivo JSON e recebe a dependencia
     * do GerenciadorFornecedores para poder interagir com ele.
     * @param gf A instancia principal do GerenciadorFornecedores.
     */
    public GerenciadorEstoque(GerenciadorFornecedores gf) {
        this.gerenciadorFornecedores = gf;
        this.listaProdutos = carregarEstoque();
        this.dadosForamModificados = false;
    }

   /**
     * Exibe o menu principal para a gestao do estoque.
     * Permite ao usuario acessar as funcionalidades de cadastro, listagem, busca e atualizacao de produtos.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void gerenciarEstoque(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR ESTOQUE ===");
            System.out.println("1. Adicionar Novo Produto");
            System.out.println("2. Listar Todos os Produtos");
            System.out.println("3. Adicionar Estoque (Entrada de Fornecedor)");
            System.out.println("4. Buscar Produto por ID");
            System.out.println("5. Salvar Alteracoes em Json");
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
                    adicionarNovoProduto(scanner);
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    entradaDeEstoque(scanner);
                    break;
                case 4:
                    buscarEExibirProduto(scanner);
                    break;
                case 5:
                    confirmarESalvar(scanner);
                    break;
                case 0:
                    if (dadosForamModificados) {
                        System.out.print("Voce tem alteracoes nao salvas. Deseja salva-las antes de sair? (S/N): ");
                        String resposta = scanner.nextLine();
                        if (resposta.equalsIgnoreCase("S")) {
                            salvarEstoque();
                        }
                    }
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }
    
    /**
     * Conduz o fluxo de trabalho para cadastrar um novo produto.
     * Exige a selecao de um fornecedor ja cadastrado para vincular ao produto.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void adicionarNovoProduto(Scanner scanner) {
        System.out.println("\n--- Adicionar Novo Produto ao Estoque ---");
        
        // Verifica se algum eciste algum fornecedor cadaastrado antes de prosseguir
        if (gerenciadorFornecedores.getListaFornecedores().isEmpty()) {
            System.out.println("Erro: E necessario cadastrar um fornecedor antes de adicionar produtos.");
            System.out.println("Por favor, va ao menu 'Gerenciar Fornecedores' e cadastre um.");
            return;
        }
        
        try {
            // Lista os fornecedores para o usuario escolher
            System.out.println("Selecione o fornecedor deste produto:");
            gerenciadorFornecedores.listarFornecedores();
            System.out.print("Digite o ID do fornecedor: ");
            int idFornecedor = Integer.parseInt(scanner.nextLine());
            Fornecedor fornecedorSelecionado = gerenciadorFornecedores.buscarFornecedorPorId(idFornecedor);

            // Se o fornecedor nao for encontrado, cancela a operacao
            if (fornecedorSelecionado == null) {
                System.out.println("Fornecedor com ID " + idFornecedor + " nao encontrado.");
                return;
            }
            
            System.out.print("Nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Descricao: ");
            String descricao = scanner.nextLine();
            System.out.print("Preco de custo (ex: 25.50): ");
            double precoCusto = Double.parseDouble(scanner.nextLine());
            System.out.print("Preco de venda (ex: 49.90): ");
            double precoVenda = Double.parseDouble(scanner.nextLine());
            System.out.print("Quantidade inicial em estoque: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            int novoId = gerarProximoIdProduto();
            Produto novoProduto = new Produto(novoId, nome, descricao, precoCusto, precoVenda, quantidade, fornecedorSelecionado);
            this.listaProdutos.add(novoProduto);
            this.dadosForamModificados = true;
            System.out.println("Produto '" + nome + "' adicionado em memoria! ID: " + novoId);
            System.out.println("Lembre-se de salvar as alteracoes no menu.");
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato. Certifique-se de usar numeros validos para precos e quantidade.");
        }
    }

    /**
     * Conduz o fluxo de trabalho para adicionar quantidade a um produto ja existente no estoque.
     * Simula a entrada de mercadoria de um fornecedor.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void entradaDeEstoque(Scanner scanner) {
        System.out.println("\n--- Adicionar Estoque (Entrada de Fornecedor) ---");
        listarProdutos();
        if (this.listaProdutos.isEmpty()) return;

        System.out.print("Digite o ID do produto para adicionar estoque: ");
        try {
            int idProduto = Integer.parseInt(scanner.nextLine());
            Produto produto = buscarProdutoPorId(idProduto);

            if (produto != null) {
                System.out.print("Digite a quantidade a ser adicionada: ");
                int quantidade = Integer.parseInt(scanner.nextLine());
                produto.adicionarEstoque(quantidade);
                this.dadosForamModificados = true;
                System.out.println("Estoque do produto '" + produto.getNome() + "' atualizado para: " + produto.getQuantidadeEstoque());
                System.out.println("Lembre-se de salvar as alteracoes no menu.");
            } else {
                System.out.println("Produto com ID " + idProduto + " nao encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID ou quantidade invalida.");
        }
    }

    /**
     * Busca um produto pelo seu ID e exibe todos os seus detalhes na tela.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void buscarEExibirProduto(Scanner scanner) {
        System.out.print("\nDigite o ID do produto que deseja buscar: ");
        try {
            int idProduto = Integer.parseInt(scanner.nextLine());
            Produto produto = buscarProdutoPorId(idProduto);
            if (produto != null) {
                System.out.println("--- Detalhes do Produto ---");
                System.out.println("ID: " + produto.getIdProduto());
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Descricao: " + produto.getDescricao());
                System.out.println("Preco de Custo: " + String.format("R$%.2f", produto.getPrecoCusto()));
                System.out.println("Preco de Venda: " + String.format("R$%.2f", produto.getPrecoVenda()));
                System.out.println("Quantidade em Estoque: " + produto.getQuantidadeEstoque());
                System.out.println("---------------------------");
            } else {
                System.out.println("Produto com ID " + idProduto + " nao encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
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
        System.out.print("Deseja salvar todas as alteracoes pendentes no arquivo estoque.json? (S/N): ");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            salvarEstoque();
            System.out.println("Alteracoes salvas com sucesso!");
        } else {
            System.out.println("Operacao cancelada. As alteracoes continuam apenas em memoria.");
        }
    }

   /**
    * Métodos Auxiliares:
     * Exibe no console uma lista de todos os produtos atualmente em estoque.
     */
    public void listarProdutos() {
        System.out.println("\n--- Lista de Produtos em Estoque ---");
        if (this.listaProdutos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado no estoque.");
            return;
        }
        for (Produto produto : this.listaProdutos) {
            System.out.println(produto);
        }
        System.out.println("------------------------------------");
    }

    /**
     * Busca um produto na lista em memoria pelo seu ID unico.
     * @param idProduto O ID do produto a ser procurado.
     * @return O objeto {@code Produto} se encontrado, ou {@code null}.
     */
    public Produto buscarProdutoPorId(int idProduto) {
        for (Produto produto : this.listaProdutos) {
            if (produto.getIdProduto() == idProduto) {
                return produto;
            }
        }
        return null;
    }

    /**
     * Gera um novo ID sequencial para um novo produto.
     * @return O proximo ID inteiro disponivel.
     */
    private int gerarProximoIdProduto() {
        if (listaProdutos.isEmpty()) {
            return 1;
        }
        return listaProdutos.stream()
                .mapToInt(Produto::getIdProduto)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Persiste a lista atual de produtos no arquivo estoque.json.
     */
    public void salvarEstoque() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_ESTOQUE_JSON)) {
            gson.toJson(this.listaProdutos, writer);
            this.dadosForamModificados = false; // Reseta a flag apos salvar
        } catch (IOException e) {
            System.err.println("Erro ao salvar o estoque: " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de produtos a partir do arquivo estoque.json.
     * @return Uma {@code List<Produto>} com os dados carregados ou uma lista vazia.
     */
    private List<Produto> carregarEstoque() {
        try (Reader reader = new FileReader(ARQUIVO_ESTOQUE_JSON)) {
            Gson gson = new Gson();
            List<Produto> produtos = gson.fromJson(reader, new TypeToken<List<Produto>>(){}.getType());
            System.out.println("GerenciadorEstoque: " + (produtos != null ? produtos.size() : 0) + " produtos carregados.");
            return produtos != null ? produtos : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorEstoque: Arquivo 'estoque.json' nao encontrado. Iniciando com estoque vazio.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar o estoque: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}