package enums;


public enum PerfilAcesso {
    ADMINISTRADOR("administrador"),
    GERENTE("gerente"),
    COLABORADOR("colaborador");

    private String valor;

    PerfilAcesso(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}