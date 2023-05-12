package tads.tbd.agendacontatos.persistencia;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class ConexaoMongoDb implements Conexao<MongoClient> {
    private static ConexaoMongoDb instancia;
    private MongoClientSettings configuracaoClient;
    private MongoClient conexao;
    
    private ConexaoMongoDb() {
        configurar();
    }
    
    public static ConexaoMongoDb instancia() {
        if (instancia == null) {
            instancia = new ConexaoMongoDb();
        }
        return instancia;
    }
    
    public MongoClient buscar() {
        
        return buscar(false);
    }
    
    @Override
    public MongoClient buscar(boolean forcarAbertura) {
        if (forcarAbertura) {
            fechar();
        }
        if (conexao == null) {
            conexao = MongoClients.create(configuracaoClient);
        }
        return conexao;
    }

    @Override
    public void fechar() {
        if (conexao != null) {
            conexao.close();
            conexao = null;
        }
    }
    
    private void configurar() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        configuracaoClient = MongoClientSettings.builder()
                                .applyConnectionString(connectionString)
                                .codecRegistry(codecRegistry)
                                .build();
    }
}
