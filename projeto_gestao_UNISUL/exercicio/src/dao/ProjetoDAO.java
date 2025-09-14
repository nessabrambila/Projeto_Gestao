package dao;

import model.Projeto;
import model.Usuario;
import enums.StatusProjeto;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	public void inserir(Projeto projeto) throws SQLException {
		String sql = "INSERT INTO projetos (nome, descricao, data_inicio, data_termino_previsto, status, gerente_id) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, projeto.getNome());
			stmt.setString(2, projeto.getDescricao());
			stmt.setDate(3, Date.valueOf(projeto.getDataInicio()));
			stmt.setDate(4, Date.valueOf(projeto.getDataTerminoPrevisto()));
			stmt.setString(5, projeto.getStatus().name().toLowerCase());
			stmt.setInt(6, projeto.getGerenteResponsavel().getId());

			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next())
					projeto.setId(rs.getInt(1));
			}
		}
	}

	public void atualizar(Projeto projeto) throws SQLException {
		String sql = "UPDATE projetos SET nome=?, descricao=?, data_inicio=?, data_termino_previsto=?, status=?, gerente_id=? WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, projeto.getNome());
			stmt.setString(2, projeto.getDescricao());
			stmt.setDate(3, Date.valueOf(projeto.getDataInicio()));
			stmt.setDate(4, Date.valueOf(projeto.getDataTerminoPrevisto()));
			stmt.setString(5, projeto.getStatus().name().toLowerCase());
			stmt.setInt(6, projeto.getGerenteResponsavel().getId());
			stmt.setInt(7, projeto.getId());

			stmt.executeUpdate();
		}
	}

	public void deletar(int id) throws SQLException {
		String sql = "DELETE FROM projetos WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	public Projeto buscarPorId(int id) throws SQLException {
		String sql = "SELECT * FROM projetos WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Projeto p = new Projeto();
					p.setId(rs.getInt("id"));
					p.setNome(rs.getString("nome"));
					p.setDescricao(rs.getString("descricao"));
					p.setDataInicio(rs.getDate("data_inicio").toLocalDate());
					p.setDataTerminoPrevisto(rs.getDate("data_termino_previsto").toLocalDate());
					p.setStatus(StatusProjeto.valueOf(rs.getString("status").toUpperCase()));

					int gerenteId = rs.getInt("gerente_id");
					Usuario gerente = usuarioDAO.buscarPorId(gerenteId);
					p.setGerenteResponsavel(gerente);

					return p;
				}
			}
		}
		return null;
	}

	public List<Projeto> listarTodos() throws SQLException {
		List<Projeto> projetos = new ArrayList<>();
		String sql = "SELECT * FROM projetos";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next())
				projetos.add(buscarPorId(rs.getInt("id")));
		}
		return projetos;
	}
}
