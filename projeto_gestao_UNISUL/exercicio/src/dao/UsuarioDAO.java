package dao;

import model.Usuario;
import enums.PerfilAcesso;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

	public void inserir(Usuario usuario) throws SQLException {
		String sql = "INSERT INTO usuarios (nome_completo, cpf, email, senha, perfil, cargo, login) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, usuario.getNomeCompleto());
			stmt.setString(2, usuario.getCpf());
			stmt.setString(3, usuario.getEmail());
			stmt.setString(4, usuario.getSenha());
			stmt.setString(5, usuario.getPerfil().name().toLowerCase());
			stmt.setString(6, usuario.getCargo());
			stmt.setString(7, usuario.getLogin());

			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					usuario.setId(rs.getInt(1));
				}
			}
		}
	}

	public void atualizar(Usuario usuario) throws SQLException {
		String sql = "UPDATE usuarios SET nome_completo=?, cpf=?, email=?, senha=?, perfil=?, cargo=?, login=? WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, usuario.getNomeCompleto());
			stmt.setString(2, usuario.getCpf());
			stmt.setString(3, usuario.getEmail());
			stmt.setString(4, usuario.getSenha());
			stmt.setString(5, usuario.getPerfil().name().toLowerCase());
			stmt.setString(6, usuario.getCargo());
			stmt.setString(7, usuario.getLogin());
			stmt.setInt(8, usuario.getId());

			stmt.executeUpdate();
		}
	}

	public void deletar(int id) throws SQLException {
		String sql = "DELETE FROM usuarios WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	public Usuario buscarPorId(int id) throws SQLException {
		String sql = "SELECT * FROM usuarios WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Usuario u = new Usuario();
					u.setId(rs.getInt("id"));
					u.setNomeCompleto(rs.getString("nome_completo"));
					u.setCpf(rs.getString("cpf"));
					u.setEmail(rs.getString("email"));
					u.setSenha(rs.getString("senha"));
					u.setPerfil(PerfilAcesso.valueOf(rs.getString("perfil").toUpperCase()));
					u.setCargo(rs.getString("cargo"));
					u.setLogin(rs.getString("login"));
					return u;
				}
			}
		}
		return null;
	}

	public List<Usuario> listarTodos() throws SQLException {
		List<Usuario> usuarios = new ArrayList<>();
		String sql = "SELECT * FROM usuarios";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Usuario u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNomeCompleto(rs.getString("nome_completo"));
				u.setCpf(rs.getString("cpf"));
				u.setEmail(rs.getString("email"));
				u.setSenha(rs.getString("senha"));
				u.setPerfil(PerfilAcesso.valueOf(rs.getString("perfil").toUpperCase()));
				u.setCargo(rs.getString("cargo"));
				u.setLogin(rs.getString("login"));
				usuarios.add(u);
			}
		}
		return usuarios;
	}
}
