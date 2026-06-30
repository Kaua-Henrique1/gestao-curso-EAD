package ifpar.tsi3.ead.service;

import ifpar.tsi3.ead.domain.Aula;
import ifpar.tsi3.ead.domain.Curso;
import ifpar.tsi3.ead.domain.Modulo;
import ifpar.tsi3.ead.domain.Trilha;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}
