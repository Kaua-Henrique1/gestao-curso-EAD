package ifpar.tsi3.ead.service;

import ifpar.tsi3.ead.domain.Aula;
import ifpar.tsi3.ead.domain.Curso;
import ifpar.tsi3.ead.domain.Modulo;
import ifpar.tsi3.ead.domain.Trilha;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class CursoService {

    private Curso cursoAtual;
    private final Gson gson;

    public CursoService(String nomeDoCurso) {
        this.cursoAtual = new Curso(nomeDoCurso);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Curso getCursoAtual() {
        return cursoAtual;
    }

    public void setCursoAtual(Curso cursoAtual) {
        this.cursoAtual = cursoAtual;
    }


    /**
     * Exporta a árvore completa do curso para um arquivo .json usando o Gson.
     */
    public void exportarParaJson(String caminhoArquivo) throws IOException {
        String jsonFormatado = gson.toJson(this.cursoAtual);
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(jsonFormatado);
        }
    }

    /**
     * Exporta a estrutura em formato TXT com recuos (tabulações) hierárquicas.
     */
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

    // -------------------------------------------------------------------------
    // MÉTODOS AUXILIARES PARA FACILITAR A INTERFACE (MÉTODOS "CORREIO")
    // -------------------------------------------------------------------------

    public void adicionarTrilha(String nome) {
        cursoAtual.adicionarTrilha(new Trilha(nome));
    }

    public void adicionarModuloNaTrilha(Trilha trilha, String nomeModulo) {
        if (trilha != null) {
            trilha.adicionarModulo(new Modulo(nomeModulo));
        }
    }

    public void adicionarAulaNoModulo(Modulo modulo, String tituloAula, int duracao) {
        if (modulo != null) {
            modulo.adicionarAula(new Aula(tituloAula, duracao));
        }
    }
    
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
                             .append(" (Dentro da Trilha: ").append(trilha.getNome()).append("\n");
                }
                for (Aula aula : modulo.getAulas()) {
                    if (aula.getTitulo().toLowerCase().contains(termoBusca)) {
                        resultado.append("Aula encontrada: ").append(aula.getTitulo())
                                 .append(" (Dentro do Módulo: ").append(modulo.getNome()).append("\n");
                    }
                }
            }
        }

        if (resultado.length() == 0) {
            return "Nenhum resultado encontrado para: " + termo;
        }
        return resultado.toString();
    }

    public void editarNomeCurso(String novoNome) {
        this.cursoAtual.setNome(novoNome);
    }

    // -------------------------------------------------------------------------
    // FUNCIONALIDADES: EDIÇÃO DE NÓS
    // -------------------------------------------------------------------------

    public boolean editarTrilha(Trilha trilha, String novoNome) {
        if (trilha != null && novoNome != null && !novoNome.trim().isEmpty()) {
            trilha.setNome(novoNome.trim());
            return true;
        }
        return false;
    }

    public boolean editarModulo(Modulo modulo, String novoNome) {
        if (modulo != null && novoNome != null && !novoNome.trim().isEmpty()) {
            modulo.setNome(novoNome.trim());
            return true;
        }
        return false;
    }

    public boolean editarAula(Aula aula, String novoTitulo, int novaDuracao) {
        if (aula != null && novoTitulo != null && !novoTitulo.trim().isEmpty() && novaDuracao > 0) {
            aula.setTitulo(novoTitulo.trim());
            aula.setDuracaoMinutos(novaDuracao);
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // FUNCIONALIDADES: REORDENAÇÃO (SUBIR / DESCER)
    // -------------------------------------------------------------------------

    public boolean reordenarTrilha(Trilha trilha, int direcao) {
        if (trilha == null) return false;
        int indexAtual = cursoAtual.getTrilhas().indexOf(trilha);
        if (indexAtual == -1) return false; // Trilha não encontrada

        int novoIndex = indexAtual + direcao;
        return cursoAtual.reordenarTrilha(indexAtual, novoIndex);
    }

    public boolean reordenarModulo(Trilha trilhaPai, Modulo modulo, int direcao) {
        if (trilhaPai == null || modulo == null) return false;
        int indexAtual = trilhaPai.getModulos().indexOf(modulo);
        if (indexAtual == -1) return false;

        int novoIndex = indexAtual + direcao;
        return trilhaPai.reordenarModulo(indexAtual, novoIndex);
    }

    public boolean reordenarAula(Modulo moduloPai, Aula aula, int direcao) {
        if (moduloPai == null || aula == null) return false;
        int indexAtual = moduloPai.getAulas().indexOf(aula);
        if (indexAtual == -1) return false;

        int novoIndex = indexAtual + direcao;
        return moduloPai.reordenarAula(indexAtual, novoIndex);
    }
}
