package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JTextField loginField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private UsuarioDAO usuarioDAO;

    public LoginView() {
        super("Login - Sistema de Gestão de Projetos");
        this.usuarioDAO = new UsuarioDAO();
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        loginField = new JTextField(20);
        senhaField = new JPasswordField(20);
        loginButton = new JButton("Entrar");

        loginButton.addActionListener(e -> {
            String login = loginField.getText();
            String senha = new String(senhaField.getPassword());
            try {
                // TODO: Adicionar um método de autenticação ao UsuarioDAO
                Usuario usuario = usuarioDAO.buscarPorLogin(login);
                if (usuario != null && usuario.getSenha().equals(senha)) {
                    JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                    MainView mainView = new MainView(usuario);
                    mainView.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setupLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Login:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(loginField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(senhaField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
