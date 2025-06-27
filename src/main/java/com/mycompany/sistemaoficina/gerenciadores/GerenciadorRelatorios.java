package com.mycompany.sistemaoficina.gerenciadores;

import com.mycompany.sistemaoficina.Despesa;
import com.mycompany.sistemaoficina.OrdemDeServico;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *Responsavel por gerar relatorios financeiros e de vendas com base nos dados de outros gerenciadores.
 * Esta classe não armazena dados proprios, atuando como um serviço de processamento e exibição de informações.
 * @author santo
 */
public class GerenciadorRelatorios {
    
     // Dependencias dos gerenciadores que contem os dados
    private final GerenciadorOrdensDeServico gerenciadorOS;
    private final GerenciadorDespesas gerenciadorDespesas;
    
/**
     * Construtor que recebe as instancias dos gerenciadores de dados.
     * @param gos A instancia do GerenciadorOrdensDeServico.
     * @param gd A instancia do GerenciadorDespesas.
     */
    public GerenciadorRelatorios(GerenciadorOrdensDeServico gos, GerenciadorDespesas gd) {
        this.gerenciadorOS = gos;
        this.gerenciadorDespesas = gd;
    }    
    
    /**
     * Exibe o menu principal para ageração  de relatorios e balanços financeiros.
     * @param scanner A instancia do Scanner para ler a entrada do usuário.
     */
     public void menuRelatorios(Scanner scanner) {
        int opcao;
        do {
            System.out.println("\n=== MENU DE RELATORIOS E BALANCO ===");
            System.out.println("1. Relatorio de Vendas do Dia");
            System.out.println("2. Relatorio de Vendas do Mes");
            System.out.println("3. Balanco Financeiro Mensal");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Opcao invalida.");
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    relatorioVendasDiario(scanner);
                    break;
                case 2:
                    relatorioVendasMensal(scanner);
                    break;
                case 3:
                    balancoMensal(scanner);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }
    
     /**
      * Gera e exibe um relatorio de todas as Ordens de Serviço finalizadas em um dia especifico.
      * @param scanner 
      */
    private void relatorioVendasDiario(Scanner scanner) {
        System.out.print("\nDigite a data para o relatorio (formato dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataDesejada = LocalDate.parse(dataStr, formatter);
            
            // Pega a lista completa de OS do gerenciador
            List<OrdemDeServico> todasAsOS = gerenciadorOS.getListaOrdensDeServico();

            // Filtra as OS que foram finalizadas na data desejada
            List<OrdemDeServico> osDoDia = todasAsOS.stream()
                .filter(os -> os.getStatus().equals("Finalizada"))
                .filter(os -> os.getDataEmissao().toLocalDate().equals(dataDesejada))
                .collect(Collectors.toList());

            System.out.println("\n--- RELATORIO DE VENDAS E SERVICOS - " + dataStr + " ---");
            if (osDoDia.isEmpty()) {
                System.out.println("Nenhuma Ordem de Servico finalizada nesta data.");
                return;
            }

            double totalReceitas = 0;
            for (OrdemDeServico os : osDoDia) {
                System.out.println("OS #" + os.getIdOrdemDeServico() + " | Cliente: " + os.getNomeCliente() + " | Valor: R$" + String.format("%.2f", os.getValorTotal()));
                totalReceitas += os.getValorTotal();
            }

            System.out.println("----------------------------------------------");
            System.out.println("Total de Receitas do Dia: R$" + String.format("%.2f", totalReceitas));
            System.out.println("----------------------------------------------");

        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data invalido.");
        }
    }
  
    /**
     * Gera e exibe um relarorio consolidado de todas as Ordens de Serviço dinalizadas em um mes e ano especificos.
     * @param scanner A instancia do Scanner para ler a entrada do usuario.
     */
    private void relatorioVendasMensal(Scanner scanner) {
        System.out.print("\nDigite o mes e ano para o relatorio (formato MM/yyyy): ");
        String mesAnoStr = scanner.nextLine();
        try {
            String[] partes = mesAnoStr.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            List<OrdemDeServico> osDoMes = gerenciadorOS.getListaOrdensDeServico().stream()
                .filter(os -> os.getStatus().equals("Finalizada"))
                .filter(os -> os.getDataEmissao().getMonthValue() == mes && os.getDataEmissao().getYear() == ano)
                .collect(Collectors.toList());

            System.out.println("\n--- RELATORIO DE VENDAS E SERVICOS - " + mesAnoStr + " ---");
            if (osDoMes.isEmpty()) {
                System.out.println("Nenhuma Ordem de Servico finalizada neste mes.");
                return;
            }
            
            double totalReceitas = osDoMes.stream().mapToDouble(OrdemDeServico::getValorTotal).sum();

            System.out.println("Total de Ordens de Servico Finalizadas: " + osDoMes.size());
            System.out.println("Total de Receitas do Mes: R$" + String.format("%.2f", totalReceitas));
            System.out.println("-------------------------------------------------");
            
        } catch (Exception e) {
            System.out.println("Erro: Formato de data invalido ou dados incorretos.");
        }
    }
    
    /**
     * Gera e exibe um balanço financeiro completo de um mes e ano especificos.
     * Calcula o total de receitas, o total de despecas e o resultado final(lucro ou prejuízo).
     * @param scanner 
     */
    private void balancoMensal(Scanner scanner) {
        System.out.print("\nDigite o mes e ano para o balanco (formato MM/yyyy): ");
        String mesAnoStr = scanner.nextLine();
        try {
            String[] partes = mesAnoStr.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            // 1. Calcula as Receitas
            double totalReceitas = gerenciadorOS.getListaOrdensDeServico().stream()
                .filter(os -> os.getStatus().equals("Finalizada"))
                .filter(os -> os.getDataEmissao().getMonthValue() == mes && os.getDataEmissao().getYear() == ano)
                .mapToDouble(OrdemDeServico::getValorTotal)
                .sum();

            // 2. Calcula as Despesas
            double totalDespesas = gerenciadorDespesas.getListaDespesas().stream()
                .filter(d -> d.getData().getMonthValue() == mes && d.getData().getYear() == ano)
                .mapToDouble(Despesa::getValor)
                .sum();
            
            // 3. Calcula o resultado
            double resultado = totalReceitas - totalDespesas;
            
            System.out.println("\n--- BALANCO MENSAL - " + Month.of(mes).name() + " / " + ano + " ---");
            System.out.println(String.format("Total de Receitas (Vendas e Servicos): + R$ %.2f", totalReceitas));
            System.out.println(String.format("Total de Despesas (Custos Fixos e Variaveis): - R$ %.2f", totalDespesas));
            System.out.println("-------------------------------------------------------");
            System.out.println(String.format("Resultado do Mes: R$ %.2f", resultado));
            System.out.println("-------------------------------------------------------");
            
            if (resultado > 0) {
                System.out.println("Status: LUCRO");
            } else if (resultado < 0) {
                System.out.println("Status: PREJUIZO");
            } else {
                System.out.println("Status: EMPATE");
            }

        } catch (Exception e) {
            System.out.println("Erro: Formato de data invalido ou dados incorretos.");
        }
    }
    
    
}
