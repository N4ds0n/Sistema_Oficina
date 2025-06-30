package com.mycompany.sistemaoficina;

import java.util.Objects;

/**
 * Classe que representa um veículo associado a um cliente da oficina
 * inclui contadores estaticos para rastrear o numero de instancias criadas.
 * @author santo
 */
public class Veiculo {
    // Atributos privados do veículo
    private String modelo; // Ex: "Fiat Uno"
    private String placa; // Ex: "ABC-1234"
    private String cor; // Ex: "vermelho"
    private int ano; // Ex: "2010"
    
    // Adicionando os contadores estatísticos:
    /**
     * Contador privado (encapsulado).. Só pode ser acessado atraves de metodos publicos.
     * Esta é a abordagem padrão e mais segura da engenharia de software.
     */
    private static int contadorInstanciasEncapsulado = 0;
    
    /**
     * Contador protegido. Pode ser acessado diretamente por classes no mesmo pacote
     * ou por subclasses, mesmo em pacotes diferentes. Menos seguro.
     */
    protected static int contadorInstanciasProtegido = 0;

    /**
     * Construtor padrão.
     */
    public Veiculo() {
        //Incrementando os contadores no construtor
        contadorInstanciasEncapsulado++;
        contadorInstanciasProtegido++;    
    }

    /**
     * Construtor completo que inicializa o veículo com modelo, placa, cor e ano.
     * @param modelo
     * @param placa
     * @param cor
     * @param ano 
     */
    public Veiculo(String modelo, String placa, String cor, int ano) {
        this(); // CHama o construtor padrão para incrementar os contadores.
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
        this.ano = ano;
    }

   // Getters estaticos para acessar os contadores
    
    /**
     * Metodo de classe (static) para obter a contagem da variavel privada.
     * @return O numero total de instancias de Veiculo criadas.
     */
    public static int getContadorInstanciasEncapsulado(){
        return contadorInstanciasEncapsulado;
    }
    
    /**
     * Metodo de classe (static) para obter a contagem da variavel protegida.
     * @return O numero total de instancias de Veiculo criadas.
     */
    public static int getContadorInstanciasProtegido(){
        return contadorInstanciasProtegido;
    }
    
    
    // Getters e Setters normais
    public String getModelo() { 
        return modelo; 
    }
    public void setModelo(String modelo) { 
        this.modelo = modelo; 
    }

    public String getPlaca() { 
        return placa; 
    }
    public void setPlaca(String placa) {
        this.placa = placa; 
    }

    public int getAno() { 
        return ano; 
    }
    public void setAno(int ano) { 
        this.ano = ano; 
    }

    public String getCor() {
        return cor; 
    }
    public void setCor(String cor) {
        this.cor = cor; 
    }

    /**
     * Método sobrescrito para exibir informações do veículo de forma legível.
     * @return String contendo as informações do veículo
     */
   @Override
    public String toString() {
        return modelo + " (" + placa + ") - " + ano + " - " + cor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(placa.toLowerCase(), veiculo.placa.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa.toLowerCase());
    }
}
