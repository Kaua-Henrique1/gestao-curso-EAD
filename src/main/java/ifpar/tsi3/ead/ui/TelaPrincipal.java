package ifpar.tsi3.ead.ui;

import ifpar.tsi3.ead.domain.Aula;
import ifpar.tsi3.ead.domain.Modulo;
import ifpar.tsi3.ead.domain.Trilha;
import ifpar.tsi3.ead.service.CursoService;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.IOException;

public class TelaPrincipal extends JFrame {
    private final CursoService service;
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private final DefaultMutableTreeNode rootNode;

    public TelaPrincipal() {
        // 1. Inicializa o serviço e carrega alguns dados de teste
        service = new CursoService("Gestão de Curso - EAD");

        if (service.getCursoAtual().getTrilhas().isEmpty()) {
            carregarDadosDeTeste();
        }
        // 2. Configurações da Janela Principal (JFrame)
        setTitle("Sistema EAD - Estrutura de Árvore");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 3. Configuração do componente JTree (A Árvore Visual)
        rootNode = new DefaultMutableTreeNode("Carregando...");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hierarquia do Curso"));

        // 4. Painel Inferior com Botões de Ação Organizadinhos
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 10));

        JButton btnAdicionar = new JButton("Adicionar Filho");
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnExcluir = new JButton("Excluir Selecionado");
        JButton btnBuscar = new JButton("Buscar Conteúdo");
        JButton btnSubir = new JButton("Subir (▲)");
        JButton btnDescer = new JButton("Descer (▼)");
        JButton btnExportar = new JButton("Exportar (TXT)");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnSubir);
        painelBotoes.add(btnDescer);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnExportar);
        painelBotoes.add(btnExcluir);

        // 5. Adicionando Ações (Eventos) aos Botões

        // AÇÃO: ADICIONAR DINÂMICO
        btnAdicionar.addActionListener(e -> {
            DefaultMutableTreeNode noSelecionado = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (noSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um item na árvore para adicionar um elemento filho!");
                return;
            }

            Object userObj = noSelecionado.getUserObject();

            // Se for a Raiz (Curso), adiciona Trilha
            if (noSelecionado.isRoot()) {
                String nome = JOptionPane.showInputDialog(this, "Nome da nova Trilha:");
                if (nome != null && !nome.trim().isEmpty()) {
                    service.adicionarTrilha(nome);
                }
                // Se for uma Trilha, adiciona Módulo
            } else if (userObj instanceof Trilha) {
                String nome = JOptionPane.showInputDialog(this, "Nome do novo Módulo:");
                if (nome != null && !nome.trim().isEmpty()) {
                    service.adicionarModuloNaTrilha((Trilha) userObj, nome);
                }
                // Se for un Módulo, adiciona Aula
            } else if (userObj instanceof Modulo) {
                String nome = JOptionPane.showInputDialog(this, "Título da Aula:");
                if (nome != null && !nome.trim().isEmpty()) {
                    String duracaoStr = JOptionPane.showInputDialog(this, "Duração em minutos:");
                    try {
                        int duracao = Integer.parseInt(duracaoStr);
                        service.adicionarAulaNoModulo((Modulo) userObj, nome, duracao);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Duração inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Aulas não podem ter elementos filhos!");
            }
            atualizarArvoreVisual();
        });

        // AÇÃO: EDITAR
        btnEditar.addActionListener(e -> {
            DefaultMutableTreeNode noSelecionado = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (noSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um elemento para editar!");
                return;
            }

            Object userObj = noSelecionado.getUserObject();

            if (noSelecionado.isRoot()) {
                String novoNome = JOptionPane.showInputDialog(this, "Novo nome do Curso:", service.getCursoAtual().getNome());
                if (novoNome != null && !novoNome.trim().isEmpty()) {
                    service.editarNomeCurso(novoNome);
                }
            } else if (userObj instanceof Trilha) {
                Trilha t = (Trilha) userObj;
                String novoNome = JOptionPane.showInputDialog(this, "Novo nome da Trilha:", t.getNome());
                service.editarTrilha(t, novoNome);
            } else if (userObj instanceof Modulo) {
                Modulo m = (Modulo) userObj;
                String novoNome = JOptionPane.showInputDialog(this, "Novo nome do Módulo:", m.getNome());
                service.editarModulo(m, novoNome);
            } else if (userObj instanceof Aula) {
                Aula a = (Aula) userObj;
                String novoTitulo = JOptionPane.showInputDialog(this, "Novo título da Aula:", a.getTitulo());
                if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
                    String novaDuracaoStr = JOptionPane.showInputDialog(this, "Nova duração:", a.getDuracaoMinutos());
                    try {
                        int novaDuracao = Integer.parseInt(novaDuracaoStr);
                        service.editarAula(a, novoTitulo, novaDuracao);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Número inválido!");
                    }
                }
            }
            atualizarArvoreVisual();
        });

        // AÇÃO: EXCLUIR ITEM COM CONFIRMAÇÃO
        btnExcluir.addActionListener(e -> {
            DefaultMutableTreeNode noSelecionado = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (noSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um item na árvore para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (noSelecionado.isRoot()) {
                JOptionPane.showMessageDialog(this, "Não é possível excluir o curso inteiro por aqui.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object userObj = noSelecionado.getUserObject();

            // Pergunta de confirmação
            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir este item?\nAVISO: Se ele possuir filhos (módulos ou aulas), eles também serão deletados para sempre!",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            // Se o usuário clicar em "Sim" (YES)
            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = false;
                DefaultMutableTreeNode noPai = (DefaultMutableTreeNode) noSelecionado.getParent();

                if (userObj instanceof Trilha) {
                    sucesso = service.excluirTrilha((Trilha) userObj);
                } else if (userObj instanceof Modulo) {
                    Trilha trilhaPai = (Trilha) noPai.getUserObject();
                    sucesso = service.excluirModulo(trilhaPai, (Modulo) userObj);
                } else if (userObj instanceof Aula) {
                    Modulo moduloPai = (Modulo) noPai.getUserObject();
                    sucesso = service.excluirAula(moduloPai, (Aula) userObj);
                }

                if (sucesso) {
                    atualizarArvoreVisual();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao tentar excluir o item.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // AÇÃO: REORDENAR PARA CIMA (▲)
        btnSubir.addActionListener(e -> executarReordenacao(-1));

        // AÇÃO: REORDENAR PARA BAIXO (▼)
        btnDescer.addActionListener(e -> executarReordenacao(1));

       btnBuscar.addActionListener(e -> {
            String termo = JOptionPane.showInputDialog(this, "Digite o termo para buscar (Ex: Aula 1):");
            if (termo != null && !termo.trim().isEmpty()) {
                
                // Inicia o percurso recursivo a partir da Raiz
                DefaultMutableTreeNode noEncontrado = buscarNoRecursivo(rootNode, termo.toLowerCase());
                
                if (noEncontrado != null) {
                    // Expande a árvore até o nó encontrado
                    javax.swing.tree.TreePath caminho = new javax.swing.tree.TreePath(noEncontrado.getPath());
                    tree.setSelectionPath(caminho);
                    tree.scrollPathToVisible(caminho);
                    JOptionPane.showMessageDialog(this, "Item encontrado na árvore!", "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum resultado encontrado para: " + termo, "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Ação: Exportar
        btnExportar.addActionListener(e -> {
            try {
                service.exportarParaTxt("estrutura_curso.txt");
                JOptionPane.showMessageDialog(this, "Arquivo TXT exportado com sucesso na pasta do projeto!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 6. Montagem da Tela
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        atualizarArvoreVisual();
    }

    private void executarReordenacao(int direcao) {
        DefaultMutableTreeNode noSelecionado = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (noSelecionado == null || noSelecionado.isRoot()) {
            JOptionPane.showMessageDialog(this, "Selecione uma Trilha, Módulo ou Aula para mover!");
            return;
        }

        Object userObj = noSelecionado.getUserObject();
        boolean sucesso = false;

        if (userObj instanceof Trilha) {
            sucesso = service.reordenarTrilha((Trilha) userObj, direcao);
        } else if (userObj instanceof Modulo) {
            // Descobre o nó da Trilha pai na árvore visual
            DefaultMutableTreeNode noPai = (DefaultMutableTreeNode) noSelecionado.getParent();
            Trilha trilhaPai = (Trilha) noPai.getUserObject();
            sucesso = service.reordenarModulo(trilhaPai, (Modulo) userObj, direcao);
        } else if (userObj instanceof Aula) {
            // Descobre o nó do Módulo pai na árvore visual
            DefaultMutableTreeNode noPai = (DefaultMutableTreeNode) noSelecionado.getParent();
            Modulo moduloPai = (Modulo) noPai.getUserObject();
            sucesso = service.reordenarAula(moduloPai, (Aula) userObj, direcao);
        }

        if (sucesso) {
            atualizarArvoreVisual();
        } else {
            JOptionPane.showMessageDialog(this, "Não é possível mover este elemento para a direção solicitada (limite da lista atingido).");
        }
    }

    private void atualizarArvoreVisual() {
        rootNode.removeAllChildren();
        rootNode.setUserObject(service.getCursoAtual().getNome());

        for (Trilha trilha : service.getCursoAtual().getTrilhas()) {
            // Passamos o objeto 'trilha' inteiro! O Swing usará o toString() dele automaticamente na tela
            DefaultMutableTreeNode noTrilha = new DefaultMutableTreeNode(trilha);

            for (Modulo modulo : trilha.getModulos()) {
                DefaultMutableTreeNode noModulo = new DefaultMutableTreeNode(modulo);

                for (Aula aula : modulo.getAulas()) {
                    DefaultMutableTreeNode noAula = new DefaultMutableTreeNode(aula);
                    noModulo.add(noAula);
                }
                noTrilha.add(noModulo);
            }
            rootNode.add(noTrilha);
        }

        treeModel.reload();

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private void carregarDadosDeTeste() {
        service.adicionarTrilha("Programação Orientada a Objetos");
        Trilha trilhaPoo = service.getCursoAtual().getTrilhas().get(0);
        service.adicionarModuloNaTrilha(trilhaPoo, "Módulo 1: Classes e Objetos");
        Modulo modulo1 = trilhaPoo.getModulos().get(0);
        service.adicionarAulaNoModulo(modulo1, "Aula 1.1: O que são Atributos", 15);
        service.adicionarAulaNoModulo(modulo1, "Aula 1.2: Métodos e Construtores", 25);
    }

    private DefaultMutableTreeNode buscarNoRecursivo(DefaultMutableTreeNode noAtual, String termo) {
        String nomeNo = noAtual.getUserObject().toString().toLowerCase();
        
        if (nomeNo.contains(termo)) {
            return noAtual; 
        }
        
        for (int i = 0; i < noAtual.getChildCount(); i++) {
            DefaultMutableTreeNode filho = (DefaultMutableTreeNode) noAtual.getChildAt(i);
            DefaultMutableTreeNode resultado = buscarNoRecursivo(filho, termo);
            
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }
}