package tads.tbd.agendacontatos.persistencia;

public interface Conexao<T> {
    public T buscar(boolean forcarAbertura);
    public void fechar();
}
