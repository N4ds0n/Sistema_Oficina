package com.mycompany.sistemaoficina;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Classe que representa o Gerente da Oficina. 
 * Ela herda atributos e metodos basicos da classe Funcionario, como ID, nome, CPF e senha.
 * @author santo
 */
public class Gerente extends Funcionario {

    // Gerente não precisa mais de nome, cpf, senha como atributos próprios,
    // pois eles são herdados de Funcionario.

    /**
     * Construtor padrão.
     * Chama o construtor da superclasse Funcionario.
     */
    public Gerente() {
        super(); // Chama o construtor padrão de Funcionario
    }

    /**
     * Construtor para criar um novo Gerente com todos os dados necessários.
     * @param id unico do Gerente
     * @param nome O nome completo do gerente.
     * @param cpf CPF do gerente
     * @param senha  A senha de acesso ao sistema do gerente.
     */
    public Gerente(int id, String nome, String cpf, String senha) {
        super(id, nome, cpf, senha); // Chama o construtor de Funcionario com ID
    }

    /**
     * Construtor para criar um novo Gerente sem um ID predefinico.
     * O ID é gerado externamente pelo AuthService.
     * @param nome
     * @param cpf
     * @param senha 
     */
    public Gerente(String nome, String cpf, String senha) {
        super(0, nome, cpf, senha); // ID 0 temporário, será atualizado pelo AuthService
    }


    // Getters e setters para nome, cpf, senha não são mais necessários aqui,
    // pois são herdados de Funcionario.

   /**
    * Salva o objeto Gerente atual em um arquivo JSOn individual.
    * O nome do arquivo é baseado no ID do gerente para garantir a unicidade.
    * @throws IOException se ocorrer um erro durante a escrita do arquivo.
    */
    public void salvarComoJson() throws IOException {
        String dirPath = "data/gerentes";
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Usa o ID herdado de Funcionario para o nome do arquivo
        File arquivo = new File(dir, this.getId() + ".json"); 

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(arquivo)) {
            gson.toJson(this, writer);
        }
    }

    /**
     * Fornece uma representação textual resumida do Gerente.
     * Utiliza os metodos Getters herdados da classe Funcionario.
     * @return Uma String formatada com os principais dados do gerente.
     */
    @Override
    public String toString() {
        // Chama o toString da superclasse Funcionario e adiciona informações específicas de Gerente, se houver.
        return "Gerente{" +
                "id=" + getId() + // ID é herdado de Funcionario
                ", nome='" + getNome() + '\'' + // Nome é herdado de Funcionario
                ", cpf='" + getCpf() + '\'' + + // CPF é herdado de Funcionario
                '}';
    }
    
    /**
     * Copara este gerente com outro objeto para verificar a igualdade.
     * @param o O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o); // Usa a implementação de Funcionario
    }

    /**
     * Gera um codigo hash para o Gerente.
     * @return  O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return super.hashCode(); // Usa a implementação de Funcionario
    }
}
