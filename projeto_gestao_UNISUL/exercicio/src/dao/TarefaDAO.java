package dao;

import model.Projeto;
import model.Tarefa;
import model.Usuario;
import enums.StatusTarefa;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

	private ProjetoDAO projetoDAO = new ProjetoDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	public void inserir(Tarefa t) throws SQLException {
		String sql = "INSERT INTO tarefas (titulo, descricao, projeto_id, responsavel_id, status, data_inicio_prevista, data_termino_previsto, data_inicio_real, data_termino_real) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, t.getTitulo());
			stmt.setString(2, t.getDescricao());
			stmt.setInt(3, t.getProjeto().getId());
			stmt.setInt(4, t.getResponsavel().getId());
			stmt.setString(5, t.getStatus().name().toLowerCase());
			stmt.setDate(6, t.getDataInicioPrevista() != null ? Date.valueOf(t.getDataInicioPrevista()) : null);
			stmt.setDate(7, t.getDataTerminoPrevista() != null ? Date.valueOf(t.getDataTerminoPrevista()) : null);
			stmt.setDate(8, t.getDataInicioReal() != null ? Date.valueOf(t.getDataInicioReal()) : null);
			stmt.setDate(9, t.getDataTerminoReal() != null ? Date.valueOf(t.getDataTerminoReal()) : null);

			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next())
					t.setId(rs.getInt(1));
			}
		}
	}

	public void atualizar(Tarefa t) throws SQLException {
		String sql = "UPDATE tarefas SET titulo=?, descricao=?, projeto_id=?, responsavel_id=?, status=?, data_inicio_prevista=?, data_termino_previsto=?, data_inicio_real=?, data_termino_real=? WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, t.getTitulo());
			stmt.setString(2, t.getDescricao());
			stmt.setInt(3, t.getProjeto().getId());
			stmt.setInt(4, t.getResponsavel().getId());
			stmt.setString(5, t.getStatus().name().toLowerCase());
			stmt.setDate(6, t.getDataInicioPrevista() != null ? Date.valueOf(t.getDataInicioPrevista()) : null);
			stmt.setDate(7, t.getDataTerminoPrevista() != null ? Date.valueOf(t.getDataTerminoPrevista()) : null);
			stmt.setDate(8, t.getDataInicioReal() != null ? Date.valueOf(t.getDataInicioReal()) : null);
			stmt.setDate(9, t.getDataTerminoReal() != null ? Date.valueOf(t.getDataTerminoReal()) : null);
			stmt.setInt(10, t.getId());

			stmt.executeUpdate();
		}
	}

	public void deletar(int id) throws SQLException {
		String sql = "DELETE FROM tarefas WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	public Tarefa buscarPorId(int id) throws SQLException {
		String sql = "SELECT * FROM tarefas WHERE id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next())
					return mapearTarefa(rs);
			}
		}
		return null;
	}

	public List<Tarefa> listarTodos() throws SQLException {
		List<Tarefa> tarefas = new ArrayList<>();
		String sql = "SELECT * FROM tarefas";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next())
				tarefas.add(mapearTarefa(rs));
		}
		return tarefas;
	}

	private Tarefa mapearTarefa(ResultSet rs) throws SQLException {
		Tarefa t = new Tarefa();
		t.setId(rs.getInt("id"));
		t.setTitulo(rs.getString("titulo"));
		t.setDescricao(rs.getString("descricao"));

		Projeto p = projetoDAO.buscarPorId(rs.getInt("projeto_id"));
		t.setProjeto(p);

		Usuario u = usuarioDAO.buscarPorId(rs.getInt("responsavel_id"));
		t.setResponsavel(u);

		t.setStatus(StatusTarefa.valueOf(rs.getString("status").toUpperCase()));

		Date dtInicioPrev = rs.getDate("data_inicio_prevista");
		if (dtInicioPrev != null)
			t.setDataInicioPrevista(dtInicioPrev.toLocalDate());

		Date dtFimPrev = rs.getDate("data_termino_previsto");
		if (dtFimPrev != null)
			t.setDataTerminoPrevista(dtFimPrev.toLocalDate());

		Date dtInicioReal = rs.getDate("data_inicio_real");
		if (dtInicioReal != null)
			t.setDataInicioReal(dtInicioReal.toLocalDate());

		Date dtFimReal = rs.getDate("data_termino_real");
		if (dtFimReal != null)
			t.setDataTerminoReal(dtFimReal.toLocalDate());

		return t;
	}
}
