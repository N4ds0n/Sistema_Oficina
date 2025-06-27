package com.mycompany.sistemaoficina;

import java.util.Objects;

/**
 * Classe reponsável por alocar os elevadores, eles tem um numero que é seu identificador,
 * Um tipo que indica se é um elevador fixo ou não.
 * E um método que indica se o elevador esta ocupado ou não.
 * @author santo
 */
public class Elevador {

    private int numero; //Identificador do elevador
    private String tipo; // 1 elevador fixo, outros dois para outras atividades
    private boolean ocupado; // Indica se o elevador esta ou não ocupado

    /**
     * Construtor padrão
     */
    public Elevador() {
    }

    public Elevador(int numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.ocupado = false; // Elevador não ocupado por padrão ao ser criado
    }

    /**
     * Getters e Setters.
     * @return 
     */
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isOcupado() {
        return ocupado;
    }
    
    /**
     * Centraliza o controle de estado.
     * @param ocupado 
     */
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    /**
     * Sobrescreve o metodo toString() para fornecer uma representacao legivel do Elevador.
     * @return Uma String formatada com os detalhes do elevador.
     */
    @Override
    public String toString() {
        return "Elevador{" +
                "numero=" + numero +
                ", tipo='" + tipo + '\'' +
                ", ocupado=" + ocupado +
                '}';
    }

    /**
     * Sobrescreve o metodo equals() para comparar objetos Elevador.
     * Dois elevadores sao considerados iguais se tiverem o mesmo numero.
     * @param o O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elevador elevador = (Elevador) o;
        return numero == elevador.numero;
    }

    /**
     * Sobrescreve o metodo hashCode() para gerar um codigo hash para o objeto Elevador.
     * Baseia-se no numero do elevador.
     * @return O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}