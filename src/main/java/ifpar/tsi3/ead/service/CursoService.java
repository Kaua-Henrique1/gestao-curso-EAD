package ifpar.tsi3.ead.service;

import ifpar.tsi3.ead.domain.Aula;
import ifpar.tsi3.ead.domain.Curso;
import ifpar.tsi3.ead.domain.Modulo;
import ifpar.tsi3.ead.domain.Trilha;
import ifpar.tsi3.ead.infrastructure.CursoRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class CursoService {

    private Curso cursoAtual;
    private final Gson gson;
    private final CursoRepository repository;

    public CursoService(String nomePadraoCurso) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.repository = new CursoRepository("dados_sistema.json");

        try {
            this.cursoAtual = repository.carregar();
            if (this.cursoAtual == null) {
                this.cursoAtual = new Curso(nomePadraoCurso);
                salvarAlteracoes();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados persistidos. Criando novo curso.");
            this.cursoAtual = new Curso(nomePadraoCurso);
        }
    }

    /**
     * Centraliza o salvamento no repositório.
     * A UI nunca chama o Repository, ela chama as ações do Service e o Service salva!
     */
    private void salvarAlteracoes() {
        try {
            repository.salvar(this.cursoAtual);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no disco: " + e.getMessage());
        }
    }

    public Curso getCursoAtual() {
        return cursoAtual;
    }

    public void setCursoAtual(Curso cursoAtual) {
        this.cursoAtual = cursoAtual;
        salvarAlteracoes();
    }

    // -------------------------------------------------------------------------
    // ADICIONAR (CREATE)
    // -------------------------------------------------------------------------

    public void adicionarTrilha(String nome) {
        cursoAtual.adicionarTrilha(new Trilha(nome));
        salvarAlteracoes();
    }

    public void adicionarModuloNaTrilha(Trilha trilha, String nomeModulo) {
        if (trilha != null) {
            trilha.adicionarModulo(new Modulo(nomeModulo));
            salvarAlteracoes();
        }
    }

    public void adicionarAulaNoModulo(Modulo modulo, String tituloAula, int duracao) {
        if (modulo != null) {
            modulo.adicionarAula(new Aula(tituloAula, duracao));
            salvarAlteracoes();
        }
    }

    // -------------------------------------------------------------------------
    // EDITAR (UPDATE)
    // -------------------------------------------------------------------------

    public void editarNomeCurso(String novoNome) {
        this.cursoAtual.setNome(novoNome);
        salvarAlteracoes();
    }

    public boolean editarTrilha(Trilha trilha, String novoNome) {
        if (trilha != null && novoNome != null && !novoNome.trim().isEmpty()) {
            trilha.setNome(novoNome.trim());
            salvarAlteracoes();
            return true;
        }
        return false;
    }

    public boolean editarModulo(Modulo modulo, String novoNome) {
        if (modulo != null && novoNome != null && !novoNome.trim().isEmpty()) {
            modulo.setNome(novoNome.trim());
            salvarAlteracoes();
            return true;
        }
        return false;
    }

    public boolean editarAula(Aula aula, String novoTitulo, int novaDuracao) {
        if (aula != null && novoTitulo != null && !novoTitulo.trim().isEmpty() && novaDuracao > 0) {
            aula.setTitulo(novoTitulo.trim());
            aula.setDuracaoMinutos(novaDuracao);
            salvarAlteracoes();
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // EXCLUIR (DELETE) - Com efeito cascata
    // -------------------------------------------------------------------------

    public boolean excluirTrilha(Trilha trilha) {
        if (trilha != null && cursoAtual.getTrilhas().remove(trilha)) {
            salvarAlteracoes();
            return true;
        }
        return false;
    }

    public boolean excluirModulo(Trilha trilhaPai, Modulo modulo) {
        if (trilhaPai != null && modulo != null && trilhaPai.getModulos().remove(modulo)) {
            salvarAlteracoes();
            return true;
        }
        return false;
    }

    public boolean excluirAula(Modulo moduloPai, Aula aula) {
        if (moduloPai != null && aula != null && moduloPai.getAulas().remove(aula)) {
            salvarAlteracoes();
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // REORDENAR
    // -------------------------------------------------------------------------

    public boolean reordenarTrilha(Trilha trilha, int direcao) {
        if (trilha == null) return false;
        int indexAtual = cursoAtual.getTrilhas().indexOf(trilha);
        if (indexAtual == -1) return false;

        int novoIndex = indexAtual + direcao;
        boolean sucesso = cursoAtual.reordenarTrilha(indexAtual, novoIndex);
        if (sucesso) salvarAlteracoes();
        return sucesso;
    }

    public boolean reordenarModulo(Trilha trilhaPai, Modulo modulo, int direcao) {
        if (trilhaPai == null || modulo == null) return false;
        int indexAtual = trilhaPai.getModulos().indexOf(modulo);
        if (indexAtual == -1) return false;

        int novoIndex = indexAtual + direcao;
        boolean sucesso = trilhaPai.reordenarModulo(indexAtual, novoIndex);
        if (sucesso) salvarAlteracoes();
        return sucesso;
    }

    public boolean reordenarAula(Modulo moduloPai, Aula aula, int direcao) {
        if (moduloPai == null || aula == null) return false;
        int indexAtual = moduloPai.getAulas().indexOf(aula);
        if (indexAtual == -1) return false;

        int novoIndex = indexAtual + direcao;
        boolean sucesso = moduloPai.reordenarAula(indexAtual, novoIndex);
        if (sucesso) salvarAlteracoes();
        return sucesso;
    }

    // -------------------------------------------------------------------------
    // MÉTODOS DE BUSCA E EXPORTAÇÃO
    // -------------------------------------------------------------------------

    public String buscarConteudoPorNome(String termo) {
        String termoBusca = termo.toLowerCase();
        StringBuilder resultado = new StringBuilder();

        for (Trilha trilha : cursoAtual.getTrilhas()) {
            if (trilha.getNome().toLowerCase().contains(termoBusca)) {
                resultado.append("Trilha encontrada: ").append(trilha.getNome()).append("\n");
            }
            for (Modulo modulo : trilha.getModulos()) {
                if (modulo.getNome().toLowerCase().contains(termoBusca)) {
                    resultado.append("Módulo encontrado: ").append(modulo.getNome())
                            .append(" (Dentro da Trilha: ").append(trilha.getNome()).append(")\n");
                }
                for (Aula aula : modulo.getAulas()) {
                    if (aula.getTitulo().toLowerCase().contains(termoBusca)) {
                        resultado.append("Aula encontrada: ").append(aula.getTitulo())
                                .append(" (Dentro do Módulo: ").append(modulo.getNome()).append(")\n");
                    }
                }
            }
        }

        if (resultado.length() == 0) {
            return "Nenhum resultado encontrado para: " + termo;
        }
        return resultado.toString();
    }

    public void exportarParaJson(String caminhoArquivo) throws IOException {
        String jsonFormatado = gson.toJson(this.cursoAtual);
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(jsonFormatado);
        }
    }

    public void exportarParaTxt(String caminhoArquivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("CURSO: ").append(cursoAtual.getNome()).append("\n");

        for (Trilha trilha : cursoAtual.getTrilhas()) {
            sb.append("  └── [Trilha] ").append(trilha.getNome()).append("\n");

            for (Modulo modulo : trilha.getModulos()) {
                sb.append("      ├── [Módulo] ").append(modulo.getNome()).append("\n");

                for (Aula aula : modulo.getAulas()) {
                    sb.append("          └── [Aula] ").append(aula.getTitulo())
                            .append(" (").append(aula.getDuracaoMinutos()).append(" min)\n");
                }
            }
        }

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(sb.toString());
        }
    }
}