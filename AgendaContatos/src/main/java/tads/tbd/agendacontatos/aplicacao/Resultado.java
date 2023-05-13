package tads.tbd.agendacontatos.aplicacao;

import java.util.Optional;

public class Resultado<T> {
    private boolean sucesso;
    private Optional<Exception> erro;
    private T conteudo;

    protected Resultado() {
        this.sucesso = true;
    }

    protected Resultado(T conteudo) {
        this.sucesso = true;
        this.conteudo = conteudo;
    }

    public Resultado(Optional<Exception> erro) {
        this.sucesso = false;
        this.erro = erro;
    }

    public Resultado(Optional<Exception> erro, T conteudo) {
        this.sucesso = false;
        this.erro = erro;
        this.conteudo = conteudo;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public Optional<Exception> getErro() {
        return erro;
    }

    public T getConteudo() {
        return conteudo;
    }

    public static <T> Resultado<T> sucesso() {
        return new Resultado<T>();
    }

    public static <T> Resultado<T> sucesso(T conteudo) {
        return new Resultado<T>(conteudo);
    }

    public static <T> Resultado<T> falha(Optional<Exception> erro) {
        return new Resultado<T>(erro);
    }
    
    public static <T> Resultado<T> falha(Optional<Exception> erro, T conteudo) {
        return new Resultado<T>(erro, conteudo);
    }
}