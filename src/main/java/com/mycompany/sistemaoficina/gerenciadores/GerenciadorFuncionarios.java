package com.mycompany.sistemaoficina.gerenciadores; 

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.AuthService; 
import com.mycompany.sistemaoficina.Funcionario; 

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsavel por gerenciar todas as operacoes relacionadas a Funcionarios.
 * Inclui carregamento, salvamento e operacoes CRUD (Criar, Ler, Atualizar, Excluir).
 * @author santo
 */
public class GerenciadorFuncionarios {

    // Caminho do arquivo JSON para funcionarios
    private static final String ARQUIVO_FUNCIONARIOS_JSON = "funcionarios.json";

    // Lista de funcionarios gerenciada por esta classe
    private List<Funcionario> listaFuncionarios;

    /**
     * Construtor do GerenciadorFuncionarios.
     * Carrega os dados dos funcionarios do arquivo JSON ao ser instanciado.
     */
    public GerenciadorFuncionarios() {
        this.listaFuncionarios = carregarDadosFuncionarios();
    }

    /**
     * Retorna a lista de funcionarios.
     * @return A lista de funcionarios em memoria.
     */
    public List<Funcionario> getListaFuncionarios() {
        return listaFuncionarios;
    }

    /**
     * Exibe o submenu para gerenciar os funcionarios, permitindo o acesso
     * as funcionalidades de cadastro, edicao, exclusao e listagem.
     */
    public void gerenciarFuncionarios() { // Metodo publico para ser chamado pela SistemaOficina
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== GERENCIAR FUNCIONARIOS ===");
            System.out.println("1. Cadastrar Funcionario");
            System.out.println("2. Editar Funcionario");
            System.out.println("3. Excluir Funcionario");
            System.out.println("4. Listar Funcionarios");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarFuncionario(scanner); 
                    break;
                case 2:
                    editarFuncionario(scanner);
                    break;
                case 3:
                    excluirFuncionario(scanner);
                    break;
                case 4:
                    listarFuncionarios();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    /**
     * Conduz o fluxo de trabalho para cadastrar um novo funcionario no sistema.
     * Pede ao usuario os dados, gera um ID unico e pergunta se deseja salvar.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void cadastrarFuncionario(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();
        
        // Delega a geracao do ID para o AuthService para manter a logica centralizada.
        int novoId = AuthService.gerarIdUnicoFuncionario(this.listaFuncionarios); 
        
        Funcionario novoFunc = new Funcionario(novoId, nome, cpf, senha); 
        
        this.listaFuncionarios.add(novoFunc); 
        System.out.println("Funcionario '" + novoFunc.getNome() + "' (ID: " + novoFunc.getId() + ") cadastrado em memoria.");

        System.out.print("Deseja salvar este funcionario no arquivo? (S/N): ");
        String confirmarSalvar = scanner.nextLine().trim();
        
        if (confirmarSalvar.equalsIgnoreCase("S")) {
            salvarDadosFuncionarios(); 
            System.out.println("Funcionario salvo com sucesso no arquivo!");
        } else {
            System.out.println("Funcionario nao salvo no arquivo. Ele permanecera na sessao atual e sera perdido ao sair.");
        }
    }

    /**
     * Conduz o fluxo de trabalho para editar um funcionario existente.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void editarFuncionario(Scanner scanner) {
        if (listaFuncionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado para editar.");
            return;
        }

        listarFuncionarios();

        System.out.print("\nDigite o ID (inteiro) do funcionario a editar: ");
        int idEditar = scanner.nextInt();
        scanner.nextLine();

        Funcionario funcionarioParaEditar = buscarFuncionarioPorId(idEditar);

        if (funcionarioParaEditar == null) {
            System.out.println("Funcionario com o ID " + idEditar + " nao encontrado.");
            return;
        }

        System.out.println("\nEditando Funcionario: " + funcionarioParaEditar.getNome());
        System.out.print("Novo nome (atual: " + funcionarioParaEditar.getNome() + ", deixe em branco para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            funcionarioParaEditar.setNome(novoNome);
        }

        System.out.print("Novo CPF (atual: " + funcionarioParaEditar.getCpf() + ", deixe em branco para manter): ");
        String novoCpf = scanner.nextLine();
        if (!novoCpf.isEmpty()) {
            funcionarioParaEditar.setCpf(novoCpf);
        }

        System.out.print("Nova senha (deixe em branco para manter): ");
        String novaSenha = scanner.nextLine();
        if (!novaSenha.isEmpty()) {
            funcionarioParaEditar.setSenha(novaSenha);
        }
        
        salvarDadosFuncionarios(); // Salva apos edicao
        System.out.println("Funcionario editado com sucesso e salvo no arquivo!");
    }

     /**
     * Conduz o fluxo de trabalho para excluir um funcionario do sistema.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void excluirFuncionario(Scanner scanner) {
        if (listaFuncionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado para excluir.");
            return;
        }

        listarFuncionarios();

        System.out.print("\nDigite o ID (inteiro) do funcionario a excluir: ");
        int idExcluir = scanner.nextInt();
        scanner.nextLine();

        Funcionario funcionarioParaRemover = buscarFuncionarioPorId(idExcluir);

        if (funcionarioParaRemover == null) {
            System.out.println("Funcionario com o ID " + idExcluir + " nao encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir " + funcionarioParaRemover.getNome() + "? (S/N): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            listaFuncionarios.remove(funcionarioParaRemover); 
            salvarDadosFuncionarios(); // Salva apos exclusao
            System.out.println("Funcionario " + funcionarioParaRemover.getNome() + " excluido com sucesso e salvo no arquivo!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    /**
     * Exibe a lista de funcionarios cadastrados com seus respectivos IDs.
     */
    public void listarFuncionarios() { 
        if (listaFuncionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado.");
            return;
        }

        System.out.println("\n=== LISTA DE FUNCIONARIOS ===");
        for (Funcionario f : listaFuncionarios) {
            System.out.println("ID: " + f.getId() + " | Nome: " + f.getNome() + " | CPF: " + f.getCpf());
        }
    }

    /**
     * Busca um funcionario na lista em memoria pelo seu ID unico.
     * @param idFuncionario O ID do funcionario a ser procurado.
     * @return O objeto {@code Funcionario} se encontrado, ou {@code null}.
     */
    public Funcionario buscarFuncionarioPorId(int idFuncionario) { // Metodo publico para ser chamado de fora
        for (Funcionario f : listaFuncionarios) {
            if (f.getId() == idFuncionario) {
                return f;
            }
        }
        return null;
    }

    /**
     * Carrega a lista de funcionarios a partir do arquivo JSON.
     * @return Uma {@code List<Funcionario>} com os dados carregados ou uma lista vazia.
     */
    private List<Funcionario> carregarDadosFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_FUNCIONARIOS_JSON))) {
            Gson gson = new Gson();
            funcionarios = gson.fromJson(reader, new TypeToken<List<Funcionario>>(){}.getType());
            if (funcionarios == null) {
                funcionarios = new ArrayList<>();
            }
            System.out.println("GerenciadorFuncionarios: Funcionarios carregados. Total: " + funcionarios.size()); 
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorFuncionarios: Arquivo '" + ARQUIVO_FUNCIONARIOS_JSON + "' nao encontrado. Criando lista de funcionarios vazia.");
            funcionarios = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("GerenciadorFuncionarios: Erro ao carregar dados de funcionarios: " + e.getMessage());
        }
        return funcionarios;
    }

    /**
     * Persiste a lista atual de funcionarios no arquivo funcionarios.json.
     */
    public void salvarDadosFuncionarios() { 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_FUNCIONARIOS_JSON))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(listaFuncionarios, writer);
            System.out.println("GerenciadorFuncionarios: Funcionarios salvos com sucesso. Total: " + listaFuncionarios.size()); 
        } catch (IOException e) {
            System.err.println("GerenciadorFuncionarios: Erro ao salvar dados de funcionarios: " + e.getMessage());
        }
    }
}

