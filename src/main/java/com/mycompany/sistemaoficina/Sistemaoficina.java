package com.mycompany.sistemaoficina;

import com.mycompany.sistemaoficina.gerenciadores.GerenciadorClientes; 
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorFuncionarios; 
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorGerentes; 
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorAgendamentos; 
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorElevadores;
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorEstoque;
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorServicos;
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorOrdensDeServico;
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorDespesas;
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorRelatorios;
import com.mycompany.sistemaoficina.gerenciadores.GerenciadorFornecedores;


import java.util.Scanner; 

/**
 * Classe Principal do Sistema Oficina.
 * Responsavel por orquestrar as operacoes e gerenciar os diferentes gerenciadores de dados.
 * Atua como o ponto central da aplicação, controlando os menus e o fluxo de trabalho.
 * @author santo
 */
public class Sistemaoficina {

    /**
     *  Instancias dos gerenciadores. 
     * Declaracao de TODOS os gerenciadores como atributos finais da classe
    */
    private final GerenciadorClientes gerenciadorClientes;
    private final GerenciadorFuncionarios gerenciadorFuncionarios; 
    private final GerenciadorGerentes gerenciadorGerentes; 
    private final GerenciadorAgendamentos gerenciadorAgendamentos; 
    private final GerenciadorElevadores gerenciadorElevadores;
    private final GerenciadorEstoque gerenciadorEstoque;
    private final GerenciadorServicos gerenciadorServicos;
    private final GerenciadorOrdensDeServico gerenciadorOrdensDeServico;
    private final GerenciadorDespesas gerenciadorDespesas;
    private final GerenciadorRelatorios gerenciadorRelatorios;
    private final GerenciadorFornecedores gerenciadorFornecedores;

    /**
     * Construtor da classe.
     * Inicializa os gerenciadores na ordem correta de suas dependencias.
     * Cada atributo 'final' e inicializado exatamente uma vez.
     */
    public Sistemaoficina() {
        // Gerenciadores que nao dependem de ninguem
        this.gerenciadorClientes = new GerenciadorClientes();
        this.gerenciadorFuncionarios = new GerenciadorFuncionarios();
        this.gerenciadorGerentes = new GerenciadorGerentes();
        this.gerenciadorElevadores = new GerenciadorElevadores();
        this.gerenciadorServicos = new GerenciadorServicos();
        this.gerenciadorDespesas = new GerenciadorDespesas();
        this.gerenciadorFornecedores = new GerenciadorFornecedores();

        // Gerenciador que dependem de outros ja criados
        this.gerenciadorEstoque = new GerenciadorEstoque(this.gerenciadorFornecedores);
        this.gerenciadorOrdensDeServico = new GerenciadorOrdensDeServico(this.gerenciadorEstoque, this.gerenciadorServicos);
        this.gerenciadorAgendamentos = new GerenciadorAgendamentos(this.gerenciadorClientes, this.gerenciadorFuncionarios, this.gerenciadorElevadores, this.gerenciadorOrdensDeServico);
        this.gerenciadorRelatorios = new GerenciadorRelatorios(this.gerenciadorOrdensDeServico, this.gerenciadorDespesas);
    }

    /**
     * Metodo principal para iniciar o sistema para um gerente logado.
     * Exibe o menu com todas as permissoes administrativas.
     * @param gerenteLogado O objeto Gerente que fez login no sistema.
     */
    public void iniciar(Gerente gerenteLogado) { 
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("Bem-vindo ao Sistema Oficina, Gerente " + gerenteLogado.getNome() + "!");
        do {
            exibirMenuPrincipal(); 
            opcao = scanner.nextInt(); 
            scanner.nextLine(); 

            switch (opcao) {
                 case 1:
                    gerenciadorClientes.gerenciarClientes();
                    break;
                case 2:
                    gerenciadorAgendamentos.gerenciarAgendamentos(scanner);
                    break;
                case 3:
                    gerenciadorEstoque.gerenciarEstoque(scanner);
                    break;
                case 4:
                    gerenciadorServicos.gerenciarServicos(scanner);
                    break;
                case 5:
                    gerenciadorFornecedores.gerenciarFornecedores(scanner);
                    break;
                case 6:
                    gerenciadorDespesas.gerenciarDespesas(scanner);
                    break;
                case 7:
                    gerenciadorRelatorios.menuRelatorios(scanner);
                    break;
                case 8:
                    exibirContagemDeVeiculos();
                    break;
                case 9:
                     gerenciadorFuncionarios.gerenciarFuncionarios();
                    break;
                case 10:
                    gerenciadorGerentes.gerenciarMeusDadosGerente(gerenteLogado, scanner);
                    break;
                case 11:
                    gerenciadorElevadores.menuAdministrativoElevadores(scanner);
                    break;
                case 0:
                    System.out.println("Sistema encerrado. Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
        scanner.close();
    }

    /**
     * Metodo para iniciar o sistema com permissoes de funcionario.
     * Exibe um menu com acesso limitado as funcionalidades operacionais.
     * @param funcionarioLogado O objeto Funcionario que fez login no sistema.
     */
    public void iniciarSomenteComPermissoesFuncionario(Funcionario funcionarioLogado) { 
        Scanner scanner = new Scanner(System.in);
        int opcao;
        System.out.println("Bem-vindo ao Sistema Oficina, " + funcionarioLogado.getNome()+ "!");
        do {
            System.out.println("\n=== MENU FUNCIONARIO ===");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Agendamentos e Ordens de Servico"); 
            System.out.println("3. Gerenciar Vendas e Estoque");
            System.out.println("4. Consultar Catalogo de Servicos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    gerenciadorClientes.gerenciarClientes(); 
                    break;
                case 2:
                    gerenciadorAgendamentos.gerenciarAgendamentos(scanner); 
                    break;
                case 3:
                    gerenciadorEstoque.gerenciarEstoque(scanner);
                    break;
                case 4:
                    gerenciadorServicos.consultarCatalogoServicos(); 
                    break;
                case 8: 
                    gerenciadorElevadores.listarElevadores();
                    break;
                case 0:
                    System.out.println("Sistema encerrado. Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
        scanner.close();
    }

    /**
     * Exibe na tela as opções do menu principal para o perfil de Gerente
     */
     private void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL (GERENTE) ===");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Agendamentos e Servicos");
        System.out.println("3. Gerenciar Estoque");
        System.out.println("4. Gerenciar Catalogo de Servicos");
        System.out.println("5. Gerenciar Fornecedores"); 
        System.out.println("6. Lancar Despesas");
        System.out.println("7. Relatorios e Balanco Financeiro");
        System.out.println("8. Ver Estatisticas do Sistema");
        System.out.println("9. Gerenciar Funcionarios");
        System.out.println("10. Gerenciar Meus Dados (Gerente)");
        System.out.println("11. Gestao Administrativa de Elevadores");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opcao: ");
    }

     /**
     * Metodo para verificar se existe algum gerente cadastrado.
     * Delega a chamada ao GerenciadorGerentes.
     * @return true se houver gerentes, false caso contrario.
     */
    public boolean existeGerenteCadastrado(){
        return !gerenciadorGerentes.getListaGerentes().isEmpty();
    }
    
   /**
     * Metodo para cadastrar o gerente inicial.
     * Delega a chamada ao GerenciadorGerentes.
     * @param scanner Scanner para entrada de dados.
     */
    public void cadastrarGerenteInicial(Scanner scanner){
        gerenciadorGerentes.cadastrarGerenteInicial(scanner);
    }
    
    /**
     * Autentica um funcionario usando o AuthService.
     * @param cpf CPF do funcionario.
     * @param senha Senha do funcionario.
     * @return O objeto Funcionario autenticado, ou null.
     */
    public Funcionario autenticarFuncionario(String cpf, String senha) {
        return AuthService.autenticarFuncionario(cpf, senha, gerenciadorFuncionarios.getListaFuncionarios());
    }

    /**
     * Autentica um gerente usando o AuthService.
     * @param cpf CPF do gerente.
     * @param senha Senha do gerente.
     * @return O objeto Gerente autenticado, ou null.
     */
    public Gerente autenticarGerente(String cpf, String senha) {
        return AuthService.autenticarGerente(cpf, senha, gerenciadorGerentes.getListaGerentes());
    }
    
    /**
     * Exibe no console o numero total de instancias da classe Veiculo que foram criadas
     * durante a execução do sistema. Utilizando duas abordagens de contagem.
     */
    public void exibirContagemDeVeiculos() {
    System.out.println("\n--- ESTATISTICAS DE INSTANCIAS ---");
    // Chama o metodo estatico diretamente da classe Veiculo
    System.out.println("Numero total de veiculos cadastrados (abordagem encapsulada): " + Veiculo.getContadorInstanciasEncapsulado());
    System.out.println("Numero total de veiculos cadastrados (abordagem protegida): " + Veiculo.getContadorInstanciasProtegido());
    System.out.println("------------------------------------");
}
}
