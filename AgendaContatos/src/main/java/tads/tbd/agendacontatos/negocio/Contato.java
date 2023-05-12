package tads.tbd.agendacontatos.negocio;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;

public class Contato implements Serializable {
    private ObjectId id;
    private String nome;
    private Endereco endereco;
    private List<Telefone> telefones;

    public Contato(){}
    public Contato(String nome) {
        this.nome = nome;
    }
    public Contato(String nome, Endereco endereco, List<Telefone> telefones) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefones = telefones;
    }
    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    public void persistir(ObjectId id) throws Exception {
        if (this.id == null) {
            this.id = id;
        }
        else {
            throw new Exception("Nao e possivel persistir o Contato com o id '" + id.toString() + "', contato ja possui o id '" + this.id.toString() + "'.");
        }
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public List<Telefone> getTelefones() {
        return telefones;
    }
    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    
}
