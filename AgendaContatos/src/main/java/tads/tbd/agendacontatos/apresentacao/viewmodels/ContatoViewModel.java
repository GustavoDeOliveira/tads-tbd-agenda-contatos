package tads.tbd.agendacontatos.apresentacao.viewmodels;

import java.util.List;

import tads.tbd.agendacontatos.negocio.Endereco;
import tads.tbd.agendacontatos.negocio.Telefone;

public class ContatoViewModel {
    private String nome;
    private Endereco endereco;
    private List<Telefone> telefones;
    
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
