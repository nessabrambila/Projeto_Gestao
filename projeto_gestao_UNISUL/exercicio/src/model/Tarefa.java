package model;

import java.time.LocalDate;
import enums.StatusTarefa;

public class Tarefa {
	private int id;
	private String titulo;
	private String descricao;
	private Projeto projeto;
	private Usuario responsavel;
	private StatusTarefa status;
	private LocalDate dataInicioPrevista;
	private LocalDate dataTerminoPrevista; 
	private LocalDate dataInicioReal;
	private LocalDate dataTerminoReal;

	// Getters e setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public StatusTarefa getStatus() {
		return status;
	}

	public void setStatus(StatusTarefa status) {
		this.status = status;
	}

	public LocalDate getDataInicioPrevista() {
		return dataInicioPrevista;
	}

	public void setDataInicioPrevista(LocalDate dataInicioPrevista) {
		this.dataInicioPrevista = dataInicioPrevista;
	}

	public LocalDate getDataTerminoPrevista() {
		return dataTerminoPrevista;
	}

	public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) {
		this.dataTerminoPrevista = dataTerminoPrevista;
	}

	public LocalDate getDataInicioReal() {
		return dataInicioReal;
	}

	public void setDataInicioReal(LocalDate dataInicioReal) {
		this.dataInicioReal = dataInicioReal;
	}

	public LocalDate getDataTerminoReal() {
		return dataTerminoReal;
	}

	public void setDataTerminoReal(LocalDate dataTerminoReal) {
		this.dataTerminoReal = dataTerminoReal;
	}
}
