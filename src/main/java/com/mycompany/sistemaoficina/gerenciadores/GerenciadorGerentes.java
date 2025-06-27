package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.sistemaoficina.AuthService;
import com.mycompany.sistemaoficina.Gerente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsavel por gerenciar todas as operacoes relacionadas a Gerentes.
 * Inclui carregamento, salvamento e operacoes como Editar senha e o cadastro exigindo nome e CPF.
 * @author santo
 */
public class GerenciadorGerentes {

    // Lista de gerentes gerenciada em memoria por esta classe.
    private List<Gerente> listaGerentes;
    // Caminho do arquivo JSON para persistencia dos dados dos gerentes. 
    private static final String ARQUIVO_GERENTES_JSON = "gerentes.json";

     /**
     * Construtor do GerenciadorGerentes.
     * Carrega os dados dos gerentes do arquivo JSON ao ser instanciado.
     */
    public GerenciadorGerentes() {
        this.listaGerentes = carregarDadosGerentes();
    }

    /**
     * Retorna a lista de gerentes atualmente em memoria.
     * @return A lista de objetos Gerente.
     */
    public List<Gerente> getListaGerentes() {
        return this.listaGerentes;
    }

    /**
     * Exibe um menu para o gerente logado gerenciar seus proprios dados,
     * com foco na alteracao segura da senha.
     * @param gerenteLogado O objeto do gerente que esta a usar o sistema.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void gerenciarMeusDadosGerente(Gerente gerenteLogado, Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== MEUS DADOS (GERENTE) ===");
            System.out.println("1. Alterar Minha Senha");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Opcao invalida.");
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    // A logica de seguranca para alterar a senha e delegada para o AuthService.
                    boolean senhaFoiAlterada = AuthService.alterarSenhaGerente(gerenteLogado, scanner);
                    
                    // Se a alteracao foi bem-sucedida, o gerenciador salva o estado atualizado no arquivo.
                    if (senhaFoiAlterada) {
                        salvarDadosGerentes();
                        System.out.println("Confirmacao: A sua nova senha foi salva com seguranca.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }
    
   /**
     * Cadastra o primeiro gerente no sistema, caso nao exista nenhum.
     * Este metodo e essencial para a inicializacao do sistema.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void cadastrarGerenteInicial(Scanner scanner) {
        if (!listaGerentes.isEmpty()) {
            return; // A verificacao principal deve ser feita antes de chamar este metodo.
        }
        System.out.println("--- Cadastro do Primeiro Gerente ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Crie uma senha: ");
        String senha = scanner.nextLine().trim();
        
        int novoId = AuthService.gerarIdUnicoGerente(listaGerentes);
        Gerente novoGerente = new Gerente(novoId, nome, cpf, senha);
        
        this.listaGerentes.add(novoGerente);
        salvarDadosGerentes();
        System.out.println("Gerente inicial cadastrado com sucesso!");
    }

    /**
     * Persiste a lista atual de gerentes no arquivo gerentes.json.
     * Utiliza a biblioteca Gson para serializar a lista de objetos.
     */
    private void salvarDadosGerentes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_GERENTES_JSON)) {
            gson.toJson(this.listaGerentes, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos gerentes: " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de gerentes a partir do arquivo JSON.
     * Se o arquivo nao for encontrado, inicializa uma lista vazia.
     * @return Uma {@code List<Gerente>} com os dados carregados ou uma lista vazia.
     */
    private List<Gerente> carregarDadosGerentes() {
        try (Reader reader = new FileReader(ARQUIVO_GERENTES_JSON)) {
            List<Gerente> gerentes = new Gson().fromJson(reader, new TypeToken<List<Gerente>>(){}.getType());
            System.out.println("GerenciadorGerentes: Gerentes carregados. Total: " + (gerentes != null ? gerentes.size() : 0));
            return gerentes != null ? gerentes : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorGerentes: Arquivo 'gerentes.json' nao encontrado.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados dos gerentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
