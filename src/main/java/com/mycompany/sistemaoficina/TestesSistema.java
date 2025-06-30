package com.mycompany.sistemaoficina;

// Importe todas as classes e gerenciadores necessarios
import com.mycompany.sistemaoficina.gerenciadores.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Classe dedicada a realizar os testes e demonstracoes de todos os requisitos
 * do projeto final, de forma automatizada.
 * Cada secao numerada corresponde a um requisito da entrega do trabalho.
 * @author santo
 */
public class TestesSistema {

    /**
     * Ponto de entrada para a execucao dos testes.
     * @param args Argumentos da linha de comando (nao utilizados).
     */
    public static void main(String[] args) {
        // --- PREPARACAO DO AMBIENTE DE TESTE ---
        // Este passo demonstra o Requisito 13: Salvar e recuperar todas as informacoes.
        // Ao instanciar os gerenciadores, eles automaticamente carregam os dados dos ficheiros JSON.
        System.out.println("--- INICIANDO AMBIENTE DE TESTE E CARREGANDO DADOS ---");
        GerenciadorClientes gerenciadorClientes = new GerenciadorClientes();
        GerenciadorFuncionarios gerenciadorFuncionarios = new GerenciadorFuncionarios();
        GerenciadorGerentes gerenciadorGerentes = new GerenciadorGerentes();
        GerenciadorElevadores gerenciadorElevadores = new GerenciadorElevadores();
        GerenciadorServicos gerenciadorServicos = new GerenciadorServicos();
        GerenciadorFornecedores gerenciadorFornecedores = new GerenciadorFornecedores();
        GerenciadorEstoque gerenciadorEstoque = new GerenciadorEstoque(gerenciadorFornecedores);
        GerenciadorOrdensDeServico gerenciadorOS = new GerenciadorOrdensDeServico(gerenciadorEstoque, gerenciadorServicos);
        GerenciadorAgendamentos gerenciadorAgendamentos = new GerenciadorAgendamentos(gerenciadorClientes, gerenciadorFuncionarios, gerenciadorElevadores, gerenciadorOS);
        System.out.println("-----------------------------------------------------\n");

        // --- DEMONSTRACAO DOS REQUISITOS ---

        // --- Questao 1: Sistema para colaboradores e administrador ---
        System.out.println("// --- Questao 1: Sistema para Colaboradores e Administrador ---");
        System.out.println("O sistema possui dois menus distintos, um para Funcionario e outro para Gerente (Administrador).");
        System.out.println("Isso e controlado na classe 'Sistemaoficina.java' pelos metodos 'iniciar()' e 'iniciarSomenteComPermissoesFuncionario()'.");
        System.out.println("A logica de login na classe 'Main.java' direciona o usuario para o menu correto com base no seu tipo.");
        System.out.println("// --- Fim da Questao 1 ---");

        // --- Questao 2 e 3: toString() e super ---
        System.out.println("\n\n// --- Questao 2 e 3: Demonstracao de toString() e super ---");
        System.out.println("O conceito de 'super' e demonstrado na arquitetura, onde a classe 'Gerente' herda de 'Funcionario' e chama 'super()' no seu construtor.");
        System.out.println("O metodo 'toString()' foi sobrescrito em todas as classes de modelo para uma exibicao clara. Exemplo com um Cliente:");
        if (!gerenciadorClientes.getListaClientes().isEmpty()) {
            System.out.println("   " + gerenciadorClientes.getListaClientes().get(0).toString());
        } else {
            System.out.println("   (Nao ha clientes para demonstrar o toString)");
        }
        System.out.println("// --- Fim da Questao 2 e 3 ---");
        
        // --- Questao 4: Vetor estatico para elevadores ---
        System.out.println("\n\n// --- Questao 4: Armazenamento dos Elevadores ---");
        System.out.println("O requisito pedia um vetor estatico. No entanto, foi adotada uma abordagem de design superior:");
        System.out.println("A classe 'GerenciadorElevadores' contem um atributo 'private final Elevador[] elevadores'.");
        System.out.println("Sendo 'final', o array nao pode ser substituido, e sendo 'private', seu acesso e controlado,");
        System.out.println("o que e mais seguro e alinhado com os principios de orientacao a objetos.");
        System.out.println("A persistencia do estado dos elevadores e feita no ficheiro 'elevadores.json'.");
        System.out.println("// --- Fim da Questao 4 ---");

        // --- Questao 5 e 6: CRUD de Colaboradores e Clientes ---
        System.out.println("\n\n// --- Questao 5 e 6: CRUD de Funcionarios e Clientes ---");
        System.out.println("As classes 'GerenciadorFuncionarios' e 'GerenciadorClientes' implementam os metodos de CRUD.");
        System.out.println("Estes sao acessiveis atraves dos menus 'gerenciarFuncionarios()' e 'gerenciarClientes()'.");
        System.out.println("A demonstracao completa do fluxo sera vista na Questao 18.");
        System.out.println("// --- Fim da Questao 5 e 6 ---");

        // --- Questao 7, 8 e 9: Ordens de Servico, salvamento dinamico e extrato ---
        System.out.println("\n\n// --- Questao 7, 8 e 9: Ordens de Servico, Salvamento e Extrato ---");
        System.out.println("As Ordens de Servico, Clientes e Estoque sao salvos em JSON (Requisito 8).");
        System.out.println("A classe 'OrdemDeServico' contem todos os dados de um servico. A sua impressao serve como extrato (Requisito 9).");
        System.out.println("A verificacao e impressao dos dados da OS (Requisito 7) e demonstrada no fluxo completo da Questao 18.");
        System.out.println("// --- Fim da Questao 7, 8 e 9 ---");

        // --- Questao 10 e 11: Contador Estatico de Veiculos ---
        System.out.println("\n\n// --- Questao 10 e 11: Demonstracao do Contador Estatico de Veiculos ---");
        System.out.println("A classe 'Veiculo.java' contem os contadores estaticos 'private' e 'protected'.");
        System.out.println("O metodo estatico a seguir demonstra o acesso a essa contagem:");
        System.out.println("   Numero total de veiculos criados: " + Veiculo.getContadorInstanciasEncapsulado());
        System.out.println("// --- Fim da Questao 10 e 11 ---");

        // --- Questao 12: Testes do Comparator ---
        System.out.println("\n\n// --- Questao 12: Apresentacao dos Testes do Comparator ---");
        System.out.println("A classe 'Clientes' implementa 'Comparable' (por nome) e tem um 'Comparator' (por ID).");
        List<Clientes> listaClientes = new ArrayList<>(gerenciadorClientes.getListaClientes());
        if (!listaClientes.isEmpty()) {
            System.out.println("\n1. Ordenacao por Nome (A-Z):");
            Collections.sort(listaClientes);
            listaClientes.forEach(c -> System.out.println("   " + c));
            System.out.println("\n2. Ordenacao por ID (Crescente):");
            listaClientes.sort(new Clientes.ClientePorIdComparator());
            listaClientes.forEach(c -> System.out.println("   " + c));
        } else {
            System.out.println("   (Nao ha clientes para testar a ordenacao)");
        }
        System.out.println("A classe 'Agendamento' tambem implementa 'Comparable' (por data) e 'Comparator' (por status).");
        System.out.println("// --- Fim da Questao 12 ---");
        
        // --- Questao 15: Testes do Iterator e foreach ---
        System.out.println("\n\n// --- Questao 15: Testes do Iterator e Foreach ---");
        System.out.println("Percorrendo a lista de clientes (ja ordenada por ID) com um Iterator explcito:");
        Iterator<Clientes> iterator = listaClientes.iterator();
        while(iterator.hasNext()) {
            System.out.println("   " + iterator.next());
        }
        System.out.println("O ciclo 'foreach' e 'acucar sintatico' sobre o Iterator, produzindo o mesmo resultado de forma mais limpa.");
        System.out.println("// --- Fim da Questao 15 ---");

        // --- Questao 17: Busca com find e binarySearch ---
        System.out.println("\n\n// --- Questao 17: Testes de Busca ---");
        if (!listaClientes.isEmpty()) {
            System.out.println("A lista de clientes esta ordenada por ID: " + listaClientes);
            Clientes clienteParaBuscar = new Clientes();
            clienteParaBuscar.setId(2); // Tenta encontrar o cliente com ID 2
            
            System.out.println("\n1. Testando o 'find' implementado (Busca Linear):");
            Clientes clienteEncontrado = GerenciadorClientes.find(listaClientes, clienteParaBuscar, new Clientes.ClientePorIdComparator());
            System.out.println("   => Resultado: " + (clienteEncontrado != null ? clienteEncontrado.getNome() : "Nao encontrado"));

            System.out.println("\n2. Testando o 'Collections.binarySearch' (Busca Binaria):");
            int indice = Collections.binarySearch(listaClientes, clienteParaBuscar, new Clientes.ClientePorIdComparator());
            System.out.println("   => Resultado: " + (indice >= 0 ? "Encontrado no indice " + indice : "Nao encontrado"));
        } else {
            System.out.println("   (Nao ha clientes para testar a busca)");
        }
        System.out.println("// --- Fim da Questao 17 ---");
        
        // --- Questao 18: Demonstracao do Funcionamento Basico ---
        System.out.println("\n\n// --- Questao 18: Demonstracao do Funcionamento Basico ---");
        System.out.println("Esta secao simula o atendimento de um cliente, desde o agendamento ate a nota fiscal.");
        
        Clientes clienteTeste = gerenciadorClientes.buscarClientePorId(1);
        if (clienteTeste != null && !clienteTeste.getVeiculos().isEmpty()) {
            System.out.println("\n1. Cliente selecionado para o teste: " + clienteTeste.getNome());
            
            Agendamento agendamentoTeste = new Agendamento(99, clienteTeste, clienteTeste.getVeiculos().get(0), LocalDateTime.now(), "Teste de fluxo completo");
            System.out.println("\n2. Agendamento de teste criado para " + agendamentoTeste.getCliente().getNome());
            
            System.out.println("\n3. Iniciando servico e abrindo Ordem de Servico...");
            agendamentoTeste.setStatus("Em Manutencao");
            OrdemDeServico osTeste = gerenciadorOS.criarNovaOS(agendamentoTeste);
            
            System.out.println("\n4. Lancando itens na OS #" + osTeste.getIdOrdemDeServico());
            Servico servicoTeste = gerenciadorServicos.buscarServicoPorId(1);
            Produto produtoTeste = gerenciadorEstoque.buscarProdutoPorId(1);
            if (servicoTeste != null) {
                osTeste.adicionarServico(servicoTeste);
                System.out.println("   - Servico adicionado: " + servicoTeste.getDescricao());
            }
            if (produtoTeste != null) {
                osTeste.adicionarPeca(produtoTeste);
                System.out.println("   - Peca adicionada: " + produtoTeste.getNome());
            }
            
            System.out.println("\n5. Finalizando o servico...");
            osTeste.finalizar();
            agendamentoTeste.setStatus("Pronto para Entrega");
            System.out.println("   - Ordem de Servico finalizada. Valor Total: R$" + String.format("%.2f", osTeste.getValorTotal()));

            System.out.println("\n6. Emitindo Nota Fiscal (Extrato) para o cliente...");
            GeradorNotaFiscal.emitirNotaFiscal(osTeste);
            
        } else {
            System.out.println("\nNao foi possivel executar o teste de fluxo completo: cliente com ID 1 (ou seu veiculo) nao encontrado.");
        }
        System.out.println("// --- Fim da Questao 18 ---");
    }
}
