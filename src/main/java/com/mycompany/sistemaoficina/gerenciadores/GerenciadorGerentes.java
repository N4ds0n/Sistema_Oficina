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
 * Gerencia todas as operacoes relacionadas aos Gerentes do sistema.
 */
public class GerenciadorGerentes {

    private List<Gerente> listaGerentes;
    private static final String ARQUIVO_GERENTES_JSON = "gerentes.json";

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
     * focando na alteracao segura da senha.
     * @param gerenteLogado O objeto do gerente que esta usando o sistema.
     * @param scanner Scanner para entrada do usuario.
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
     * Metodo para cadastrar o primeiro gerente do sistema, caso nao exista nenhum.
     * @param scanner Scanner para ler a entrada do usuario.
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

    private void salvarDadosGerentes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_GERENTES_JSON)) {
            gson.toJson(this.listaGerentes, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos gerentes: " + e.getMessage());
        }
    }

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
