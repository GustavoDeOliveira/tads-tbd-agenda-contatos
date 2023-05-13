package tads.tbd.agendacontatos.persistencia;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import tads.tbd.agendacontatos.negocio.Contato;

public class ContatoMongoDbRepository implements Repository<ObjectId, Contato> {
    private final ConexaoMongoDb conexao;
    private final String base = "Agenda";
    private String colecao = "Contatos";
    
    public ContatoMongoDbRepository() {
        conexao = ConexaoMongoDb.instancia();
    }

    @Override
    public Optional<Contato> carregar(ObjectId id) {
        var client = conexao.buscar();
        var db = client.getDatabase(base);
        return Optional.ofNullable(db.getCollection(colecao)
            .find(eq("_id", id), Contato.class)
            .first());
    }

    @Override
    public void salvar(Contato entidade) {
        var contatos = getContatos();
        if (entidade.getId() != null) {
            contatos.findOneAndReplace(eq("_id", entidade.getId()), entidade);
        } else {
            InsertOneResult resultado = contatos.insertOne(entidade);
            try {
                entidade.persistir(resultado.getInsertedId().asObjectId().getValue());
            } catch (Exception e) {
                System.err.println("Erro ao recuperar o id de um Contato inserido: " + e.getMessage());
            }
        }
    }

    @Override
    public void remover(ObjectId id) {
        getContatos()
            .deleteOne(eq("_id", id));
    }

    @Override
    public List<Contato> listar() {
        return listar(null, false);
    }

    public List<Contato> listarComMaisDeUmTelefone() {
        return listar(null, true);
    }
    
    public List<Contato> listar(String cidade) {
        return listar(cidade, false);
    }

    public List<Contato> listar(String cidade, boolean maisDeUmTelefone) {
        var contatos = getContatos();
        FindIterable<Contato> query;
        if (cidade != null) {
            query = contatos.find(eq("endereco.cidade", cidade));
        } else if (maisDeUmTelefone) {
            query = contatos.find(Filters.exists("telefones.1"));
        } else {
            query = contatos.find();
        }
        List<Contato> resultado = new ArrayList<Contato>();
        query.into(resultado);
        return resultado;
    }
    
    private MongoCollection<Contato> getContatos() {
        var client = conexao.buscar();
        var db = client.getDatabase(base);
        return db.getCollection(colecao, Contato.class);
    }
}
