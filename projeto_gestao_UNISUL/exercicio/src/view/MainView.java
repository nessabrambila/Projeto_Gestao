package view;

import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private Usuario usuarioLogado;

    public MainView(Usuario usuarioLogado) {
        super("Sistema de Gestão de Projetos");
        this.usuarioLogado = usuarioLogado;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Projetos", new ProjetoView());
        tabbedPane.addTab("Tarefas", new TarefaView());

        // Lógica para mostrar as abas de acordo com o perfil
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
                // Colaboradores podem não ter acesso a gerenciar usuários e equipes
                break;
        }

        add(tabbedPane, BorderLayout.CENTER);
    }
}
