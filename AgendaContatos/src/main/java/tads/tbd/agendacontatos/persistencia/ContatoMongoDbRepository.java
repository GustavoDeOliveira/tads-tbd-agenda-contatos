package tads.tbd.agendacontatos.persistencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.InsertOneResult;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

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
        return Optional.of(db.getCollection(colecao)
            .find(eq("_id", id), Contato.class)
            .first());
    }

    @Override
    public void salvar(Contato entidade) {
        var client = conexao.buscar();
        var db = client.getDatabase(base);
        if (entidade.getId() != null) {
            db.getCollection(colecao, Contato.class)
                .findOneAndReplace(eq("_id", entidade.getId()), entidade);
        } else {
            InsertOneResult resultado = db.getCollection(colecao, Contato.class)
                .insertOne(entidade);
            try {
                entidade.persistir(resultado.getInsertedId().asObjectId().getValue());
            } catch (Exception e) {
                System.err.println("Erro ao recuperar o id de um Contato inserido: " + e.getMessage());
            }
        }
    }

    @Override
    public void remover(ObjectId id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remover'");
    }

    @Override
    public List<Contato> listar(int pular, int buscar) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }
    
    public List<Contato> listar(int pular, int buscar, String cidade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }
    
}
