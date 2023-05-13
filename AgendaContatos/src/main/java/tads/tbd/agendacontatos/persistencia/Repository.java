package tads.tbd.agendacontatos.persistencia;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<Tid, T extends Serializable> {
    public Optional<T> carregar(Tid id);
    public void salvar(T entidade);
    public void remover(Tid id);
    public List<T> listar();
}
