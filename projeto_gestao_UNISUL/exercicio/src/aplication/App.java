package aplication;

import dao.*;
import model.*;
import enums.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {
	private static final Scanner sc = new Scanner(System.in);

	private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static final ProjetoDAO projetoDAO = new ProjetoDAO();
	private static final EquipeDAO equipeDAO = new EquipeDAO();
	private static final TarefaDAO tarefaDAO = new TarefaDAO();

	public static void main(String[] args) {
		boolean executar = true;

		while (executar) {
			System.out.println("\n=== Sistema de Gestão de Projetos ===");
			System.out.println("1. Gerenciar Usuários");
			System.out.println("2. Gerenciar Projetos");
			System.out.println("3. Gerenciar Equipes");
			System.out.println("4. Gerenciar Tarefas");
			System.out.println("0. Sair");
			System.out.print("Escolha uma opção: ");
			String opcao = sc.nextLine();

			switch (opcao) {
			case "1":
				gerenciarUsuarios();
				break;
			case "2":
				gerenciarProjetos();
				break;
			case "3":
				gerenciarEquipes();
				break;
			case "4":
				gerenciarTarefas();
				break;
			case "0":
				executar = false;
				break;
			default:
				System.out.println("Opção inválida!");
			}
		}

		sc.close();
	}

	// ========================== USUÁRIOS ==========================
	private static void gerenciarUsuarios() {
		boolean gerenciar = true;
		while (gerenciar) {
			System.out.println("\n--- Gerenciar Usuários ---");
			System.out.println("1. Listar usuários");
			System.out.println("2. Inserir usuário");
			System.out.println("3. Atualizar usuário");
			System.out.println("4. Deletar usuário");
			System.out.println("0. Voltar");
			System.out.print("Opção: ");
			String uOpcao = sc.nextLine();

			try {
				switch (uOpcao) {
				case "1":
					List<Usuario> usuarios = usuarioDAO.listarTodos();
					usuarios.forEach(u -> System.out
							.println(u.getId() + " - " + u.getNomeCompleto() + " (" + u.getPerfil() + ")"));
					break;
				case "2":
					Usuario novo = new Usuario();
					System.out.print("Nome completo: ");
					novo.setNomeCompleto(sc.nextLine());
					System.out.print("CPF: ");
					novo.setCpf(sc.nextLine());
					System.out.print("Email: ");
					novo.setEmail(sc.nextLine());
					System.out.print("Senha: ");
					novo.setSenha(sc.nextLine());
					System.out.print("Cargo: ");
					novo.setCargo(sc.nextLine());
					System.out.print("Login: ");
					novo.setLogin(sc.nextLine());
					System.out.print("Perfil (ADMINISTRADOR, GERENTE, COLABORADOR): ");
					novo.setPerfil(PerfilAcesso.valueOf(sc.nextLine().toUpperCase()));

					usuarioDAO.inserir(novo);
					System.out.println("Usuário inserido com sucesso!");
					break;
				case "3":
					System.out.print("ID do usuário para atualizar: ");
					int uId = Integer.parseInt(sc.nextLine());
					Usuario uAtual = usuarioDAO.buscarPorId(uId);
					if (uAtual != null) {
						System.out.print("Nome completo (" + uAtual.getNomeCompleto() + "): ");
						String novoNome = sc.nextLine();
						if (!novoNome.isBlank())
							uAtual.setNomeCompleto(novoNome);

						System.out.print("Email (" + uAtual.getEmail() + "): ");
						String novoEmail = sc.nextLine();
						if (!novoEmail.isBlank())
							uAtual.setEmail(novoEmail);

						System.out.print("Cargo (" + uAtual.getCargo() + "): ");
						String novoCargo = sc.nextLine();
						if (!novoCargo.isBlank())
							uAtual.setCargo(novoCargo);

						System.out.print("Login (" + uAtual.getLogin() + "): ");
						String novoLogin = sc.nextLine();
						if (!novoLogin.isBlank())
							uAtual.setLogin(novoLogin);

						System.out.print("Perfil (" + uAtual.getPerfil() + "): ");
						String novoPerfil = sc.nextLine();
						if (!novoPerfil.isBlank())
							uAtual.setPerfil(PerfilAcesso.valueOf(novoPerfil.toUpperCase()));

						usuarioDAO.atualizar(uAtual);
						System.out.println("Usuário atualizado com sucesso!");
					} else
						System.out.println("Usuário não encontrado.");
					break;
				case "4":
					System.out.print("ID do usuário para deletar: ");
					int delId = Integer.parseInt(sc.nextLine());
					usuarioDAO.deletar(delId);
					System.out.println("Usuário deletado com sucesso!");
					break;
				case "0":
					gerenciar = false;
					break;
				default:
					System.out.println("Opção inválida!");
				}
			} catch (SQLException e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}

	// ========================== PROJETOS ==========================
	private static void gerenciarProjetos() {
		boolean gerenciar = true;
		while (gerenciar) {
			System.out.println("\n--- Gerenciar Projetos ---");
			System.out.println("1. Listar projetos");
			System.out.println("2. Inserir projeto");
			System.out.println("3. Atualizar projeto");
			System.out.println("4. Deletar projeto");
			System.out.println("0. Voltar");
			System.out.print("Opção: ");
			String pOpcao = sc.nextLine();

			try {
				switch (pOpcao) {
				case "1":
					List<Projeto> projetos = projetoDAO.listarTodos();
					projetos.forEach(
							p -> System.out.println(p.getId() + " - " + p.getNome() + " (" + p.getStatus() + ")"));
					break;
				case "2":
					Projeto novo = new Projeto();
					System.out.print("Nome: ");
					novo.setNome(sc.nextLine());
					System.out.print("Descrição: ");
					novo.setDescricao(sc.nextLine());
					System.out.print("Data início (yyyy-MM-dd): ");
					novo.setDataInicio(LocalDate.parse(sc.nextLine()));
					System.out.print("Data término previsto (yyyy-MM-dd): ");
					novo.setDataTerminoPrevisto(LocalDate.parse(sc.nextLine()));
					System.out.print("Status (PLANEJADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO): ");
					String statusInput = sc.nextLine().toUpperCase().replace(" ", "_");
					novo.setStatus(StatusProjeto.valueOf(statusInput));
					System.out.print("ID do gerente responsável: ");
					int gerenteId = Integer.parseInt(sc.nextLine());
					Usuario gerente = usuarioDAO.buscarPorId(gerenteId);
					if (gerente == null) {
						System.out.println("Gerente não encontrado. Operação cancelada!");
						break;
					}
					novo.setGerenteResponsavel(gerente);

					projetoDAO.inserir(novo);
					System.out.println("Projeto inserido com sucesso!");
					break;
				case "3":
					System.out.print("ID do projeto para atualizar: ");
					int projId = Integer.parseInt(sc.nextLine());
					Projeto pAtual = projetoDAO.buscarPorId(projId);
					if (pAtual != null) {
						System.out.print("Nome (" + pAtual.getNome() + "): ");
						String nome = sc.nextLine();
						if (!nome.isBlank())
							pAtual.setNome(nome);

						System.out.print("Descrição (" + pAtual.getDescricao() + "): ");
						String desc = sc.nextLine();
						if (!desc.isBlank())
							pAtual.setDescricao(desc);

						System.out.print("Data início (" + pAtual.getDataInicio() + "): ");
						String dataIni = sc.nextLine();
						if (!dataIni.isBlank())
							pAtual.setDataInicio(LocalDate.parse(dataIni));

						System.out.print("Data término previsto (" + pAtual.getDataTerminoPrevisto() + "): ");
						String dataFim = sc.nextLine();
						if (!dataFim.isBlank())
							pAtual.setDataTerminoPrevisto(LocalDate.parse(dataFim));

						System.out.print("Status (" + pAtual.getStatus() + "): ");
						String status = sc.nextLine();
						if (!status.isBlank())
							pAtual.setStatus(StatusProjeto.valueOf(status.toUpperCase()));

						projetoDAO.atualizar(pAtual);
						System.out.println("Projeto atualizado com sucesso!");
					} else
						System.out.println("Projeto não encontrado.");
					break;
				case "4":
					System.out.print("ID do projeto para deletar: ");
					int delId = Integer.parseInt(sc.nextLine());
					projetoDAO.deletar(delId);
					System.out.println("Projeto deletado com sucesso!");
					break;
				case "0":
					gerenciar = false;
					break;
				default:
					System.out.println("Opção inválida!");
				}
			} catch (SQLException e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}

	// ========================== EQUIPES ==========================
	private static void gerenciarEquipes() {
		boolean gerenciar = true;
		while (gerenciar) {
			System.out.println("\n--- Gerenciar Equipes ---");
			System.out.println("1. Listar equipes");
			System.out.println("2. Inserir equipe");
			System.out.println("3. Atualizar equipe");
			System.out.println("4. Deletar equipe");
			System.out.println("0. Voltar");
			System.out.print("Opção: ");
			String eOpcao = sc.nextLine();

			try {
				switch (eOpcao) {
				case "1":
					List<Equipe> equipes = equipeDAO.listarTodos();
					equipes.forEach(e -> System.out.println(e.getId() + " - " + e.getNome()));
					break;
				case "2":
					Equipe nova = new Equipe();
					System.out.print("Nome: ");
					nova.setNome(sc.nextLine());
					System.out.print("Descrição: ");
					nova.setDescricao(sc.nextLine());
					equipeDAO.inserir(nova);
					System.out.println("Equipe inserida com sucesso!");
					break;
				case "3":
					System.out.print("ID da equipe para atualizar: ");
					int eqId = Integer.parseInt(sc.nextLine());
					Equipe eqAtual = equipeDAO.buscarPorId(eqId);
					if (eqAtual != null) {
						System.out.print("Nome (" + eqAtual.getNome() + "): ");
						String nome = sc.nextLine();
						if (!nome.isBlank())
							eqAtual.setNome(nome);

						System.out.print("Descrição (" + eqAtual.getDescricao() + "): ");
						String desc = sc.nextLine();
						if (!desc.isBlank())
							eqAtual.setDescricao(desc);

						equipeDAO.atualizar(eqAtual);
						System.out.println("Equipe atualizada com sucesso!");
					} else
						System.out.println("Equipe não encontrada.");
					break;
				case "4":
					System.out.print("ID da equipe para deletar: ");
					int delId = Integer.parseInt(sc.nextLine());
					equipeDAO.deletar(delId);
					System.out.println("Equipe deletada com sucesso!");
					break;
				case "0":
					gerenciar = false;
					break;
				default:
					System.out.println("Opção inválida!");
				}
			} catch (SQLException e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}

	// ========================== TAREFAS ==========================
	private static void gerenciarTarefas() {
		boolean gerenciar = true;
		while (gerenciar) {
			System.out.println("\n--- Gerenciar Tarefas ---");
			System.out.println("1. Listar tarefas");
			System.out.println("2. Inserir tarefa");
			System.out.println("3. Atualizar tarefa");
			System.out.println("4. Deletar tarefa");
			System.out.println("0. Voltar");
			System.out.print("Opção: ");
			String tOpcao = sc.nextLine();

			try {
				switch (tOpcao) {
				case "1":
					List<Tarefa> tarefas = tarefaDAO.listarTodos();
					tarefas.forEach(
							t -> System.out.println(t.getId() + " - " + t.getTitulo() + " (" + t.getStatus() + ")"));
					break;
				case "2":
					Tarefa nova = new Tarefa();
					System.out.print("Título: ");
					nova.setTitulo(sc.nextLine());
					System.out.print("Descrição: ");
					nova.setDescricao(sc.nextLine());

					System.out.print("ID do projeto: ");
					int projId = Integer.parseInt(sc.nextLine());
					Projeto proj = projetoDAO.buscarPorId(projId);
					if (proj == null) {
						System.out.println("Projeto não encontrado. Operação cancelada!");
						break;
					}
					nova.setProjeto(proj);

					System.out.print("ID do responsável: ");
					int respId = Integer.parseInt(sc.nextLine());
					Usuario resp = usuarioDAO.buscarPorId(respId);
					if (resp == null) {
						System.out.println("Responsável não encontrado. Operação cancelada!");
						break;
					}
					nova.setResponsavel(resp);

					System.out.print("Status (PENDENTE, EM_EXECUCAO, CONCLUIDO): ");
					nova.setStatus(StatusTarefa.valueOf(sc.nextLine().toUpperCase()));

					System.out.print("Data início prevista (yyyy-MM-dd): ");
					String dtIniPrev = sc.nextLine();
					if (!dtIniPrev.isBlank())
						nova.setDataInicioPrevista(LocalDate.parse(dtIniPrev));

					System.out.print("Data término prevista (yyyy-MM-dd): ");
					String dtFimPrev = sc.nextLine();
					if (!dtFimPrev.isBlank())
						nova.setDataTerminoPrevista(LocalDate.parse(dtFimPrev));

					tarefaDAO.inserir(nova);
					System.out.println("Tarefa inserida com sucesso!");
					break;
				case "3":
					System.out.print("ID da tarefa para atualizar: ");
					int tId = Integer.parseInt(sc.nextLine());
					Tarefa tAtual = tarefaDAO.buscarPorId(tId);
					if (tAtual != null) {
						System.out.print("Título (" + tAtual.getTitulo() + "): ");
						String titulo = sc.nextLine();
						if (!titulo.isBlank())
							tAtual.setTitulo(titulo);

						System.out.print("Descrição (" + tAtual.getDescricao() + "): ");
						String desc = sc.nextLine();
						if (!desc.isBlank())
							tAtual.setDescricao(desc);

						System.out.print("Status (" + tAtual.getStatus() + "): ");
						String status = sc.nextLine();
						if (!status.isBlank())
							tAtual.setStatus(StatusTarefa.valueOf(status.toUpperCase()));

						tarefaDAO.atualizar(tAtual);
						System.out.println("Tarefa atualizada com sucesso!");
					} else
						System.out.println("Tarefa não encontrada.");
					break;
				case "4":
					System.out.print("ID da tarefa para deletar: ");
					int delId = Integer.parseInt(sc.nextLine());
					tarefaDAO.deletar(delId);
					System.out.println("Tarefa deletada com sucesso!");
					break;
				case "0":
					gerenciar = false;
					break;
				default:
					System.out.println("Opção inválida!");
				}
			} catch (SQLException e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}
}
