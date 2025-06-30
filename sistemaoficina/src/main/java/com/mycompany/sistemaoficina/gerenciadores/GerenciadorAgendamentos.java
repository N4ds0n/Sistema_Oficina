package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mycompany.sistemaoficina.Agendamento;
import com.mycompany.sistemaoficina.Clientes;
import com.mycompany.sistemaoficina.OrdemDeServico;
import com.mycompany.sistemaoficina.Veiculo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Classe responsavel por gerenciar todas as operacoes relacionadas a Agendamentos.
 * Atua como o "maestro" do fluxo de trabalho, orquestrando a criacao de agendamentos,
 * o inicio e fim dos servicos, e a interacao com os outros gerenciadores.
 */
public class GerenciadorAgendamentos {

    // --- ATRIBUTOS E CONSTANTES ---
    private List<Agendamento> listaAgendamentos;
    private static final String ARQUIVO_AGENDAMENTOS_JSON = "agendamentos.json";
    private final GerenciadorClientes gerenciadorClientes;
    private final GerenciadorFuncionarios gerenciadorFuncionarios;
    private final GerenciadorElevadores gerenciadorElevadores;
    private final GerenciadorOrdensDeServico gerenciadorOS;

    private static final TypeAdapter<LocalDateTime> LOCAL_DATE_TIME_ADAPTER = new TypeAdapter<>() {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) out.nullValue(); else out.value(value.format(formatter));
        }
        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull(); return null;
            }
            return LocalDateTime.parse(in.nextString(), formatter);
        }
    };

    // --- CONSTRUTOR ---
    public GerenciadorAgendamentos(GerenciadorClientes gerenciadorClientes, GerenciadorFuncionarios gerenciadorFuncionarios, GerenciadorElevadores gerenciadorElevadores, GerenciadorOrdensDeServico gerenciadorOS) {
        this.gerenciadorClientes = gerenciadorClientes;
        this.gerenciadorFuncionarios = gerenciadorFuncionarios;
        this.gerenciadorElevadores = gerenciadorElevadores;
        this.gerenciadorOS = gerenciadorOS;
        this.listaAgendamentos = carregarDadosAgendamentos();
    }

    // --- METODO PUBLICO PRINCIPAL (MENU) ---
    public void gerenciarAgendamentos(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== GERENCIAR AGENDAMENTOS E SERVICOS ===");
            System.out.println("1. Realizar Novo Agendamento");
            System.out.println("2. Iniciar Servico e Abrir Ordem de Servico");
            System.out.println("3. Lancar Itens em Ordem de Servico Aberta");
            System.out.println("4. Finalizar Servico e Fechar Ordem de Servico");
            System.out.println("5. Registrar Entrega de Veiculo");
            System.out.println("6. Listar Todos os Agendamentos (Com Ordenacao)");
            System.out.println("7. Cancelar um Agendamento");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um numero valido.");
                opcao = -1;
            }

            switch (opcao) {
                case 1: realizarAgendamento(scanner); break;
                case 2: iniciarServico(scanner); break;
                case 3: gerenciadorOS.menuLancarItens(scanner); break;
                case 4: finalizarServico(scanner); break;
                case 5: registrarEntrega(scanner); break;
                case 6: 
                    listarAgendamentosComOrdenacao(scanner);
                    break;
                case 7: cancelarAgendamento(scanner); break;
                case 0: break;
                default: System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }
    
    // --- METODOS PRIVADOS (FLUXO DE TRABALHO) ---
    
     private void listarAgendamentosComOrdenacao(Scanner scanner) {
        if (listaAgendamentos.isEmpty()) {
            System.out.println("\nNenhum agendamento cadastrado para listar.");
            return;
        }

        System.out.println("\n--- Opcoes de Ordenacao para Agendamentos ---");
        System.out.println("1. Ordenar por Data (Mais proximos primeiro)");
        System.out.println("2. Ordenar por Status (Agrupados)");
        System.out.print("Escolha como deseja ordenar a lista: ");

        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            //Cria uma copia da lista para nao alterar a ordem original permanentemente
            List<Agendamento> listaOrdenada = new ArrayList<>(this.listaAgendamentos);

            switch (escolha) {
                case 1:
                    // Usa a ordenacao natural (por data) definida com "implements Comparable"
                    Collections.sort(listaOrdenada);
                    System.out.println("\n=== LISTA DE AGENDAMENTOS (Ordenada por Data) ===");
                    break;
                case 2:
                    // Usa o Comparator especifico que criamos para ordenar por Status
                    listaOrdenada.sort(new Agendamento.AgendamentoPorStatusComparator());
                    System.out.println("\n=== LISTA DE AGENDAMENTOS (Ordenada por Status) ===");
                    break;
                default:
                    System.out.println("Opcao invalida. Listando na ordem padrao.");
                    System.out.println("\n=== LISTA DE AGENDAMENTOS ===");
                    break;
            }

            // Imprime a lista (agora ordenada)
            for (Agendamento a : listaOrdenada) {
                System.out.println(a);
            }

        } catch (NumberFormatException e) {
            System.out.println("Opcao invalida. Listando na ordem padrao.");
            listarAgendamentos(); // Mostra a lista sem ordenar
        }
    }

    private void realizarAgendamento(Scanner scanner) {
        if (gerenciadorClientes.getListaClientes().isEmpty()) {
            System.out.println("Nao ha clientes cadastrados para realizar um agendamento.");
            return;
        }
        gerenciadorClientes.listarClientes();
        System.out.print("\nDigite o ID do cliente para o agendamento: ");
        int idCliente = Integer.parseInt(scanner.nextLine());
        Clientes clienteSelecionado = gerenciadorClientes.buscarClientePorId(idCliente);

        if (clienteSelecionado == null) {
            System.out.println("Cliente com ID '" + idCliente + "' nao encontrado.");
            return;
        }
        if (clienteSelecionado.getVeiculos().isEmpty()) {
            System.out.println("O cliente selecionado nao possui veiculos cadastrados.");
            return;
        }
        clienteSelecionado.listarVeiculos();
        System.out.print("Digite o INDICE do veiculo do cliente para o agendamento: ");
        int indexVeiculo = Integer.parseInt(scanner.nextLine());

        if (indexVeiculo < 0 || indexVeiculo >= clienteSelecionado.getVeiculos().size()) {
            System.out.println("Indice de veiculo invalido.");
            return;
        }
        Veiculo veiculoSelecionado = clienteSelecionado.getVeiculos().get(indexVeiculo);

        System.out.print("Digite a data e hora do agendamento (formato dd/MM/yyyy HH:mm): ");
        String dataHoraStr = scanner.nextLine();
        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data e hora invalido. Use dd/MM/yyyy HH:mm");
            return;
        }
        System.out.print("Descricao do problema: ");
        String descricaoProblema = scanner.nextLine();
        int idAgendamento = gerarProximoIdAgendamento();
        Agendamento novoAgendamento = new Agendamento(idAgendamento, clienteSelecionado, veiculoSelecionado, dataHora, descricaoProblema);
        
        listaAgendamentos.add(novoAgendamento);
        salvarDadosAgendamentos();
        System.out.println("Agendamento criado e salvo com sucesso! ID: " + novoAgendamento.getIdAgendamento());
    }

    private void iniciarServico(Scanner scanner) {
        System.out.println("\n--- Iniciar Servico e Abrir OS ---");
        listarAgendamentos();
        if (getListaAgendamentos().isEmpty()) return;

        System.out.print("Digite o ID do agendamento para INICIAR o servico: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Agendamento agendamento = buscarAgendamentoPorId(id);

            if (agendamento != null && agendamento.getStatus().equalsIgnoreCase("Agendado")) {
                String statusFinal = "Em Manutencao (Sem Elevador)";
                
                System.out.print("Este servico necessita de um elevador? (S/N): ");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("S")) {
                    System.out.println("Qual tipo de elevador e necessario?");
                    System.out.println("1 - Fixo (Alinhamento/Balanceamento)");
                    System.out.println("2 - Corriqueiro (Servicos Gerais)");
                    System.out.print("Escolha uma opcao: ");
                    String tipoEscolhido = scanner.nextLine();
                    
                    boolean alocado; // Declaracao da variavel
                    if (tipoEscolhido.equals("1")) {
                        alocado = this.gerenciadorElevadores.alocarElevadorPorTipo(agendamento, "Fixo");
                    } else if (tipoEscolhido.equals("2")) {
                        alocado = this.gerenciadorElevadores.alocarElevadorPorTipo(agendamento, "Corriqueiro");
                    } else {
                        System.out.println("Opcao de tipo invalida.");
                        alocado = false; // Define como falso se a opcao for invalida
                    }

                    if(alocado){
                        statusFinal = "Em Manutencao (Com Elevador)";
                    } else {
                        // A mensagem de falha ja e exibida pelo metodo alocarElevadorPorTipo
                        return; // Interrompe a operacao
                    }
                }
                
                agendamento.setStatus(statusFinal);
                gerenciadorOS.criarNovaOS(agendamento);
                salvarDadosAgendamentos();
                System.out.println("Status do agendamento ID " + id + " atualizado para: " + agendamento.getStatus());

            } else {
                System.out.println("Agendamento nao encontrado ou nao esta com o status 'Agendado'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }

    private void finalizarServico(Scanner scanner) {
        System.out.println("\n--- Finalizar Servico e Fechar OS ---");
        listarAgendamentos();
        if (getListaAgendamentos().isEmpty()) return;

        System.out.print("Digite o ID do agendamento para FINALIZAR o servico: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Agendamento agendamento = buscarAgendamentoPorId(id);

            if (agendamento != null && agendamento.getStatus().startsWith("Em Manutencao")) {
                OrdemDeServico os = gerenciadorOS.buscarOSAbertaPorAgendamento(agendamento);
                
                if (os == null) {
                    System.out.println("ERRO GRAVE: Nenhuma Ordem de Servico aberta encontrada para este agendamento.");
                    return;
                }
                
                os.finalizar();
                gerenciadorOS.salvarOrdensDeServico();
                System.out.println("Ordem de Servico #" + os.getIdOrdemDeServico() + " foi finalizada. Valor total: R$" + String.format("%.2f", os.getValorTotal()));

                this.gerenciadorElevadores.liberarElevadorDoAgendamento(agendamento);
                
                agendamento.setStatus("Pronto para Entrega");
                System.out.println("Status do agendamento ID " + id + " atualizado para: " + agendamento.getStatus());
                salvarDadosAgendamentos();
            } else {
                System.out.println("Agendamento nao encontrado ou nao esta 'Em Manutencao'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }

    /**
     * Fluxo de trabalho para REGISTRAR A ENTREGA de um veiculo pronto
     * e opcionalmente emitir a Nota Fiscal.
     */
    private void registrarEntrega(Scanner scanner) {
        System.out.println("\n--- Registrar Entrega de Veiculo ---");
        
        System.out.println("\n--- Veiculos Prontos para Entrega ---");
        List<Agendamento> agendamentosProntos = this.listaAgendamentos.stream()
            .filter(ag -> ag.getStatus().equalsIgnoreCase("Pronto para Entrega"))
            .collect(Collectors.toList());

        if (agendamentosProntos.isEmpty()) {
            System.out.println("Nenhum veiculo esta com o status 'Pronto para Entrega'.");
            return;
        }
        
        agendamentosProntos.forEach(System.out::println);

        System.out.print("\nDigite o ID do agendamento para registrar a ENTREGA: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Agendamento agendamento = buscarAgendamentoPorId(id);

            if (agendamento != null && agendamento.getStatus().equalsIgnoreCase("Pronto para Entrega")) {
                
                // Busca a Ordem de Servico finalizada usando o novo metodo auxiliar
                OrdemDeServico os = gerenciadorOS.buscarOSPorAgendamentoId(agendamento.getIdAgendamento());
                
                if (os == null) {
                    System.out.println("ERRO GRAVE: Nao foi encontrada uma Ordem de Servico para este agendamento.");
                    return;
                }
                
                // Pergunta se o usuario deseja emitir a nota fiscal
                System.out.print("Deseja emitir a Nota Fiscal para esta entrega? (S/N): ");
                String resposta = scanner.nextLine();
                
                if (resposta.equalsIgnoreCase("S")) {
                    // Chama a classe especialista para "imprimir" a nota no console
                    GeradorNotaFiscal.emitirNotaFiscal(os);
                }

                // Finaliza o processo, mudando o status do agendamento
                agendamento.setStatus("Entregue");
                System.out.println("Status do agendamento ID " + id + " atualizado para: " + agendamento.getStatus());
                salvarDadosAgendamentos(); // Salva a mudanca final do status

            } else {
                System.out.println("Agendamento nao encontrado ou nao esta com o status 'Pronto para Entrega'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }

    private void cancelarAgendamento(Scanner scanner) {
        listarAgendamentos();
        if (listaAgendamentos.isEmpty()) return;
        
        System.out.print("\nDigite o ID do agendamento a cancelar: ");
        int idCancelar = Integer.parseInt(scanner.nextLine());
        Agendamento agendamentoParaCancelar = buscarAgendamentoPorId(idCancelar);

        if (agendamentoParaCancelar == null) {
            System.out.println("Agendamento com ID '" + idCancelar + "' nao encontrado.");
            return;
        }

        if (!agendamentoParaCancelar.getStatus().equalsIgnoreCase("Agendado")) {
            System.out.println("Apenas agendamentos com status 'Agendado' podem ser cancelados.");
            return;
        }

        System.out.print("Tem certeza que deseja cancelar o agendamento " + idCancelar + "? (S/N): ");
        String confirmacao = scanner.nextLine();
        if (confirmacao.equalsIgnoreCase("S")) {
            agendamentoParaCancelar.setStatus("Cancelado");
            double valorEstimado = 100.0;
            agendamentoParaCancelar.setValorRetidoCancelamento(valorEstimado * 0.20);
            salvarDadosAgendamentos();
            System.out.println("Agendamento " + idCancelar + " cancelado com sucesso! Valor retido: R$" + String.format("%.2f", agendamentoParaCancelar.getValorRetidoCancelamento()));
        } else {
            System.out.println("Cancelamento de agendamento abortado.");
        }
    }

    private void listarAgendamentos() {
        if (listaAgendamentos.isEmpty()) {
            System.out.println("\nNenhum agendamento cadastrado.");
            return;
        }
        System.out.println("\n=== LISTA DE AGENDAMENTOS ===");
        for (Agendamento a : listaAgendamentos) {
            System.out.println(a);
        }
    }

    public Agendamento buscarAgendamentoPorId(int idAgendamento) {
        for (Agendamento a : listaAgendamentos) {
            if (a.getIdAgendamento() == idAgendamento) {
                return a;
            }
        }
        return null;
    }
    
    public List<Agendamento> getListaAgendamentos() {
        return this.listaAgendamentos;
    }

    private int gerarProximoIdAgendamento() {
        if (listaAgendamentos == null || listaAgendamentos.isEmpty()) {
            return 1;
        }
        return listaAgendamentos.stream()
                .mapToInt(Agendamento::getIdAgendamento)
                .max()
                .orElse(0) + 1;
    }

    public void salvarDadosAgendamentos() {
        try (Writer writer = new FileWriter(ARQUIVO_AGENDAMENTOS_JSON)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_ADAPTER)
                    .create();
            gson.toJson(listaAgendamentos, writer);
        } catch (IOException e) {
            System.err.println("GerenciadorAgendamentos: Erro ao salvar dados: " + e.getMessage());
        }
    }

    private List<Agendamento> carregarDadosAgendamentos() {
        try (Reader reader = new FileReader(ARQUIVO_AGENDAMENTOS_JSON)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_ADAPTER)
                    .create();
            List<Agendamento> agendamentos = gson.fromJson(reader, new TypeToken<List<Agendamento>>(){}.getType());
            System.out.println("GerenciadorAgendamentos: Agendamentos carregados. Total: " + (agendamentos != null ? agendamentos.size() : 0));
            return agendamentos != null ? agendamentos : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorAgendamentos: Arquivo '" + ARQUIVO_AGENDAMENTOS_JSON + "' nao encontrado. Criando lista vazia.");
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("GerenciadorAgendamentos: Erro ao carregar dados: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
