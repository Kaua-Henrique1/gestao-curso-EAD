package ifpar.tsi3.ead;


import ifpar.tsi3.ead.domain.Modulo;
import ifpar.tsi3.ead.domain.Trilha;
import ifpar.tsi3.ead.service.CursoService;

import java.io.IOException;
import java.util.Scanner;

public class MainConsole {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Inicializando Sistema EAD ---");
        System.out.print("Digite o nome do Curso Superior/Trilha Principal: ");
        String nomeCurso = sc.nextLine();

        // Inicializa o serviço com a raiz da árvore
        CursoService service = new CursoService(nomeCurso);

        // Criando uma estrutura fake rápida para testar o comportamento
        System.out.println("\n[Sistema] Criando estrutura padrão de testes...");

        // 1. Adiciona Trilha
        service.adicionarTrilha("Programação Orientada a Objetos");
        Trilha trilhaPoo = service.getCursoAtual().getTrilhas().get(0);

        // 2. Adiciona Módulo na Trilha
        service.adicionarModuloNaTrilha(trilhaPoo, "Módulo 1: Classes e Objetos");
        Modulo modulo1 = trilhaPoo.getModulos().get(0);

        // 3. Adiciona Aulas no Módulo
        service.adicionarAulaNoModulo(modulo1, "Aula 1.1: O que são Atributos", 15);
        service.adicionarAulaNoModulo(modulo1, "Aula 1.2: Métodos e Construtores", 25);

        // Menu de operações
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n====== MENU DE OPERAÇÕES ======");
            System.out.println("1 - Exportar Estrutura Completa para JSON");
            System.out.println("2 - Mostrar nome do Curso Atual");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
                switch (opcao) {
                    case 1:
                        String nomeArquivo = "estrutura_curso.json";
                        service.exportarParaJson(nomeArquivo);
                        System.out.println("\n[SUCESSO] Arquivo '" + nomeArquivo + "' gerado na raiz do projeto!");
                        System.out.println("Abra o arquivo no IntelliJ para ver a hierarquia linda em JSON.");
                        break;
                    case 2:
                        System.out.println("\nCurso: " + service.getCursoAtual().getNome());
                        break;
                    case 0:
                        System.out.println("Encerrando testador console...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            } catch (IOException e) {
                System.out.println("Erro ao salvar o arquivo JSON: " + e.getMessage());
            }
        }
        sc.close();
    }
}