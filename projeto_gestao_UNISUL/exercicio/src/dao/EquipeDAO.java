package dao;

import model.Equipe;
import model.Usuario;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	public void inserir(Equipe equipe) throws SQLException {
		String sql = "INSERT INTO equipes (nome, descricao) VALUES (?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, equipe.getNome());
			stmt.setString(2, equipe.getDescricao());
			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next())
					equipe.setId(rs.getInt(1));
			}
		}

		
		if (equipe.getMembros() != null) {
			for (Usuario u : equipe.getMembros())
				adicionarMembro(equipe.getId(), u.getId());
		}
	}

	public void atualizar(Equipe equipe) throws SQLException {
		String sql = "UPDATE equipes SET nome=?, descricao=? WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, equipe.getNome());
			stmt.setString(2, equipe.getDescricao());
			stmt.setInt(3, equipe.getId());
			stmt.executeUpdate();
		}

		
		removerTodosMembros(equipe.getId());
		if (equipe.getMembros() != null) {
			for (Usuario u : equipe.getMembros())
				adicionarMembro(equipe.getId(), u.getId());
		}
	}

	public void deletar(int id) throws SQLException {
		removerTodosMembros(id);
		String sql = "DELETE FROM equipes WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	public Equipe buscarPorId(int id) throws SQLException {
		String sql = "SELECT * FROM equipes WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Equipe e = new Equipe();
					e.setId(rs.getInt("id"));
					e.setNome(rs.getString("nome"));
					e.setDescricao(rs.getString("descricao"));
					e.setMembros(buscarMembros(id));
					return e;
				}
			}
		}
		return null;
	}

	public List<Equipe> listarTodos() throws SQLException {
		List<Equipe> equipes = new ArrayList<>();
		String sql = "SELECT * FROM equipes";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next())
				equipes.add(buscarPorId(rs.getInt("id")));
		}
		return equipes;
	}

	private List<Usuario> buscarMembros(int equipeId) throws SQLException {
		List<Usuario> membros = new ArrayList<>();
		String sql = "SELECT usuario_id FROM equipe_usuarios WHERE equipe_id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, equipeId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Usuario u = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
					if (u != null)
						membros.add(u);
				}
			}
		}
		return membros;
	}

	private void adicionarMembro(int equipeId, int usuarioId) throws SQLException {
		String sql = "INSERT INTO equipe_usuarios (equipe_id, usuario_id) VALUES (?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, equipeId);
			stmt.setInt(2, usuarioId);
			stmt.executeUpdate();
		}
	}

	private void removerTodosMembros(int equipeId) throws SQLException {
		String sql = "DELETE FROM equipe_usuarios WHERE equipe_id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, equipeId);
			stmt.executeUpdate();
		}
	}
}
