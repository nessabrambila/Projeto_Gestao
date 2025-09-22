package view;

import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private Usuario usuarioLogado;

    public MainView(Usuario usuarioLogado) {
        super("Sistema de Gestão de Projetos");
        this.usuarioLogado = usuarioLogado;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // Aumenta o tamanho da tela para acomodar os relatórios
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dashboard", new DashboardView());

        tabbedPane.addTab("Projetos", new ProjetoView());
        tabbedPane.addTab("Tarefas", new TarefaView());

        switch (usuarioLogado.getPerfil()) {
            case ADMINISTRADOR:
                tabbedPane.addTab("Usuários", new UsuarioView());
                tabbedPane.addTab("Equipes", new EquipeView());
                break;
            case GERENTE:
                tabbedPane.addTab("Usuários", new UsuarioView());
                tabbedPane.addTab("Equipes", new EquipeView());
                break;
            case COLABORADOR:
                break;
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
