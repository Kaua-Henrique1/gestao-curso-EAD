package ifpar.tsi3.ead.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Modulo {
    private String nome;
    private List<Aula> aulas;

    public Modulo(String nome) {
        this.nome = nome;
        this.aulas = new ArrayList<>();
    }

    public void adicionarAula(Aula aula) {
        this.aulas.add(aula);
    }

    public void removerAula(int index) {
        if (index >= 0 && index < aulas.size()) {
            this.aulas.remove(index);
        }
    }

    // Funcionalidade de Reordenação: Move a aula para cima ou para baixo na lista
    public boolean reordenarAula(int indexAtual, int novoIndex) {
        if (indexAtual >= 0 && indexAtual < aulas.size() && novoIndex >= 0 && novoIndex < aulas.size()) {
            Collections.swap(aulas, indexAtual, novoIndex);
            return true;
        }
        return false;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Aula> getAulas() { return aulas; }

    @Override
    public String toString() {
        return nome + " [" + aulas.size() + " aula(s)]";
    }
}
