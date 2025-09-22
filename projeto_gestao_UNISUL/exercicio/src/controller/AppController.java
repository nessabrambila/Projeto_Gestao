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

    }

    public void showUsuarioView() {
        if (usuarioLogado.getPerfil() == PerfilAcesso.ADMINISTRADOR) {
            UsuarioView usuarioView = new UsuarioView();
        } else {

        }
    }
}
