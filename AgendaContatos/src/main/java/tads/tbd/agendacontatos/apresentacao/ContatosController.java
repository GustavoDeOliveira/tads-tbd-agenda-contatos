package tads.tbd.agendacontatos.apresentacao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tads.tbd.agendacontatos.aplicacao.AgendaService;
import tads.tbd.agendacontatos.aplicacao.NaoEncontradoException;
import tads.tbd.agendacontatos.apresentacao.viewmodels.ContatoViewModel;
import tads.tbd.agendacontatos.negocio.Contato;
import tads.tbd.agendacontatos.persistencia.ContatoMongoDbRepository;

@RestController
@RequestMapping("/api/v1/contatos")
public class ContatosController extends BaseController {
    private ContatoMongoDbRepository repository;
    private AgendaService service;

    public ContatosController() {
        repository = new ContatoMongoDbRepository();
        service = new AgendaService();
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> salvar(
                    @RequestBody ContatoViewModel body) {

        var contato = new Contato(body.getNome(), body.getEndereco(), body.getTelefones());
        repository.salvar(contato);

        return created(
                "/{id}",
                Map.of("id", contato.getId()));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> salvar(
            @PathVariable ObjectId id,
            @RequestBody ContatoViewModel body) {

        var resultado = service.AtualizarContato(id, body);

        if (resultado.isSucesso())
            return ResponseEntity.ok(resultado.getConteudo());
        else if (resultado.getErro().isPresent() 
            && resultado.getErro().get() instanceof NaoEncontradoException) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContatoViewModel> buscar(
            @PathVariable ObjectId id) {

        var contato = repository.carregar(id);
        if (contato.isPresent()) {
            return ResponseEntity.ok(new ContatoViewModel(contato.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ContatoViewModel>> listarPorCidade(
            @RequestParam String cidade) {

        var contatos = repository.listar(cidade);
        if (contatos.size() > 0) {
            return hashMapResponse(contatos, c -> new ContatoViewModel(c), c -> c.getId().toString());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/telefones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, ContatoViewModel>> listarPorTelefones() {

        var contatos = repository.listarComMaisDeUmTelefone();
        if (contatos.size() > 0) {
            return hashMapResponse(contatos, c -> new ContatoViewModel(c), c -> c.getId().toString());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> remover(
            @PathVariable ObjectId id) {
                        
        repository.remover(id);
        return ResponseEntity.noContent().build();
    }
}