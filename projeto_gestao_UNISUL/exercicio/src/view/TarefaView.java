package view;

import dao.ProjetoDAO;
import dao.TarefaDAO;
import dao.UsuarioDAO;
import enums.StatusTarefa;
import model.Projeto;
import model.Tarefa;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TarefaView extends JPanel {
    private JTable tabelaTarefas;
    private DefaultTableModel tabelaModelo;
    private TarefaDAO tarefaDAO;
    private ProjetoDAO projetoDAO;
    private UsuarioDAO usuarioDAO;

    private JTextField txtTitulo, txtDescricao;
    private JComboBox<Projeto> cmbProjeto;
    private JComboBox<Usuario> cmbResponsavel;
    private JComboBox<StatusTarefa> cmbStatus;
    private JFormattedTextField txtDataInicioPrevista, txtDataFimPrevista;
    private JButton btnSalvar, btnNovo, btnExcluir;
    private int idTarefaSelecionada = -1;

    public TarefaView() {
        this.tarefaDAO = new TarefaDAO();
        this.projetoDAO = new ProjetoDAO();
        this.usuarioDAO = new UsuarioDAO();
        setLayout(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Título", "Projeto", "Responsável", "Status"};
        tabelaModelo = new DefaultTableModel(colunas, 0);
        tabelaTarefas = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaTarefas);
        add(scrollPane, BorderLayout.CENTER);

        tabelaTarefas.getSelectionModel().addListSelectionListener(e -> {
            int linhaSelecionada = tabelaTarefas.getSelectedRow();
            if (linhaSelecionada >= 0) {
                idTarefaSelecionada = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
                carregarTarefaParaEdicao(idTarefaSelecionada);
            }
        });

        JPanel formPanel = criarFormulario();
        add(formPanel, BorderLayout.EAST);

        carregarTarefas();
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Cadastro/Edição de Tarefas"), gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtTitulo = new JTextField(20); panel.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtDescricao = new JTextField(20); panel.add(txtDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Projeto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; cmbProjeto = new JComboBox<>(); carregarProjetosCombo(); panel.add(cmbProjeto, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Responsável:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; cmbResponsavel = new JComboBox<>(); carregarResponsaveis(); panel.add(cmbResponsavel, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; cmbStatus = new JComboBox<>(StatusTarefa.values()); panel.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Data Início Prevista (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; txtDataInicioPrevista = new JFormattedTextField(); panel.add(txtDataInicioPrevista, gbc);

        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Data Fim Prevista (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; txtDataFimPrevista = new JFormattedTextField(); panel.add(txtDataFimPrevista, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        btnNovo = new JButton("Novo");
        btnExcluir = new JButton("Excluir");

        btnSalvar.addActionListener(e -> salvarTarefa());
        btnNovo.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirTarefa());

        btnPanel.add(btnSalvar);
        btnPanel.add(btnNovo);
        btnPanel.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        return panel;
    }

    private void carregarProjetosCombo() {
        try {
            List<Projeto> projetos = projetoDAO.listarTodos();
            for (Projeto p : projetos) {
                cmbProjeto.addItem(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarResponsaveis() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            for (Usuario u : usuarios) {
                cmbResponsavel.addItem(u);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTarefas() {
        tabelaModelo.setRowCount(0);
        try {
            List<Tarefa> tarefas = tarefaDAO.listarTodos();
            for (Tarefa t : tarefas) {
                tabelaModelo.addRow(new Object[]{t.getId(), t.getTitulo(), t.getProjeto().getNome(), t.getResponsavel().getNomeCompleto(), t.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tarefas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTarefaParaEdicao(int id) {
        try {
            Tarefa tarefa = tarefaDAO.buscarPorId(id);
            if (tarefa != null) {
                txtTitulo.setText(tarefa.getTitulo());
                txtDescricao.setText(tarefa.getDescricao());
                cmbProjeto.setSelectedItem(tarefa.getProjeto());
                cmbResponsavel.setSelectedItem(tarefa.getResponsavel());
                cmbStatus.setSelectedItem(tarefa.getStatus());
                if (tarefa.getDataInicioPrevista() != null) txtDataInicioPrevista.setText(tarefa.getDataInicioPrevista().toString());
                if (tarefa.getDataTerminoPrevista() != null) txtDataFimPrevista.setText(tarefa.getDataTerminoPrevista().toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados da tarefa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarTarefa() {
        try {
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(txtTitulo.getText());
            tarefa.setDescricao(txtDescricao.getText());
            tarefa.setProjeto((Projeto) cmbProjeto.getSelectedItem());
            tarefa.setResponsavel((Usuario) cmbResponsavel.getSelectedItem());
            tarefa.setStatus((StatusTarefa) cmbStatus.getSelectedItem());
            if (!txtDataInicioPrevista.getText().isEmpty()) tarefa.setDataInicioPrevista(LocalDate.parse(txtDataInicioPrevista.getText()));
            if (!txtDataFimPrevista.getText().isEmpty()) tarefa.setDataTerminoPrevista(LocalDate.parse(txtDataFimPrevista.getText()));

            if (idTarefaSelecionada == -1) {
                tarefaDAO.inserir(tarefa);
                JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!");
            } else {
                tarefa.setId(idTarefaSelecionada);
                tarefaDAO.atualizar(tarefa);
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
            }
            limparFormulario();
            carregarTarefas();
        } catch (SQLException | java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar tarefa. Verifique os dados. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirTarefa() {
        if (idTarefaSelecionada != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta tarefa?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    tarefaDAO.deletar(idTarefaSelecionada);
                    JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso!");
                    limparFormulario();
                    carregarTarefas();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir tarefa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtTitulo.setText("");
        txtDescricao.setText("");
        cmbProjeto.setSelectedIndex(0);
        cmbResponsavel.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtDataInicioPrevista.setText("");
        txtDataFimPrevista.setText("");
        idTarefaSelecionada = -1;
        tabelaTarefas.clearSelection();
    }
}
