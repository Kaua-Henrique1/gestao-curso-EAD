package ifpar.tsi3.ead;

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
    
    private CursoService service;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;

    public TelaPrincipal() {
        // 1. Inicializa o serviço e carrega alguns dados de teste
        service = new CursoService("Gestão de Curso - EAD");
        carregarDadosDeTeste();

        // 2. Configurações da Janela Principal (JFrame)
        setTitle("Sistema EAD - Estrutura de Árvore");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());

        // 3. Configuração do componente JTree (A Árvore Visual)
        rootNode = new DefaultMutableTreeNode("Carregando...");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        
        // Adiciona uma barra de rolagem caso a árvore fique muito grande
        JScrollPane scrollPane = new JScrollPane(tree); 
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hierarquia do Curso"));

        // 4. Painel Inferior com Botões de Ação
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAdicionarTrilha = new JButton("Adicionar Trilha");
        JButton btnBuscar = new JButton("Buscar Conteúdo");
        JButton btnExportar = new JButton("Exportar (JSON/TXT)");

        painelBotoes.add(btnAdicionarTrilha);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnExportar);

        // 5. Adicionando Ações (Eventos) aos Botões
        
        // Ação: Adicionar Trilha
        btnAdicionarTrilha.addActionListener(e -> {
            String nomeTrilha = JOptionPane.showInputDialog(this, "Digite o nome da nova Trilha:");
            if (nomeTrilha != null && !nomeTrilha.trim().isEmpty()) {
                service.adicionarTrilha(nomeTrilha);
                atualizarArvoreVisual(); // Atualiza a tela após adicionar
            }
        });

        // Ação: Buscar na Árvore
        btnBuscar.addActionListener(e -> {
            String termo = JOptionPane.showInputDialog(this, "Digite o termo para buscar (Ex: Aula 1):");
            if (termo != null && !termo.trim().isEmpty()) {
                String resultado = service.buscarConteudoPorNome(termo);
                JOptionPane.showMessageDialog(this, resultado, "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Ação: Exportar
        btnExportar.addActionListener(e -> {
            try {
                service.exportarParaJson("estrutura_curso.json");
                service.exportarParaTxt("estrutura_curso.txt");
                JOptionPane.showMessageDialog(this, "Arquivos TXT e JSON exportados com sucesso na pasta do projeto!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 6. Montagem da Tela
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Renderiza a árvore com os dados reais
        atualizarArvoreVisual();
    }

    /**
     * Este método traduz a sua estrutura orientada a objetos (Trilha, Modulo, Aula)
     * para os "Nós" visuais que o Java Swing entende (DefaultMutableTreeNode).
     */
    private void atualizarArvoreVisual() {
        rootNode.removeAllChildren(); // Limpa a árvore visual
        rootNode.setUserObject(service.getCursoAtual().getNome()); // Define o nome da raiz

        // Percorre a sua estrutura e cria os nós filhos
        for (Trilha trilha : service.getCursoAtual().getTrilhas()) {
            DefaultMutableTreeNode noTrilha = new DefaultMutableTreeNode("[Trilha] " + trilha.getNome());
            
            for (Modulo modulo : trilha.getModulos()) {
                DefaultMutableTreeNode noModulo = new DefaultMutableTreeNode("[Módulo] " + modulo.getNome());
                
                for (Aula aula : modulo.getAulas()) {
                    DefaultMutableTreeNode noAula = new DefaultMutableTreeNode("[Aula] " + aula.getTitulo() + " (" + aula.getDuracaoMinutos() + " min)");
                    noModulo.add(noAula); // Adiciona a aula no módulo
                }
                noTrilha.add(noModulo); // Adiciona o módulo na trilha
            }
            rootNode.add(noTrilha); // Adiciona a trilha na raiz do curso
        }

        treeModel.reload(); // Atualiza a tela

        // Expande todas as ramificações automaticamente
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

    // Ponto de partida da Aplicação
    public static void main(String[] args) {
        // Garante que a interface gráfica rode na Thread correta do Java
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}