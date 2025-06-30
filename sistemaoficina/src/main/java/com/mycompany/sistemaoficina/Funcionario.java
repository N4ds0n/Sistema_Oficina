package com.mycompany.sistemaoficina;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects; 

public class Funcionario {

    private int id; 
    private String nome;
    private String cpf; 
    private String senha; 

    // Construtor padrão: Gera um ID único automaticamente. 
    public Funcionario() {
    }

    // Construtor para criar um novo funcionário 
    public Funcionario(int id, String nome, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.cpf = (cpf != null) ? cpf.trim() : null; 
        this.senha = (senha != null) ? senha.trim() : null; 
    }
    
    // --- Getters e Setters ---
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return (cpf != null) ? cpf.trim() : null;
    }
    public void setCpf(String cpf) {
        this.cpf = (cpf != null) ? cpf.trim() : null;
    }

    public String getSenha() {
        return (senha != null) ? senha.trim() : null;
    }
    public void setSenha(String senha) {
        this.senha = (senha != null) ? senha.trim() : null;
    }

    /**
     * Salva o objeto Funcionario em um arquivo JSON dentro da pasta "data/funcionarios"..
     */
    public void salvarComoJson() throws IOException {
        String dirPath = "data/funcionarios";
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdirs(); // Cria o diretório se não existir
        }

        // O nome do arquivo será o ID do funcionário, seguido por .json
        File arquivo = new File(dir, this.id + ".json"); 

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(arquivo)) {
            gson.toJson(this, writer);
            System.out.println("Funcionário " + this.nome + " salvo com sucesso em " + arquivo.getAbsolutePath());
        } 
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id='" + id + '\'' + 
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }

    // --- Métodos para comparação de objetos (essencial para listas) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        // A comparação deve ser baseada no ID para unicidade
        return Objects.equals(id, that.id); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }
    }

