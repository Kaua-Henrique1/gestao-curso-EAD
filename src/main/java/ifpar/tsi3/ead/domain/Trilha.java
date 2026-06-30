package ifpar.tsi3.ead.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trilha {
    private String nome;
    private List<Modulo> modulos;

    public Trilha(String nome) {
        this.nome = nome;
        this.modulos = new ArrayList<>();
    }

    public void adicionarModulo(Modulo modulo) {
        this.modulos.add(modulo);
    }

    public void removerModulo(int index) {
        if (index >= 0 && index < modulos.size()) {
            this.modulos.remove(index);
        }
    }

    // Funcionalidade de Reordenação dos Módulos
    public boolean reordenarModulo(int indexAtual, int novoIndex) {
        if (indexAtual >= 0 && indexAtual < modulos.size() && novoIndex >= 0 && novoIndex < modulos.size()) {
            Collections.swap(modulos, indexAtual, novoIndex);
            return true;
        }
        return false;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Modulo> getModulos() { return modulos; }

    @Override
    public String toString() {
        return nome;
    }
}
