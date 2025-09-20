package controller;

import enums.PerfilAcesso;
import model.Usuario;
import view.MainView;
import view.UsuarioView;

import java.awt.*;
import java.awt.event.ActionListener;

public class AppController {
    private MainView mainView;
    private Usuario usuarioLogado;

    public AppController(MainView mainView, Usuario usuarioLogado) {
        this.mainView = mainView;
        this.usuarioLogado = usuarioLogado;
        setupListeners();
    }

    private void setupListeners() {
        // Exemplo de como um botão na MainView poderia chamar a tela de usuários
        // Se tivéssemos um botão, a lógica seria aqui.
        // Como estamos usando JTabbedPane, a navegação já é controlada por ele.
    }

    public void showUsuarioView() {
        // A lógica de controle de acesso pode ser feita aqui
        if (usuarioLogado.getPerfil() == PerfilAcesso.ADMINISTRADOR) {
            UsuarioView usuarioView = new UsuarioView();
            // Lógica para adicionar a view ao JTabbedPane da MainView
            // mainView.addTab("Usuários", usuarioView);
        } else {
            // Mensagem de erro ou outra ação para acesso negado
        }
    }
}
