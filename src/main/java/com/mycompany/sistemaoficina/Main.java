package com.mycompany.sistemaoficina;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Cria uma unica instancia de SistemaOficina 
        Sistemaoficina sistema = new Sistemaoficina(); 

        // Logica para cadastro inicial do gerente
        if (!sistema.existeGerenteCadastrado()) { 
            System.out.println("Nenhum gerente cadastrado. Cadastre o gerente inicial:");
            // CORRECAO: Chama o metodo publico de SistemaOficina
            sistema.cadastrarGerenteInicial(scanner); 
        }

        System.out.println("==== Login no Sistema ====");
        System.out.print("CPF: ");
        String cpfLogin = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senhaLogin = scanner.nextLine().trim();

        // Tenta autenticar como gerente
        Gerente gerente = sistema.autenticarGerente(cpfLogin, senhaLogin); 
        if (gerente != null) {
            System.out.println("Login gerente realizado com sucesso!");
            sistema.iniciar(gerente); 
            return; 
        }

        // Tenta autenticar como funcionario
        Funcionario funcionarioLogado = sistema.autenticarFuncionario(cpfLogin, senhaLogin); 
        if (funcionarioLogado != null) {
            sistema.iniciarSomenteComPermissoesFuncionario(funcionarioLogado);
            return; 
        }

        System.out.println("CPF ou senha incorretos. Encerrando o sistema.");
        scanner.close(); 
    }
}
