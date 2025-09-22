package view;

import dao.EquipeDAO;
import dao.UsuarioDAO;
import model.Equipe;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeView extends JPanel {
    private JTable tabelaEquipes;
    private DefaultTableModel tabelaModelo;
    private EquipeDAO equipeDAO;
    private UsuarioDAO usuarioDAO;

    // Campos do formulário
    private JTextField txtNome, txtDescricao;
    private JList<Usuario> listMembrosDisponiveis;
    private JList<Usuario> listMembrosEquipe;
    private DefaultListModel<Usuario> modeloMembrosDisponiveis;
    private DefaultListModel<Usuario> modeloMembrosEquipe;
    private JButton btnAdicionarMembro, btnRemoverMembro;
    private JButton btnSalvar, btnNovo, btnExcluir;
    private int idEquipeSelecionada = -1;

    public EquipeView() {
        this.equipeDAO = new EquipeDAO();
        this.usuarioDAO = new UsuarioDAO();
        setLayout(new BorderLayout(10, 10));

        // Tabela de Equipes
        String[] colunas = {"ID", "Nome", "Descrição"};
        tabelaModelo = new DefaultTableModel(colunas, 0);
        tabelaEquipes = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaEquipes);
        add(scrollPane, BorderLayout.CENTER);

        tabelaEquipes.getSelectionModel().addListSelectionListener(e -> {
            int linhaSelecionada = tabelaEquipes.getSelectedRow();
            if (linhaSelecionada >= 0) {
                idEquipeSelecionada = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
                carregarEquipeParaEdicao(idEquipeSelecionada);
            }
        });

        JPanel formPanel = criarFormulario();
        add(formPanel, BorderLayout.EAST);

        carregarEquipes();
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Cadastro/Edição de Equipes"), gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNome = new JTextField(20); panel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtDescricao = new JTextField(20); panel.add(txtDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(new JLabel("Gerenciar Membros"), gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; panel.add(new JLabel("Disponíveis"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(new JLabel("Membros da Equipe"), gbc);

        modeloMembrosDisponiveis = new DefaultListModel<>();
        listMembrosDisponiveis = new JList<>(modeloMembrosDisponiveis);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JScrollPane(listMembrosDisponiveis), gbc);

        modeloMembrosEquipe = new DefaultListModel<>();
        listMembrosEquipe = new JList<>(modeloMembrosEquipe);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(new JScrollPane(listMembrosEquipe), gbc);

        JPanel btnMembrosPanel = new JPanel();
        btnAdicionarMembro = new JButton("->");
        btnRemoverMembro = new JButton("<-");
        btnMembrosPanel.add(btnAdicionarMembro);
        btnMembrosPanel.add(btnRemoverMembro);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; panel.add(btnMembrosPanel, gbc);

        btnAdicionarMembro.addActionListener(e -> adicionarMembro());
        btnRemoverMembro.addActionListener(e -> removerMembro());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        btnNovo = new JButton("Novo");
        btnExcluir = new JButton("Excluir");

        btnSalvar.addActionListener(e -> salvarEquipe());
        btnNovo.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirEquipe());

        btnPanel.add(btnSalvar);
        btnPanel.add(btnNovo);
        btnPanel.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        carregarUsuariosDisponiveis();

        return panel;
    }

    private void carregarUsuariosDisponiveis() {
        modeloMembrosDisponiveis.clear();
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            for (Usuario u : usuarios) {
                modeloMembrosDisponiveis.addElement(u);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarMembro() {
        List<Usuario> selecionados = listMembrosDisponiveis.getSelectedValuesList();
        for (Usuario u : selecionados) {
            modeloMembrosEquipe.addElement(u);
            modeloMembrosDisponiveis.removeElement(u);
        }
    }

    private void removerMembro() {
        List<Usuario> selecionados = listMembrosEquipe.getSelectedValuesList();
        for (Usuario u : selecionados) {
            modeloMembrosDisponiveis.addElement(u);
            modeloMembrosEquipe.removeElement(u);
        }
    }

    private void carregarEquipes() {
        tabelaModelo.setRowCount(0);
        try {
            List<Equipe> equipes = equipeDAO.listarTodos();
            for (Equipe e : equipes) {
                tabelaModelo.addRow(new Object[]{e.getId(), e.getNome(), e.getDescricao()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarEquipeParaEdicao(int id) {
        try {
            Equipe equipe = equipeDAO.buscarPorId(id);
            if (equipe != null) {
                txtNome.setText(equipe.getNome());
                txtDescricao.setText(equipe.getDescricao());
                modeloMembrosDisponiveis.clear();
                modeloMembrosEquipe.clear();
                List<Usuario> todosUsuarios = usuarioDAO.listarTodos();
                List<Usuario> membrosEquipe = equipe.getMembros();

                for (Usuario u : todosUsuarios) {
                    if (membrosEquipe != null && membrosEquipe.stream().anyMatch(m -> m.getId() == u.getId())) {
                        modeloMembrosEquipe.addElement(u);
                    } else {
                        modeloMembrosDisponiveis.addElement(u);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados da equipe: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarEquipe() {
        try {
            Equipe equipe = new Equipe();
            equipe.setNome(txtNome.getText());
            equipe.setDescricao(txtDescricao.getText());

            List<Usuario> membros = new ArrayList<>();
            for (int i = 0; i < modeloMembrosEquipe.getSize(); i++) {
                membros.add(modeloMembrosEquipe.getElementAt(i));
            }
            equipe.setMembros(membros);

            if (idEquipeSelecionada == -1) {
                equipeDAO.inserir(equipe);
                JOptionPane.showMessageDialog(this, "Equipe salva com sucesso!");
            } else {
                equipe.setId(idEquipeSelecionada);
                equipeDAO.atualizar(equipe);
                JOptionPane.showMessageDialog(this, "Equipe atualizada com sucesso!");
            }
            limparFormulario();
            carregarEquipes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar equipe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirEquipe() {
        if (idEquipeSelecionada != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta equipe?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    equipeDAO.deletar(idEquipeSelecionada);
                    JOptionPane.showMessageDialog(this, "Equipe excluída com sucesso!");
                    limparFormulario();
                    carregarEquipes();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir equipe: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        idEquipeSelecionada = -1;
        tabelaEquipes.clearSelection();
        carregarUsuariosDisponiveis();
        modeloMembrosEquipe.clear();
    }
}
