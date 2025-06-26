package com.mycompany.sistemaoficina;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Gerente extends Funcionario {

    // Gerente não precisa mais de nome, cpf, senha como atributos próprios,
    // pois eles são herdados de Funcionario.

    // Construtor padrão
    public Gerente() {
        super(); // Chama o construtor padrão de Funcionario
    }

    // Construtor para criar um novo Gerente.
    public Gerente(int id, String nome, String cpf, String senha) {
        super(id, nome, cpf, senha); // Chama o construtor de Funcionario com ID
    }

    // Construtor para criar um novo Gerente, onde o ID é gerado externamente
    public Gerente(String nome, String cpf, String senha) {
        super(0, nome, cpf, senha); // ID 0 temporário, será atualizado pelo AuthService
    }


    // Getters e setters para nome, cpf, senha não são mais necessários aqui,
    // pois são herdados de Funcionario.

    /**
     * Salva o objeto Gerente em um arquivo JSON
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

    @Override
    public String toString() {
        // Chama o toString da superclasse Funcionario e adiciona informações específicas de Gerente, se houver.
        return "Gerente{" +
                "id=" + getId() + // ID é herdado de Funcionario
                ", nome='" + getNome() + '\'' + // Nome é herdado de Funcionario
                ", cpf='" + getCpf() + '\'' + + // CPF é herdado de Funcionario
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        return super.equals(o); // Usa a implementação de Funcionario
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // Usa a implementação de Funcionario
    }
}
