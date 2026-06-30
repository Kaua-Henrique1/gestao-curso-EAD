package ifpar.tsi3.ead.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Curso {
    private String nome;
    private List<Trilha> trilhas;

    public Curso(String nome) {
        this.nome = nome;
        this.trilhas = new ArrayList<>();
    }

    public void adicionarTrilha(Trilha trilha) {
        this.trilhas.add(trilha);
    }

    public void removerTrilha(int index) {
        if (index >= 0 && index < trilhas.size()) {
            this.trilhas.remove(index);
        }
    }

    // Funcionalidade de Reordenação das Trilhas
    public boolean reordenarTrilha(int indexAtual, int novoIndex) {
        if (indexAtual >= 0 && indexAtual < trilhas.size() && novoIndex >= 0 && novoIndex < trilhas.size()) {
            Collections.swap(trilhas, indexAtual, novoIndex);
            return true;
        }
        return false;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Trilha> getTrilhas() { return trilhas; }

    @Override
    public String toString() {
        return nome;
    }
}
