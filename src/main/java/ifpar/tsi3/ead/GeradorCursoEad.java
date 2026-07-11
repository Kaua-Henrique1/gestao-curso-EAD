package ifpar.tsi3.ead;

import ifpar.tsi3.ead.ui.TelaPrincipal;
import javax.swing.SwingUtilities;

public class GeradorCursoEad {
    public static void main(String[] args) {
        // Garante que a interface gráfica Swing rode na Thread correta do Java (EDT)
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true); // Faz a janela Swing abrir na tela do usuário
        });
    }
}