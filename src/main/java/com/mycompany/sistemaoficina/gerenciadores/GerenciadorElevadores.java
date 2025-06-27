package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.sistemaoficina.Agendamento;
import com.mycompany.sistemaoficina.Elevador;

import java.io.*;
import java.util.Scanner;

/**
 * Classe responsavel por gerenciar os elevadores da oficina.
 * Armazena e controla o estado dos 3 elevadores fixos do sistema,
 * com persistencia de dados em JSON.
 * @author santo
 */
public class GerenciadorElevadores {

    private Elevador[] elevadores;
    private static final String ARQUIVO_ELEVADORES_JSON = "elevadores.json";

    /**
     * Construtor do GerenciadorElevadores.
     * Carrega os dados dos elevadores do arquivo JSON ao ser instanciado.
     */
    public GerenciadorElevadores() {
        this.elevadores = carregarDadosElevadores();
    }
    
    /**
     * Aloca o primeiro elevador disponivel de um TIPO especifico para um agendamento.
     * Este metodo verifica tanto o tipo do elevador quanto o seu estado de ocupacao.
     * @param agendamento O agendamento que requer um elevador.
     * @param tipoBuscado O tipo de elevador desejado (ex: "Fixo" ou "Corriqueiro").
     * @return true se um elevador do tipo correto foi alocado, false caso contrario.
     */
    public boolean alocarElevadorPorTipo(Agendamento agendamento, String tipoBuscado) {
        for (Elevador elevador : elevadores) {
           // A condicao usa 'contains' para ser mais robusta e evitar problemas com espacos ou caracteres especiais
            if (elevador.getTipo().contains(tipoBuscado) && !elevador.isOcupado()) {
                elevador.setOcupado(true);
                agendamento.setElevadorAlocado(elevador);
                System.out.println("SUCESSO: Elevador " + elevador.getNumero() + " (" + elevador.getTipo() + ") alocado.");
                salvarDadosElevadores(); // Salva o estado imediatamente
                return true;
            }
        }
        // Esta mensagem e exibida se o loop terminar sem encontrar um elevador do tipo correto que esteja livre.
        System.out.println("FALHA: Nenhum elevador do tipo '" + tipoBuscado + "' esta disponivel no momento.");
        return false;
    }

    /**
     * Libera o elevador que esta associado a um agendamento.
     * @param agendamento O agendamento cujo servico foi concluido.
     */
    public void liberarElevadorDoAgendamento(Agendamento agendamento) {
        Elevador elevadorOcupado = agendamento.getElevadorAlocado();
        if (elevadorOcupado != null) {
            elevadorOcupado.setOcupado(false);
            agendamento.setElevadorAlocado(null); // Limpa a referencia no agendamento
            System.out.println("SUCESSO: Elevador " + elevadorOcupado.getNumero() + " foi liberado.");
            salvarDadosElevadores(); // Salva o estado imediatamente
        } else {
            // Nenhuma mensagem e necessaria aqui, pois e normal um servico nao usar elevador.
        }
    }

    /**
     * Forca a liberacao de um elevador especifico pelo seu numero.
     * Este e um metodo administrativo para corrigir estados inconsistentes do sistema.
     * @param numeroElevador O numero do elevador a ser liberado.
     */
    public void forcarLiberacao(int numeroElevador) {
        Elevador elevador = buscarElevadorPorNumero(numeroElevador);
        if (elevador != null && elevador.isOcupado()) {
            elevador.setOcupado(false);
            System.out.println("Elevador " + numeroElevador + " foi FORCADAMENTE liberado.");
            salvarDadosElevadores();
        } else if (elevador != null) {
            System.out.println("Elevador " + numeroElevador + " ja esta livre.");
        } else {
            System.out.println("Elevador " + numeroElevador + " nao encontrado.");
        }
    }

    /**
     * Exibe o menu para gerenciamento administrativo dos elevadores.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void menuAdministrativoElevadores(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== GESTAO ADMINISTRATIVA DE ELEVADORES ===");
            System.out.println("1. Listar Status dos Elevadores");
            System.out.println("2. Forcar Liberacao de Elevador");
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
                    listarElevadores();
                    break;
                case 2:
                    listarElevadores();
                    System.out.print("Digite o numero do elevador para FORCAR A LIBERACAO: ");
                    try {
                        int numLiberar = Integer.parseInt(scanner.nextLine());
                        forcarLiberacao(numLiberar);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Numero invalido.");
                    }
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    /**
     * Exibe no console o status atual de todos os elevadores.
     */
    public void listarElevadores() {
        System.out.println("\n=== STATUS DOS ELEVADORES ===");
        if (elevadores == null || elevadores.length == 0) {
            System.out.println("Nenhum elevador configurado.");
            return;
        }
        for (Elevador elevador : elevadores) {
            System.out.println(elevador.toString());
        }
        System.out.println("=============================");
    }

     /**
     * Busca um elevador no array pelo seu numero identificador.
     * @param numeroElevador O numero do elevador.
     * @return O objeto {@code Elevador} se encontrado, ou {@code null} caso contrario.
     */
    public Elevador buscarElevadorPorNumero(int numeroElevador) {
        if (elevadores == null) return null;
        for (Elevador elevador : elevadores) {
            if (elevador.getNumero() == numeroElevador) {
                return elevador;
            }
        }
        return null;
    }

   /**
     * Persiste o estado atual do array de elevadores no arquivo JSON.
     * Este metodo e chamado sempre que o estado de um elevador e modificado.
     */
    private void salvarDadosElevadores() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(ARQUIVO_ELEVADORES_JSON)) {
            gson.toJson(this.elevadores, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o estado dos elevadores: " + e.getMessage());
        }
    }

    /**
     * Carrega o estado dos elevadores a partir do arquivo JSON.
     * Se o arquivo nao existir, chama o metodo para criar os elevadores padrao.
     * @return Um array de {@code Elevador} com o estado carregado ou padrao.
     */
    private Elevador[] carregarDadosElevadores() {
        try (Reader reader = new FileReader(ARQUIVO_ELEVADORES_JSON)) {
            Gson gson = new Gson();
            Elevador[] elevadoresCarregados = gson.fromJson(reader, Elevador[].class);
            if (elevadoresCarregados == null) {
                 return criarElevadoresPadrao();
            }
            System.out.println("GerenciadorElevadores: Estado dos elevadores carregado do arquivo.");
            return elevadoresCarregados;
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorElevadores: Arquivo '" + ARQUIVO_ELEVADORES_JSON + "' nao encontrado. Criando elevadores padrao.");
            return criarElevadoresPadrao();
        } catch (IOException e) {
            System.err.println("Erro ao carregar o estado dos elevadores: " + e.getMessage());
            return new Elevador[0];
        }
    }
    
   /**
     * Cria o array de elevadores com os valores padrao.
     * Este metodo e chamado apenas na primeira execucao ou se o arquivo JSON nao for encontrado.
     * @return Um array com os 3 elevadores padrao.
     */
    private Elevador[] criarElevadoresPadrao() {
        Elevador[] elevadoresPadrao = new Elevador[3];
        elevadoresPadrao[0] = new Elevador(1, "Fixo (Alinhamento/Balanceamento)");
        elevadoresPadrao[1] = new Elevador(2, "Corriqueiro");
        elevadoresPadrao[2] = new Elevador(3, "Corriqueiro");
        
        this.elevadores = elevadoresPadrao;
        salvarDadosElevadores();
        
        return elevadoresPadrao;
    }
}
