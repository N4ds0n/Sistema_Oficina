package com.mycompany.sistemaoficina.gerenciadores;

import com.mycompany.sistemaoficina.OrdemDeServico;
import com.mycompany.sistemaoficina.Produto;
import com.mycompany.sistemaoficina.Servico;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaria responsavel por gerar uma representacao textual
 * de uma Nota Fiscal com base em uma Ordem de Servico.
 * @author santo
 */
public class GeradorNotaFiscal {
    
     /**
     * Gera e imprime no console uma Nota Fiscal formatada a  partir de uma Ordem de Serviço
     * Este método static pode ser chamado de qualquer parte do sistema para "imprimir" a nota.
     * @param os A Ordem de Servico finalizada a partir da qual a nota sera gerada.
     */
    public static void emitirNotaFiscal(OrdemDeServico os) {
        // Formatter para exibir datas de forma legivel
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Cabecalho da Nota Fiscal com informações da empresa.
        System.out.println("\n\n========================================================");
        System.out.println("                    NOTA FISCAL DE SERVICOS");
        System.out.println("========================================================");
        System.out.println("OFICINA MILHO VERDE AUTOMOTIVA");
        System.out.println("CNPJ: 12.345.678/**01-**");
        System.out.println("Rua das Flores, 123 - Milho Verde, MG");
        System.out.println("--------------------------------------------------------");
        
        // Dados da Transacao
        // String.format e usado para formatar o numero com 6 digitos, preenchendo com zeros
        System.out.println("Nota Fiscal No: " + String.format("%06d", os.getIdOrdemDeServico())); 
        System.out.println("Data de Emissao: " + os.getDataEmissao().format(formatter));
        System.out.println("--------------------------------------------------------");

        // Dados do Cliente, obtidos da Ordem de Serviço.
        System.out.println("CLIENTE:");
        System.out.println("  Nome: " + os.getNomeCliente());
        System.out.println("  Veiculo: " + os.getModeloVeiculo());
        System.out.println("  Placa: " + os.getPlacaVeiculo());
        System.out.println("--------------------------------------------------------");
        
        // Itens da Nota (Servicos e Pecas)
        System.out.println("DESCRICAO DOS ITENS");
        System.out.println("--------------------------------------------------------");
        
        // Exibe a lista de Servicos realizados e os imprime de forma alinhada.
        if (!os.getServicosRealizados().isEmpty()) {
            System.out.println("SERVICOS PRESTADOS:");
            for (Servico servico : os.getServicosRealizados()) {
                String nomeServico = servico.getDescricao();
                String valorFormatado = String.format("R$ %.2f", servico.getValor());
                // printf permite formatar e alinhar o texto
                System.out.printf("  - %-35s %15s%n", nomeServico, valorFormatado);
            }
        }
        
        // Exibe a ista de Pecas utilizadas e as imprime de forma alinhada.
        if (!os.getPecasUtilizadas().isEmpty()) {
            System.out.println("\nPECAS UTILIZADAS:");
            for (Produto peca : os.getPecasUtilizadas()) {
                String nomePeca = peca.getNome();
                String valorFormatado = String.format("R$ %.2f", peca.getPrecoVenda());
                System.out.printf("  - %-35s %15s%n", nomePeca, valorFormatado);
            }
        }
        
        System.out.println("========================================================");
        
        // Totalizador da nota, alinhado a direita.
        String valorTotalFormatado = String.format("R$ %.2f", os.getValorTotal());
        System.out.printf("VALOR TOTAL %38s%n", valorTotalFormatado);
        
        System.out.println("========================================================");
        System.out.println("Obrigado pela preferencia!");
        System.out.println("\n");
    }
    
}
