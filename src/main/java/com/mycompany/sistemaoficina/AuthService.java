package com.mycompany.sistemaoficina;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Classe responsavel pela autenticacao e geracao de IDs para Gerentes e Funcionarios.
 */
public class AuthService {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * Conduz o fluxo de trabalho seguro para um gerente alterar a propria senha.
     * Pede a senha atual para verificacao antes de permitir a alteracao.
     * @param gerenteLogado O objeto do gerente que esta a alterar a senha.
     * @param scanner Scanner para ler a entrada do usuario.
     * @return true se a senha foi alterada com sucesso, false caso contrario.
     */
    public static boolean alterarSenhaGerente(Gerente gerenteLogado, Scanner scanner) {
        System.out.println("\n--- Alteracao de Senha ---");
        System.out.print("Para seguranca, digite sua senha ATUAL: ");
        String senhaAtualDigitada = scanner.nextLine();

        // 1. Verifica se a senha atual fornecida esta correta
        if (!gerenteLogado.getSenha().equals(senhaAtualDigitada)) {
            System.out.println("Erro: Senha atual incorreta. A operacao foi cancelada.");
            return false;
        }

        // 2. Pede a nova senha
        System.out.print("Digite a NOVA senha: ");
        String novaSenha = scanner.nextLine();

        // 3. Pede a confirmacao da nova senha
        System.out.print("Confirme a NOVA senha: ");
        String confirmacaoNovaSenha = scanner.nextLine();

        // 4. Verifica se a nova senha e a confirmacao sao iguais
        if (!novaSenha.equals(confirmacaoNovaSenha)) {
            System.out.println("Erro: As novas senhas nao coincidem. A operacao foi cancelada.");
            return false;
        }
        
        // 5. Verifica se a nova senha nao esta em branco
        if (novaSenha.isBlank()) {
            System.out.println("Erro: A nova senha nao pode estar em branco.");
            return false;
        }

        // Se todas as verificacoes passaram, atualiza a senha no objeto
        gerenteLogado.setSenha(novaSenha);
        System.out.println("Senha alterada com sucesso! Lembre-se de salvar os dados.");
        return true;
    }
    
    /**
     * Verifica se ha algum gerente cadastrado.
     * Para esta verificacao inicial, ainda precisamos carregar a lista internamente.
     * Idealmente, isso seria feito por SistemaOficina ou Main.
     */
    public static boolean existeGerenteCadastrado() {
        List<Gerente> gerentes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("gerentes.json"))) { // Caminho direto aqui
            gerentes = gson.fromJson(reader, new TypeToken<List<Gerente>>(){}.getType());
            if (gerentes == null) {
                gerentes = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            // Arquivo nao encontrado, significa que nao ha gerentes. Nao e um erro.
        } catch (IOException e) {
            System.err.println("AuthService: Erro ao carregar dados de gerentes para verificacao: " + e.getMessage());
        }
        System.out.println("AuthService: Existe gerente cadastrado? " + !gerentes.isEmpty()); // DEBUG
        return !gerentes.isEmpty();
    }

    /**
     * Autentica um gerente.
     * Recebe a lista de gerentes como parametro.
     * @param cpf CPF do gerente.
     * @param senha Senha do gerente.
     * @param listaGerentes A lista atual de gerentes em memoria.
     * @return O objeto Gerente se a autenticacao for bem-sucedida, ou null.
     */
    public static Gerente autenticarGerente(String cpf, String senha, List<Gerente> listaGerentes) {
        for (Gerente g : listaGerentes) {
            if (g.getCpf().equals(cpf) && g.getSenha().equals(senha)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Autentica um funcionario.
     * Recebe a lista de funcionarios como parametro.
     * @param cpf CPF do funcionario.
     * @param senha Senha do funcionario.
     * @param listaFuncionarios A lista atual de funcionarios em memoria.
     * @return O objeto Funcionario se a autenticacao for bem-sucedida, ou null.
     */
    public static Funcionario autenticarFuncionario(String cpf, String senha, List<Funcionario> listaFuncionarios) {
        for (Funcionario f : listaFuncionarios) {
            if (f.getCpf().equals(cpf) && f.getSenha().equals(senha)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Gera um ID unico para um novo funcionario baseado na lista de funcionarios existente.
     * Esta lista e fornecida pela SistemaOficina.
     * @param funcionariosExistentes A lista atual de funcionarios em memoria.
     * @return O proximo ID disponivel.
     */
    public static int gerarIdUnicoFuncionario(List<Funcionario> funcionariosExistentes) {
        int maxId = 0;
        if (funcionariosExistentes != null) {
            for (Funcionario f : funcionariosExistentes) {
                if (f.getId() > maxId) {
                    maxId = f.getId();
                }
            }
        }
        System.out.println("AuthService: Gerando ID para funcionario. Max ID atual: " + maxId + ", Novo ID: " + (maxId + 1)); // DEBUG
        return maxId + 1;
    }
    
    /**
     * Gera um ID unico para um novo gerente baseado na lista de gerentes existente.
     * Esta lista e fornecida pela Main (para o primeiro cadastro) ou SistemaOficina.
     * @param gerentesExistentes A lista atual de gerentes em memoria.
     * @return O proximo ID disponivel.
     */
    public static int gerarIdUnicoGerente(List<Gerente> gerentesExistentes) {
        int maxId = 0;
        if (gerentesExistentes != null) {
            for (Gerente g : gerentesExistentes) {
                if (g.getId() > maxId) {
                    maxId = g.getId();
                }
            }
        }
        System.out.println("AuthService: Gerando ID para gerente. Max ID atual: " + maxId + ", Novo ID: " + (maxId + 1)); // DEBUG
        return maxId + 1;
    }
}
