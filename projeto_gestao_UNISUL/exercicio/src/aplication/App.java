package aplication;

import view.LoginView;
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        // Usa SwingUtilities.invokeLater para garantir que a GUI seja criada na thread de despacho de eventos.
        // É uma prática recomendada para aplicações Swing.
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}