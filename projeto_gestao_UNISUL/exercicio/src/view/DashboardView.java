package view;

import dao.ProjetoDAO;
import dao.TarefaDAO;
import dao.UsuarioDAO;
import model.Projeto;
import model.Tarefa;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DashboardView extends JPanel {
    private ProjetoDAO projetoDAO;
    private TarefaDAO tarefaDAO;
    private UsuarioDAO usuarioDAO;

    public DashboardView() {
        this.projetoDAO = new ProjetoDAO();
        this.tarefaDAO = new TarefaDAO();
        this.usuarioDAO = new UsuarioDAO();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("<html><h1>Dashboard de Projetos</h1></html>"));
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(criarPainelResumoProjetos());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(criarPainelProjetosComAtraso());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(criarPainelDesempenhoColaboradores());
    }

    private JPanel criarPainelResumoProjetos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resumo de Andamento dos Projetos"));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            List<Projeto> projetos = projetoDAO.listarTodos();
            if (projetos.isEmpty()) {
                textArea.setText("Nenhum projeto cadastrado.");
            } else {
                StringBuilder resumo = new StringBuilder();
                for (Projeto p : projetos) {
                    resumo.append("Projeto: ").append(p.getNome()).append("\n");
                    resumo.append("  - Status: ").append(p.getStatus()).append("\n");
                    resumo.append("  - Gerente: ").append(p.getGerenteResponsavel().getNomeCompleto()).append("\n");
                    resumo.append("  - Data Prevista de Término: ").append(p.getDataTerminoPrevisto()).append("\n");
                    resumo.append("--------------------------------------------------\n");
                }
                textArea.setText(resumo.toString());
            }
        } catch (SQLException e) {
            textArea.setText("Erro ao carregar resumo de projetos: " + e.getMessage());
        }

        return panel;
    }

    private JPanel criarPainelProjetosComAtraso() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Projetos com Risco de Atraso"));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            List<Projeto> projetos = projetoDAO.listarTodos();
            StringBuilder atrasos = new StringBuilder();
            LocalDate hoje = LocalDate.now();

            for (Projeto p : projetos) {
                if (p.getStatus() != enums.StatusProjeto.CONCLUIDO && p.getDataTerminoPrevisto() != null && hoje.isAfter(p.getDataTerminoPrevisto())) {
                    atrasos.append("Projeto: ").append(p.getNome()).append("\n");
                    atrasos.append("  - Status: ").append(p.getStatus()).append("\n");
                    atrasos.append("  - Dias de Atraso: ").append(java.time.temporal.ChronoUnit.DAYS.between(p.getDataTerminoPrevisto(), hoje)).append("\n");
                    atrasos.append("--------------------------------------------------\n");
                }
            }
            if (atrasos.length() == 0) {
                textArea.setText("Nenhum projeto com atraso.");
            } else {
                textArea.setText(atrasos.toString());
            }
        } catch (SQLException e) {
            textArea.setText("Erro ao carregar projetos com atraso: " + e.getMessage());
        }

        return panel;
    }

    private JPanel criarPainelDesempenhoColaboradores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Desempenho dos Colaboradores"));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            List<Tarefa> tarefas = tarefaDAO.listarTodos();
            StringBuilder desempenho = new StringBuilder();

            for (Usuario u : usuarios) {
                long tarefasAtribuidas = tarefas.stream().filter(t -> t.getResponsavel() != null && t.getResponsavel().getId() == u.getId()).count();
                long tarefasConcluidas = tarefas.stream().filter(t -> t.getResponsavel() != null && t.getResponsavel().getId() == u.getId() && t.getStatus() == enums.StatusTarefa.CONCLUIDA).count();

                desempenho.append("Colaborador: ").append(u.getNomeCompleto()).append(" (").append(u.getCargo()).append(")\n");
                desempenho.append("  - Tarefas Atribuídas: ").append(tarefasAtribuidas).append("\n");
                desempenho.append("  - Tarefas Concluídas: ").append(tarefasConcluidas).append("\n");
                desempenho.append("--------------------------------------------------\n");
            }
            textArea.setText(desempenho.toString());
        } catch (SQLException e) {
            textArea.setText("Erro ao carregar desempenho dos colaboradores: " + e.getMessage());
        }

        return panel;
    }
}
