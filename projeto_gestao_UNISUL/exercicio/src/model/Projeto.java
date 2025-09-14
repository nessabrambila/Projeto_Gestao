package model;

import enums.StatusProjeto;
import java.time.LocalDate;

public class Projeto {
	private int id;
	private String nome;
	private String descricao;
	private LocalDate dataInicio;
	private LocalDate dataTerminoPrevisto;
	private StatusProjeto status;
	private Usuario gerenteResponsavel;

	// Getters e setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataTerminoPrevisto() {
		return dataTerminoPrevisto;
	}

	public void setDataTerminoPrevisto(LocalDate dataTerminoPrevisto) {
		this.dataTerminoPrevisto = dataTerminoPrevisto;
	}

	public StatusProjeto getStatus() {
		return status;
	}

	public void setStatus(StatusProjeto status) {
		this.status = status;
	}

	public Usuario getGerenteResponsavel() {
		return gerenteResponsavel;
	}

	public void setGerenteResponsavel(Usuario gerenteResponsavel) {
		this.gerenteResponsavel = gerenteResponsavel;
	}
}
