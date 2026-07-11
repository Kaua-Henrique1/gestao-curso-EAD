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
            System.out.println("1 - Exibir Hierarquia do Curso");
            System.out.println("2 - Buscar Conteúdo por Nome (Percurso)");
            System.out.println("3 - Adicionar Nova Trilha");
            System.out.println("4 - Remover Trilha");
            System.out.println("5 - Editar Nome do Curso");
            System.out.println("6 - Reordenar Trilhas");
            System.out.println("7 - Exportar Estrutura (JSON / TXT)");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
                switch (opcao) {
                    case 1:
                        System.out.println("\n--- Estrutura Atual ---");
                        service.exportarParaTxt("temp_console.txt");
                        System.out.println(java.nio.file.Files.readString(java.nio.file.Paths.get("temp_console.txt")));
                        break;
                    case 2:
                        System.out.print("\nDigite o termo para buscar na árvore: ");
                        String termo = sc.nextLine();
                        System.out.println("\n--- Resultados da Busca ---");
                        System.out.println(service.buscarConteudoPorNome(termo));
                        break;
                    case 3:
                        System.out.print("\nDigite o nome da nova Trilha: ");
                        String nomeTrilha = sc.nextLine();
                        service.adicionarTrilha(nomeTrilha);
                        System.out.println("Trilha adicionada com sucesso!");
                        break;
                    case 4:
                        System.out.println("\n--- Remover Trilha ---");
                        for (int i = 0; i < service.getCursoAtual().getTrilhas().size(); i++) {
                            System.out.println(i + " - " + service.getCursoAtual().getTrilhas().get(i).getNome());
                        }
                        System.out.print("Digite o índice da trilha a ser removida: ");
                        int indexRemover = Integer.parseInt(sc.nextLine());
                        service.getCursoAtual().removerTrilha(indexRemover);
                        System.out.println("Trilha removida (e todos os seus Módulos/Aulas em cascata).");
                        break;
                    case 5:
                        System.out.println("\nNome atual: " + service.getCursoAtual().getNome());
                        System.out.print("Digite o novo nome para o Curso: ");
                        String novoNome = sc.nextLine();
                        service.editarNomeCurso(novoNome);
                        System.out.println("Nome do curso atualizado!");
                        break;
                    case 6:
                        System.out.println("\n--- Reordenar Trilhas ---");
                        for (int i = 0; i < service.getCursoAtual().getTrilhas().size(); i++) {
                            System.out.println(i + " - " + service.getCursoAtual().getTrilhas().get(i).getNome());
                        }
                        System.out.print("Digite o índice atual da trilha: ");
                        int indexAtual = Integer.parseInt(sc.nextLine());
                        System.out.print("Digite o novo índice de destino: ");
                        int novoIndex = Integer.parseInt(sc.nextLine());

                        if (service.getCursoAtual().reordenarTrilha(indexAtual, novoIndex)) {
                            System.out.println("Ordem atualizada com sucesso!");
                        } else {
                            System.out.println("Falha na reordenação. Verifique os índices.");
                        }
                        break;
                    case 7:
                        service.exportarParaJson("estrutura_curso.json");
                        service.exportarParaTxt("estrutura_curso.txt");
                        System.out.println("\n[SUCESSO] Arquivos JSON e TXT gerados na raiz do projeto!");
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            } catch (IOException e) {
                System.out.println("Erro ao gerar visualização: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Erro: Índice inválido selecionado.");
            }
        }
        sc.close();
    }
}