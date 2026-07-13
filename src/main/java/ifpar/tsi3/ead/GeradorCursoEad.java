package ifpar.tsi3.ead;

import ifpar.tsi3.ead.ui.TelaPrincipal;
import javax.swing.SwingUtilities;

public class GeradorCursoEad {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true); // Faz a janela Swing abrir na tela do usuário
        });
    }
}