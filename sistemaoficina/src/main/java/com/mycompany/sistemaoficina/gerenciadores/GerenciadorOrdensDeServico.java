package com.mycompany.sistemaoficina.gerenciadores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mycompany.sistemaoficina.Agendamento;
import com.mycompany.sistemaoficina.OrdemDeServico;
import com.mycompany.sistemaoficina.Produto;
import com.mycompany.sistemaoficina.Servico;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Gerencia todas as operacoes relacionadas as Ordens de Servico (OS).
 */
public class GerenciadorOrdensDeServico {

    private List<OrdemDeServico> listaOrdensDeServico;
    private static final String ARQUIVO_OS_JSON = "ordens_de_servico.json";

    private GerenciadorEstoque gerenciadorEstoque;
    private GerenciadorServicos gerenciadorServicos;


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
    
 /* Retorna a lista de todas as Ordens de Servico.
 * @return A lista de objetos OrdemDeServico.
 */
public List<OrdemDeServico> getListaOrdensDeServico() {
    return this.listaOrdensDeServico;
}


    public GerenciadorOrdensDeServico(GerenciadorEstoque gerenciadorEstoque, GerenciadorServicos gerenciadorServicos) {
        this.gerenciadorEstoque = gerenciadorEstoque;
        this.gerenciadorServicos = gerenciadorServicos;
        this.listaOrdensDeServico = carregarOrdensDeServico();
    }
    
    /**
 * Busca uma Ordem de Servico (de qualquer status) pelo ID do agendamento associado.
 * @param idAgendamento O ID do agendamento.
 * @return A OrdemDeServico encontrada, ou null.
 */
public OrdemDeServico buscarOSPorAgendamentoId(int idAgendamento) {
    for (OrdemDeServico os : listaOrdensDeServico) {
        if (os.getIdAgendamento() == idAgendamento) {
            return os;
        }
    }
    return null;
}
    /**
     * Cria uma nova Ordem de Servico associada a um agendamento.
     * @param agendamento O agendamento que esta dando origem a OS.
     * @return A OrdemDeServico recem-criada.
     */
    public OrdemDeServico criarNovaOS(Agendamento agendamento) {
        int novoId = gerarProximoIdOS();
        OrdemDeServico novaOS = new OrdemDeServico(novoId, agendamento);
        this.listaOrdensDeServico.add(novaOS);
        salvarOrdensDeServico();
        System.out.println("Ordem de Servico #" + novoId + " criada e aberta com sucesso.");
        return novaOS;
    }

    public OrdemDeServico buscarOSAbertaPorAgendamento(Agendamento agendamento) {
        for (OrdemDeServico os : listaOrdensDeServico) {
            if (os.getIdAgendamento() == agendamento.getIdAgendamento() 
                && os.getStatus().equals("Aberta")) {
                return os;
            }
        }
        return null;
    }

    /**
     * Menu principal para lancar itens em uma Ordem de Servico aberta.
     * @param scanner Scanner para entrada do usuario.
     */
    public void menuLancarItens(Scanner scanner) {
        System.out.println("\n--- Lancar Itens em Ordem de Servico ---");

        List<OrdemDeServico> osAbertas = listaOrdensDeServico.stream()
                .filter(os -> os.getStatus().equals("Aberta"))
                .collect(Collectors.toList());

        if (osAbertas.isEmpty()) {
            System.out.println("Nenhuma Ordem de Servico aberta no momento.");
            return;
        }

        System.out.println("Ordens de Servico em andamento:");
        for (OrdemDeServico os : osAbertas) {
            System.out.println("ID OS: " + os.getIdOrdemDeServico() + " | Cliente: " + os.getNomeCliente() + " | Veiculo: " + os.getPlacaVeiculo());
        }

        System.out.print("\nDigite o ID da OS para adicionar itens: ");
        try {
            int idOS = Integer.parseInt(scanner.nextLine());
            OrdemDeServico osSelecionada = buscarOSPorId(idOS);

            if (osSelecionada != null && osSelecionada.getStatus().equals("Aberta")) {
                menuDeAdicao(scanner, osSelecionada);
            } else {
                System.out.println("Ordem de Servico nao encontrada ou ja esta finalizada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }

    /**
     * Submenu para adicionar servicos, pecas ou mao de obra a uma OS especifica.
     */
    private void menuDeAdicao(Scanner scanner, OrdemDeServico os) {
        int opcao;
        do {
            System.out.println("\n--- Adicionando Itens a OS #" + os.getIdOrdemDeServico() + " ---");
            System.out.println("Cliente: " + os.getNomeCliente() + " | Veiculo: " + os.getModeloVeiculo() + " (" + os.getPlacaVeiculo() + ")");
            System.out.println("--- Itens Atuais ---");
            System.out.println(os.toString());
            System.out.println("--------------------");

            System.out.println("1. Adicionar Servico do Catalogo");
            System.out.println("2. Adicionar Peca do Estoque");
            System.out.println("3. Adicionar Mao de Obra Adicional");
            System.out.println("0. Concluir Lancamentos para esta OS");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Opcao invalida.");
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    lancarServicoEmOS(scanner, os);
                    break;
                case 2:
                    lancarPecaEmOS(scanner, os);
                    break;
                case 3:
                    lancarMaoDeObraEmOS(scanner, os);
                    break;
                case 0:
                    System.out.println("Lancamentos para a OS #" + os.getIdOrdemDeServico() + " concluidos.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private void lancarServicoEmOS(Scanner scanner, OrdemDeServico os) {
        gerenciadorServicos.listarServicos();
        if (gerenciadorServicos.getListaServicos().isEmpty()) {
            System.out.println("Nenhum servico cadastrado no catalogo para adicionar.");
            return;
        }

        System.out.print("Digite o ID do servico a ser adicionado: ");
        try {
            int idServico = Integer.parseInt(scanner.nextLine());
            Servico servico = gerenciadorServicos.buscarServicoPorId(idServico);
            if (servico != null) {
                os.adicionarServico(servico);
                salvarOrdensDeServico();
                System.out.println("Servico '" + servico.getDescricao() + "' adicionado a OS.");
            } else {
                System.out.println("Servico nao encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }

    private void lancarPecaEmOS(Scanner scanner, OrdemDeServico os) {
        gerenciadorEstoque.listarProdutos();
        if (gerenciadorEstoque.getListaProdutos().isEmpty()) {
            System.out.println("Nenhum produto cadastrado no estoque para adicionar.");
            return;
        }

        System.out.print("Digite o ID da peca a ser adicionada: ");
        try {
            int idPeca = Integer.parseInt(scanner.nextLine());
            Produto peca = gerenciadorEstoque.buscarProdutoPorId(idPeca);
            if (peca != null) {
                if (peca.getQuantidadeEstoque() > 0) {
                    os.adicionarPeca(peca);
                    peca.removerEstoque(1);
                    gerenciadorEstoque.salvarEstoque();
                    salvarOrdensDeServico();
                    System.out.println("Peca '" + peca.getNome() + "' adicionada a OS. Estoque restante: " + peca.getQuantidadeEstoque());
                } else {
                    System.out.println("Erro: Peca '" + peca.getNome() + "' sem estoque.");
                }
            } else {
                System.out.println("Peca nao encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID invalido.");
        }
    }

    private void lancarMaoDeObraEmOS(Scanner scanner, OrdemDeServico os) {
        System.out.println("\n--- Adicionar Mao de Obra Adicional ---");
        try {
            System.out.print("Descricao da mao de obra (ex: 'Remocao de parafuso emperrado'): ");
            String descricao = scanner.nextLine();

            System.out.print("Valor a ser cobrado pela mao de obra: ");
            double valor = Double.parseDouble(scanner.nextLine());

            if (valor > 0) {
                Servico maoDeObra = new Servico(0, descricao, valor); // ID 0 para item nao catalogado
                os.adicionarServico(maoDeObra);
                salvarOrdensDeServico();
                System.out.println("Mao de obra adicional adicionada com sucesso a OS.");
            } else {
                System.out.println("O valor deve ser maior que zero.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor invalido.");
        }
    }
    
    public OrdemDeServico buscarOSPorId(int id) {
        for(OrdemDeServico os : listaOrdensDeServico) {
            if(os.getIdOrdemDeServico() == id) {
                return os;
            }
        }
        return null;
    }

    private int gerarProximoIdOS() {
        if (listaOrdensDeServico.isEmpty()) {
            return 1;
        }
        return listaOrdensDeServico.stream().mapToInt(OrdemDeServico::getIdOrdemDeServico).max().orElse(0) + 1;
    }

    public void salvarOrdensDeServico() {
        // CORRECAO: Cria o Gson com o adaptador de data/hora registrado
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_ADAPTER)
                .create();
        try (Writer writer = new FileWriter(ARQUIVO_OS_JSON)) {
            gson.toJson(this.listaOrdensDeServico, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar Ordens de Servico: " + e.getMessage());
        }
    }

    private List<OrdemDeServico> carregarOrdensDeServico() {
        try (Reader reader = new FileReader(ARQUIVO_OS_JSON)) {
            // CORRECAO: Cria o Gson com o adaptador de data/hora registrado
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_ADAPTER)
                    .create();
            Type listType = new TypeToken<ArrayList<OrdemDeServico>>(){}.getType();
            List<OrdemDeServico> ordens = gson.fromJson(reader, listType);
            System.out.println("GerenciadorOrdensDeServico: " + (ordens != null ? ordens.size() : 0) + " ordens carregadas.");
            return ordens != null ? ordens : new ArrayList<>();
        } catch (FileNotFoundException e) {
            System.out.println("GerenciadorOrdensDeServico: Arquivo 'ordens_de_servico.json' nao encontrado.");
            return new ArrayList<>();
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            System.err.println("Erro ao carregar Ordens de Servico: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
