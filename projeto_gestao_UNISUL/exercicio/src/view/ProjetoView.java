package view;

import dao.ProjetoDAO;
import dao.UsuarioDAO;
import enums.StatusProjeto;
import model.Projeto;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProjetoView extends JPanel {
    private JTable tabelaProjetos;
    private DefaultTableModel tabelaModelo;
    private ProjetoDAO projetoDAO;
    private UsuarioDAO usuarioDAO;

    // Campos do formulário
    private JTextField txtNome, txtDescricao;
    private JFormattedTextField txtDataInicio, txtDataFimPrevisto;
    private JComboBox<StatusProjeto> cmbStatus;
    private JComboBox<Usuario> cmbGerente;
    private JButton btnSalvar, btnNovo, btnExcluir;
    private int idProjetoSelecionado = -1;

    public ProjetoView() {
        this.projetoDAO = new ProjetoDAO();
        this.usuarioDAO = new UsuarioDAO();
        setLayout(new BorderLayout(10, 10));

        // Tabela de Projetos
        String[] colunas = {"ID", "Nome", "Gerente", "Status"};
        tabelaModelo = new DefaultTableModel(colunas, 0);
        tabelaProjetos = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaProjetos);
        add(scrollPane, BorderLayout.CENTER);

        // Evento de seleção da tabela
        tabelaProjetos.getSelectionModel().addListSelectionListener(e -> {
            int linhaSelecionada = tabelaProjetos.getSelectedRow();
            if (linhaSelecionada >= 0) {
                idProjetoSelecionado = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
                carregarProjetoParaEdicao(idProjetoSelecionado);
            }
        });

        // Formulário de Projetos
        JPanel formPanel = criarFormulario();
        add(formPanel, BorderLayout.EAST);

        carregarProjetos();
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Cadastro/Edição de Projetos"), gbc);

        gbc.gridwidth = 1;

        // Nome
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNome = new JTextField(20); panel.add(txtNome, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtDescricao = new JTextField(20); panel.add(txtDescricao, gbc);

        // Data Início
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Data Início (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; txtDataInicio = new JFormattedTextField(); panel.add(txtDataInicio, gbc);

        // Data Fim Previsto
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Data Fim Previsto (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; txtDataFimPrevisto = new JFormattedTextField(); panel.add(txtDataFimPrevisto, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; cmbStatus = new JComboBox<>(StatusProjeto.values()); panel.add(cmbStatus, gbc);

        // Gerente Responsável
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Gerente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; cmbGerente = new JComboBox<>(); carregarGerentes(); panel.add(cmbGerente, gbc);

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        btnNovo = new JButton("Novo");
        btnExcluir = new JButton("Excluir");

        btnSalvar.addActionListener(e -> salvarProjeto());
        btnNovo.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirProjeto());

        btnPanel.add(btnSalvar);
        btnPanel.add(btnNovo);
        btnPanel.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        return panel;
    }

    private void carregarGerentes() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            for (Usuario u : usuarios) {
                // Apenas gerentes podem ser selecionados
                if (u.getPerfil().name().equals("GERENTE")) {
                    cmbGerente.addItem(u);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gerentes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProjetos() {
        tabelaModelo.setRowCount(0);
        try {
            List<Projeto> projetos = projetoDAO.listarTodos();
            for (Projeto p : projetos) {
                tabelaModelo.addRow(new Object[]{p.getId(), p.getNome(), p.getGerenteResponsavel().getNomeCompleto(), p.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProjetoParaEdicao(int id) {
        try {
            Projeto projeto = projetoDAO.buscarPorId(id);
            if (projeto != null) {
                txtNome.setText(projeto.getNome());
                txtDescricao.setText(projeto.getDescricao());
                txtDataInicio.setText(projeto.getDataInicio().toString());
                txtDataFimPrevisto.setText(projeto.getDataTerminoPrevisto().toString());
                cmbStatus.setSelectedItem(projeto.getStatus());
                cmbGerente.setSelectedItem(projeto.getGerenteResponsavel());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do projeto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarProjeto() {
        try {
            Projeto projeto = new Projeto();
            projeto.setNome(txtNome.getText());
            projeto.setDescricao(txtDescricao.getText());
            projeto.setDataInicio(LocalDate.parse(txtDataInicio.getText()));
            projeto.setDataTerminoPrevisto(LocalDate.parse(txtDataFimPrevisto.getText()));
            projeto.setStatus((StatusProjeto) cmbStatus.getSelectedItem());
            projeto.setGerenteResponsavel((Usuario) cmbGerente.getSelectedItem());

            if (idProjetoSelecionado == -1) {
                projetoDAO.inserir(projeto);
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!");
            } else {
                projeto.setId(idProjetoSelecionado);
                projetoDAO.atualizar(projeto);
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
            }
            limparFormulario();
            carregarProjetos();
        } catch (SQLException | java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar projeto. Verifique os dados. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProjeto() {
        if (idProjetoSelecionado != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este projeto?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    projetoDAO.deletar(idProjetoSelecionado);
                    JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso!");
                    limparFormulario();
                    carregarProjetos();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir projeto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um projeto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtDataInicio.setText("");
        txtDataFimPrevisto.setText("");
        cmbStatus.setSelectedIndex(0);
        cmbGerente.setSelectedIndex(0);
        idProjetoSelecionado = -1;
        tabelaProjetos.clearSelection();
    }
}
