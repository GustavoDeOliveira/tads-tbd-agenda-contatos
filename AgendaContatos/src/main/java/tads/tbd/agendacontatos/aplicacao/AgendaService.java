package tads.tbd.agendacontatos.aplicacao;

import java.util.Optional;

import org.bson.types.ObjectId;

import tads.tbd.agendacontatos.apresentacao.viewmodels.ContatoViewModel;
import tads.tbd.agendacontatos.negocio.Contato;
import tads.tbd.agendacontatos.persistencia.ConexaoMongoDb;
import tads.tbd.agendacontatos.persistencia.ContatoMongoDbRepository;

public class AgendaService {
    private ContatoMongoDbRepository repository;

    public AgendaService() {
        this.repository = new ContatoMongoDbRepository();
    }

    public Resultado<Contato> AtualizarContato(ObjectId id, ContatoViewModel model) {
        var resultadoContato = repository.carregar(id);
        if (resultadoContato.isEmpty()) {
            return Resultado.falha(Optional.of(
                new NaoEncontradoException()));
        }

        var contato = resultadoContato.get();
        contato.setNome(model.getNome());
        contato.setEndereco(model.getEndereco());
        contato.setTelefones(model.getTelefones());
        repository.salvar(contato);

        ConexaoMongoDb.instancia().fechar();
        return Resultado.sucesso(contato);
    }
}
