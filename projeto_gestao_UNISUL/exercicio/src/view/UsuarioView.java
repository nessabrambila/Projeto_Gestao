package view;

import dao.UsuarioDAO;
import enums.PerfilAcesso;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class UsuarioView extends JPanel {
    private JTable tabelaUsuarios;
    private DefaultTableModel tabelaModelo;
    private JScrollPane scrollPane;
    private UsuarioDAO usuarioDAO;

    // Campos do formulário
    private JTextField txtNome, txtCpf, txtEmail, txtCargo, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<PerfilAcesso> cmbPerfil;
    private JButton btnSalvar, btnNovo, btnExcluir;
    private int idUsuarioSelecionado = -1;

    public UsuarioView() {
        this.usuarioDAO = new UsuarioDAO();
        setLayout(new BorderLayout(10, 10));

        // Inicializa a tabela
        String[] colunas = {"ID", "Nome", "Login", "Perfil"};
        tabelaModelo = new DefaultTableModel(colunas, 0);
        tabelaUsuarios = new JTable(tabelaModelo);
        scrollPane = new JScrollPane(tabelaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        // Evento de clique na tabela
        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            int linhaSelecionada = tabelaUsuarios.getSelectedRow();
            if (linhaSelecionada >= 0) {
                idUsuarioSelecionado = (int) tabelaModelo.getValueAt(linhaSelecionada, 0);
                carregarUsuarioParaEdicao(idUsuarioSelecionado);
            }
        });

        // Inicializa o formulário
        JPanel formPanel = criarFormulario();
        add(formPanel, BorderLayout.EAST);

        // Carrega os dados iniciais
        carregarUsuarios();
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Cadastro/Edição de Usuários"), gbc);

        gbc.gridwidth = 1; // Reseta o gridwidth para 1

        // Nome Completo
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNome = new JTextField(20); panel.add(txtNome, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtCpf = new JTextField(20); panel.add(txtCpf, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; txtEmail = new JTextField(20); panel.add(txtEmail, gbc);

        // Login
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; txtLogin = new JTextField(20); panel.add(txtLogin, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; txtSenha = new JPasswordField(20); panel.add(txtSenha, gbc);

        // Cargo
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; txtCargo = new JTextField(20); panel.add(txtCargo, gbc);

        // Perfil
        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Perfil:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; cmbPerfil = new JComboBox<>(PerfilAcesso.values()); panel.add(cmbPerfil, gbc);

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        btnNovo = new JButton("Novo");
        btnExcluir = new JButton("Excluir");

        btnSalvar.addActionListener(e -> salvarUsuario());
        btnNovo.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirUsuario());

        btnPanel.add(btnSalvar);
        btnPanel.add(btnNovo);
        btnPanel.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        return panel;
    }

    private void carregarUsuarios() {
        tabelaModelo.setRowCount(0); // Limpa a tabela
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            for (Usuario u : usuarios) {
                tabelaModelo.addRow(new Object[]{u.getId(), u.getNomeCompleto(), u.getLogin(), u.getPerfil()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarUsuarioParaEdicao(int id) {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario != null) {
                txtNome.setText(usuario.getNomeCompleto());
                txtCpf.setText(usuario.getCpf());
                txtEmail.setText(usuario.getEmail());
                txtLogin.setText(usuario.getLogin());
                txtSenha.setText(usuario.getSenha()); // Cuidado: Senha em texto plano não é ideal
                txtCargo.setText(usuario.getCargo());
                cmbPerfil.setSelectedItem(usuario.getPerfil());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(txtNome.getText());
        usuario.setCpf(txtCpf.getText());
        usuario.setEmail(txtEmail.getText());
        usuario.setLogin(txtLogin.getText());
        usuario.setSenha(new String(txtSenha.getPassword()));
        usuario.setCargo(txtCargo.getText());
        usuario.setPerfil((PerfilAcesso) cmbPerfil.getSelectedItem());

        try {
            if (idUsuarioSelecionado == -1) {
                // Inserir novo usuário
                usuarioDAO.inserir(usuario);
                JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
            } else {
                // Atualizar usuário existente
                usuario.setId(idUsuarioSelecionado);
                usuarioDAO.atualizar(usuario);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            }
            limparFormulario();
            carregarUsuarios(); // Atualiza a tabela
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirUsuario() {
        if (idUsuarioSelecionado != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este usuário?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    usuarioDAO.deletar(idUsuarioSelecionado);
                    JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                    limparFormulario();
                    carregarUsuarios(); // Atualiza a tabela
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtCargo.setText("");
        cmbPerfil.setSelectedIndex(0);
        idUsuarioSelecionado = -1;
        tabelaUsuarios.clearSelection();
    }
}
