package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mycompany.sistemaoficina.Despesa;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gerencia o registo e a persistencia das despesas da oficina.
 * @author santo
 */
public class GerenciadorDespesas {

    private List<Despesa> listaDespesas;
    private static final String ARQUIVO_DESPESAS_JSON = "despesas.json";

    /**
     * Adaptador para que a biblioteca Gson saiba como lidar com LocalDateTime.
     */
    private static final TypeAdapter<LocalDateTime> LOCAL_DATE_TIME_ADAPTER = new TypeAdapter<>() {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(formatter));
            }
        }
        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return LocalDateTime.parse(in.nextString(), formatter);
        }
    };
    
 /**
 * Retorna a lista de todas as Despesas.
 * @return A lista de objetos Despesa.
 */
public List<Despesa> getListaDespesas() {
    return this.listaDespesas;
}
    
     /**
     * Construtor. Carrega as despesas do arquivo JSON ao iniciar.
     */
    public GerenciadorDespesas() {
        this.listaDespesas = carregarDadosDespesas();
    }
    
    /**
     * Exibe o menu principal para a gestao de despesas.
     * Permite ao usuario lancar uma nova despesa ou listar todas as existentes.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    public void gerenciarDespesas(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR DESPESAS ===");
            System.out.println("1. Lancar Nova Despesa");
            System.out.println("2. Listar Todas as Despesas");
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
                    lancarNovaDespesa(scanner);
                    break;
                case 2:
                    listarDespesas();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }
    
    /**
     * Conduz o fluxo de trabalho para registar uma nova despesa.
     * Pede ao usuario os detalhes da despesa e a salva no sistema.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void lancarNovaDespesa(Scanner scanner) {
        System.out.println("\n--- Lancar Nova Despesa ---");
        try {
            System.out.print("Descricao da despesa (ex: Conta de Luz): ");
            String descricao = scanner.nextLine();

            System.out.print("Categoria (ex: Contas, Salarios, Fornecedores, Outros): ");
            String categoria = scanner.nextLine();

            System.out.print("Valor da despesa (ex: 350.50): ");
            double valor = Double.parseDouble(scanner.nextLine());

            int novoId = gerarProximoIdDespesa();
            // A data da despesa e o momento atual do lancamento
            Despesa novaDespesa = new Despesa(novoId, descricao, valor, LocalDateTime.now(), categoria);
            
            this.listaDespesas.add(novaDespesa);
            salvarDadosDespesas();
            System.out.println("Despesa lancada e salva com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro de formato. O valor deve ser um numero valido.");
        }
    }
    
    /**
      * Exibe no console uma lista formatada de todas as despesas registradas.
      */
     private void listarDespesas() {
        System.out.println("\n--- LISTA DE DESPESAS LANCADAS ---");
        if (this.listaDespesas.isEmpty()) {
            System.out.println("Nenhuma despesa lancada ate o momento.");
            return;
        }
        for (Despesa despesa : this.listaDespesas) {
            System.out.println(despesa); // Usa o toString() da classe Despesa
        }
        System.out.println("------------------------------------");
    }
     
     /**
      * Gera um novo ID sequencial para uma nova despesa.
      * @return O proximo ID inteiro disponivel.
      */
     private int gerarProximoIdDespesa() {
        if (listaDespesas.isEmpty()) {
            return 1;
        }
        return listaDespesas.stream()
                .mapToInt(Despesa::getIdDespesa)
                .max()
                .orElse(0) + 1;
    }
     
   /**
    * Persiste a lista atual de despesas no arquivo despesas.json.
    */
   private void salvarDadosDespesas() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_ADAPTER)
                .create();
        try (Writer writer = new FileWriter(ARQUIVO_DESPESAS_JSON)) {
            gson.toJson(this.listaDespesas, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar despesas: " + e.getMessage());
        }
    }
   
   /**
     * Carrega a lista de despesas a partir do arquivo despesas.json.
     * Se o arquivo nao for encontrado, inicializa uma lista vazia.
     * @return Uma {@code List<Despesa>} com os dados carregados ou uma lista vazia.
     */
   private List<Despesa> carregarDadosDespesas() {
        try (Reader reader = new FileReader(ARQUIVO_DESPESAS_JSON)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_ADAPTER)
                    .create();
            List<Despesa> despesas = gson.fromJson(reader, new TypeToken<List<Despesa>>(){}.getType());
            System.out.println("GerenciadorDespesas: " + (despesas != null ? despesas.size() : 0) + " despesas carregadas.");
            return despesas != null ? despesas : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorDespesas: Arquivo 'despesas.json' nao encontrado.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar despesas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
    


            
