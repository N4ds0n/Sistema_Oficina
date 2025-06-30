package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.Clientes;
import com.mycompany.sistemaoficina.Veiculo; // Importe a classe Veiculo

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsável por gerenciar TODAS as operações relacionadas a Clientes.
 * Centraliza a lógica de negócios, a interação com o usuário (UI) e a persistência de dados (JSON).
 *
 * @author santo (Refatorado por Gemini)
 */
public class GerenciadorClientes {

    private static final String ARQUIVO_CLIENTES_JSON = "clientes.json";
    private final List<Clientes> listaClientes;
    private final Scanner scanner; // Instância única do Scanner para toda a classe
    private final Gson gson;       // Instância única do Gson para performance

    /**
     * Construtor que inicializa o gerenciador.
     * Instancia o Scanner, o Gson e carrega os clientes do arquivo JSON.
     */
    public GerenciadorClientes() {
        this.scanner = new Scanner(System.in);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.listaClientes = carregarDadosDoArquivo();
    }

    // --- PONTO DE ENTRADA E MENU PRINCIPAL ---

    /**
     * Exibe o menu principal de gerenciamento de clientes e controla o fluxo de navegação.
     * Este é o único método que deve ser chamado de fora para iniciar a interação.
     */
    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n===== GERENCIAR CLIENTES =====");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Editar Cliente Existente");
            System.out.println("3. Excluir Cliente");
            System.out.println("4. Listar Clientes (com opcoes de ordenacao)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Opcao invalida. Por favor, digite um numero.");
                opcao = -1; // Força a repetição do loop
            }

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> editarCliente();
                case 3 -> excluirCliente();
                case 4 -> listarClientesComOrdenacao();
                case 0 -> System.out.println("Retornando ao menu principal...");
                default -> System.out.println("Opção invalida!");
            }
        } while (opcao != 0);
    }

    /**
     * LÓGICA DE CRUD
     * Conduz todo o fluxo de cadastro de um novo cliente, incluindo seus dados e veículo inicial.
     * Toda a interação com o usuário está contida aqui.
     */
    private void cadastrarCliente() {
        System.out.println("\n--- Cadastro de Novo Cliente ---");
        int novoId = gerarProximoId();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine(); // Futuramente, você pode adicionar a lógica de anonimização aqui

        // Cria o objeto de modelo (POJO) com os dados coletados
        Clientes novoCliente = new Clientes(novoId, nome, endereco, telefone, email, "XXX." + cpf.substring(4, 7) + ".XXX-XX");
        
        System.out.println("\n--- Cadastro de Veiculo Inicial ---");
        Veiculo novoVeiculo = pedirDadosVeiculo();
        novoCliente.adicionarVeiculo(novoVeiculo); // Usa o método do modelo para adicionar

        this.listaClientes.add(novoCliente);
        salvarDadosNoArquivo();
        System.out.println("\nCliente '" + novoCliente.getNome() + "' cadastrado e salvo com sucesso!");
    }

    /**
     * Conduz o fluxo para editar um cliente selecionado pelo usuário.
     */
    private void editarCliente() {
        System.out.println("\n--- Edicao de Cliente ---");
        Clientes clienteParaEditar = selecionarClientePeloId();
        if (clienteParaEditar == null) {
            return; // Mensagem de erro já foi exibida em selecionarClientePeloId
        }

        System.out.println("Deixe o campo em branco e pressione Enter para manter o valor atual.");

        System.out.print("Novo nome (" + clienteParaEditar.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) clienteParaEditar.setNome(novoNome);

        System.out.print("Novo endereco (" + clienteParaEditar.getEndereco() + "): ");
        String novoEndereco = scanner.nextLine();
        if (!novoEndereco.trim().isEmpty()) clienteParaEditar.setEndereco(novoEndereco);
        
        System.out.print("Novo telefone (" + clienteParaEditar.getTelefone() + "): ");
        String novoTelefone = scanner.nextLine();
        if (!novoTelefone.trim().isEmpty()) clienteParaEditar.setTelefone(novoTelefone);

        System.out.print("Novo email (" + clienteParaEditar.getEmail() + "): ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.trim().isEmpty()) clienteParaEditar.setEmail(novoEmail);

        System.out.print("Deseja gerenciar os veiculos deste cliente? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            gerenciarVeiculosDoCliente(clienteParaEditar);
        }

        salvarDadosNoArquivo();
        System.out.println("\nCliente atualizado com sucesso!");
    }

    /**
     * Conduz o fluxo para excluir um cliente selecionado pelo usuário.
     */
    private void excluirCliente() {
        System.out.println("\n--- Exclusao de Cliente ---");
        Clientes clienteParaExcluir = selecionarClientePeloId();
        if (clienteParaExcluir == null) {
            return;
        }

        System.out.print("Tem certeza que deseja excluir o cliente '" + clienteParaExcluir.getNome() + "' (ID: " + clienteParaExcluir.getId() + ")? Esta acao nao pode ser desfeita. (s/n): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("s")) {
            this.listaClientes.remove(clienteParaExcluir);
            salvarDadosNoArquivo();
            System.out.println("Cliente excluido com sucesso.");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    // --- MÉTODOS DE LISTAGEM E ORDENAÇÃO ---

    /**
     * Apresenta um submenu para o usuário escolher como ordenar a lista de clientes
     * antes de exibi-la.
     */
    private void listarClientesComOrdenacao() {
        if (listaClientes.isEmpty()) {
            System.out.println("\nNenhum cliente cadastrado para listar.");
            return;
        }

        System.out.println("\n--- Opcoes de Ordenacao ---");
        System.out.println("1. Ordenar por Nome (A-Z)");
        System.out.println("2. Ordenar por ID (Crescente)");
        System.out.print("Escolha uma opcao: ");

        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            String titulo = "\n===== LISTA DE CLIENTES =====";
            switch (escolha) {
                case 1 -> {
                    Collections.sort(this.listaClientes); // Ordenação natural (por nome)
                    titulo = "\n===== LISTA DE CLIENTES (Ordenada por Nome) =====";
                }
                case 2 -> {
                    this.listaClientes.sort(new Clientes.ClientePorIdComparator()); // Ordenação por ID
                    titulo = "\n===== LISTA DE CLIENTES (Ordenada por ID) =====";
                }
                default -> System.out.println("Opcao invalida. Listando na ordem atual.");
            }
            System.out.println(titulo);
        } catch (NumberFormatException e) {
            System.out.println("Opcao invalida. Listando na ordem atual.");
        }

        // Imprime a lista (agora ordenada)
        for (Clientes c : this.listaClientes) {
            // Usando String.format para um alinhamento limpo
            System.out.printf("ID: %-4d | Nome: %-30s | Veiculos: %d\n", c.getId(), c.getNome(), c.getVeiculos().size());
        }
    }


    // --- MÉTODOS AUXILIARES (LÓGICA INTERNA) ---

    /**
     * Reutiliza a lógica de pedir ao usuário para selecionar um cliente por ID.
     * @return O objeto Cliente encontrado ou null se não for encontrado/inválido.
     */
    private Clientes selecionarClientePeloId() {
        if (listaClientes.isEmpty()) {
            System.out.println("\nNao ha clientes cadastrados.");
            return null;
        }

        System.out.println("\n--- Clientes Disponiveis ---");
        for (Clientes c : this.listaClientes) {
            System.out.printf("ID: %-4d | Nome: %s\n", c.getId(), c.getNome());
        }
        
        System.out.print("\nDigite o ID do cliente desejado: ");
        try {
            int idBusca = Integer.parseInt(scanner.nextLine());
            for (Clientes cliente : this.listaClientes) {
                if (cliente.getId() == idBusca) {
                    return cliente;
                }
            }
            System.out.println("Erro: Cliente com ID " + idBusca + " nao encontrado.");
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido. Por favor, digite um numero.");
            return null;
        }
    }
    
    /**
     * Gerencia a UI para adicionar ou editar veículos de um cliente.
     */
    private void gerenciarVeiculosDoCliente(Clientes cliente) {
        // Lógica para listar, adicionar, editar veículos pode ser implementada aqui
        System.out.println("Funcionalidade de gerenciamento de veículos ainda não implementada.");
    }

    /**
     * Pede ao usuário os dados de um único veículo.
     * @return Um novo objeto Veiculo preenchido.
     */
    private Veiculo pedirDadosVeiculo() {
        System.out.print("Modelo do veiculo: ");
        String modelo = scanner.nextLine();
        System.out.print("Placa do veiculo: ");
        String placa = scanner.nextLine();
        System.out.print("Cor do veiculo: ");
        String cor = scanner.nextLine();
        System.out.print("Ano do veiculo: ");
        int ano = Integer.parseInt(scanner.nextLine());
        return new Veiculo(modelo, placa, cor, ano);
    }
    
    /**
     * Gera o próximo ID sequencial com base no maior ID existente na lista.
     * @return O próximo ID disponível.
     */
    private int gerarProximoId() {
        // Se a lista estiver vazia, o primeiro ID será 1
        if (listaClientes.isEmpty()) {
            return 1;
        }
        // Encontra o maior ID atual e retorna o próximo número
        return listaClientes.stream()
                .mapToInt(Clientes::getId)
                .max()
                .orElse(0) + 1;
    }
    
     /**
     * Retorna a lista completa de clientes.
     * Necessário para outras classes do sistema, como Testes e Agendamentos.
     * @return Uma lista de objetos Clientes.
     */
    public List<Clientes> getListaClientes() {
        return this.listaClientes;
    }

    /**
     * Busca e retorna um cliente pelo seu ID.
     * Método público para ser usado por outras partes do sistema.
     * @param idBusca O ID do cliente a ser procurado.
     * @return O objeto Cliente encontrado, ou null se não existir.
     */
    public Clientes buscarClientePorId(int idBusca) {
        for (Clientes cliente : this.listaClientes) {
            if (cliente.getId() == idBusca) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Exibe no console uma lista simples de todos os clientes.
     * Útil para outras classes que precisam apenas mostrar os clientes disponíveis.
     */
    public void listarClientes() {
        System.out.println("\n--- Clientes Cadastrados ---");
        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente na lista.");
            return;
        }
        for (Clientes c : listaClientes) {
            System.out.printf("ID: %-4d | Nome: %s\n", c.getId(), c.getNome());
        }
    }

    /**
     * Exibe no console a lista de veículos de um cliente específico.
     * @param cliente O cliente cujos veículos serão exibidos.
     */
    public void exibirVeiculosDoCliente(Clientes cliente) {
        if (cliente.getVeiculos().isEmpty()) {
            System.out.println("Nenhum veiculo cadastrado para: " + cliente.getNome());
            return;
        }
        System.out.println("--- Veiculos de " + cliente.getNome() + ": ---");
        for (int i = 0; i < cliente.getVeiculos().size(); i++) {
            System.out.println("  Indice " + i + ": " + cliente.getVeiculos().get(i));
        }
    }

    /**
     * Método de busca genérico e estático.
     * Necessário para a classe de testes (TestesSistema).
     * @param lista A lista onde a busca será realizada.
     * @param chaveDeBusca O objeto com o critério de busca.
     * @param comparator O comparador que define como a busca será feita.
     * @return O objeto Cliente encontrado, ou null.
     */
    public static Clientes find(List<Clientes> lista, Clientes chaveDeBusca, java.util.Comparator<Clientes> comparator) {
        for (Clientes clienteAtual : lista) {
            if (comparator.compare(clienteAtual, chaveDeBusca) == 0) {
                return clienteAtual;
            }
        }
        return null;
    }

    // --- PERSISTÊNCIA DE DADOS (JSON) ---

    /**
     * Carrega a lista de clientes a partir do arquivo JSON.
     * @return Uma lista de clientes preenchida ou uma lista vazia se o arquivo não existir.
     */
    private List<Clientes> carregarDadosDoArquivo() {
        File arquivo = new File(ARQUIVO_CLIENTES_JSON);
        if (!arquivo.exists()) {
            System.out.println("GerenciadorClientes: Arquivo de dados nao encontrado. Iniciando com lista vazia.");
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            List<Clientes> clientes = gson.fromJson(reader, new TypeToken<List<Clientes>>() {}.getType());
            System.out.println("GerenciadorClientes: Dados carregados. Total: " + (clientes != null ? clientes.size() : 0) + " clientes.");
            return clientes != null ? clientes : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("GerenciadorClientes: Erro critico ao carregar dados: " + e.getMessage());
            return new ArrayList<>(); // Retorna lista vazia em caso de erro
        }
    }

    /**
     * Salva a lista de clientes em arquivo Json
     */
    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CLIENTES_JSON))) {
            gson.toJson(this.listaClientes, writer);
        } catch (IOException e) {
            System.err.println("GerenciadorClientes: Erro critico ao salvar dados: " + e.getMessage());
        }
    }
}