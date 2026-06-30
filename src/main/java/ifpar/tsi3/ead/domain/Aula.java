package ifpar.tsi3.ead.domain;

public class Aula {
    private String titulo;
    private int duracaoMinutos;

    public Aula(String titulo, int duracaoMinutos) {
        this.titulo = titulo;
        this.duracaoMinutos = duracaoMinutos;
    }

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    @Override
    public String toString() {
        return titulo + " (" + duracaoMinutos + " min)";
    }
}
