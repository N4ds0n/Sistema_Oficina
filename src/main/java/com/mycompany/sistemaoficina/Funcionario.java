package com.mycompany.sistemaoficina;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects; 

/**
 * Classe que representa os Funcionarios da oficina.
 * Esta é a classe base para todos os tipos de empregados.
 * Contendo informações essenciais como ID, nome e credenciais de acesso.
 * @author santo
 */
public class Funcionario {

    private int id; 
    private String nome;
    private String cpf; 
    private String senha; 

    /**
     * Construtor padrão.
     */
    public Funcionario() {
    }

    /**
     * Construtor completo para criar um novo funcionario com todos os dados
     * Realiza uma limpeza basica (trim) nos campos de CPF e Senha
     * @param id
     * @param nome
     * @param cpf
     * @param senha 
     */
    public Funcionario(int id, String nome, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.cpf = (cpf != null) ? cpf.trim() : null; 
        this.senha = (senha != null) ? senha.trim() : null; 
    }
    
    /**
     * Getters e Setters.
     * @return 
     */
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
     * Salva o objeto Funcionario atual em um arquivo Json individual.
     * O arquivo é nomeado com o ID do funcionario e guardado na pasta "'data/funcionarios".
     * @throws IOException  se ocorrer um erro durante a escrita do arquivo
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

    /**
     * Fornece uma representação textual resumida do Funcionario.
     * @return Uma String formatada com os principais dados do funcionario.
     */
    @Override
    public String toString() {
        return "Funcionario{" +
                "id='" + id + '\'' + 
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }

    /**
     * Metodos para comparação de objetos.
     * Compara dois objetos Funcionario para verificar a igualdade
     * @param o O objeto a ser comparado.
     * @return true se os IDs forem iguais, false caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        // A comparação deve ser baseada no ID para unicidade
        return Objects.equals(id, that.id); 
    }

    /**
     * Gera um codigo hash para o Funcionario, baseado no seu ID.
     * @return O codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }
    }

