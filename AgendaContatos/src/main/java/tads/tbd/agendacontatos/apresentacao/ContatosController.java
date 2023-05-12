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

import tads.tbd.agendacontatos.apresentacao.viewmodels.ContatoViewModel;
import tads.tbd.agendacontatos.negocio.Contato;
import tads.tbd.agendacontatos.persistencia.ContatoMongoDbRepository;

@RestController
@RequestMapping("/api/v1/contatos")
public class ContatosController extends BaseController {
    private ContatoMongoDbRepository repository;

    public ContatosController() {
        repository = new ContatoMongoDbRepository();
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

        var resultadoInscricao = repository.carregar(id);
        if (resultadoInscricao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var inscricao = resultadoInscricao.get();
        inscricao.setNome(body.getNome());
        inscricao.setEndereco(body.getEndereco());
        inscricao.setTelefones(body.getTelefones());
        repository.salvar(inscricao);

        return ResponseEntity.ok(inscricao);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contato> buscar(
            @PathVariable ObjectId id) {

        var contato = repository.carregar(id);
        if (contato.isPresent()) {
            return ResponseEntity.ok(contato.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contato>> listarPorCidade(
            Optional<Integer> pular,
            Optional<Integer> buscar,
            @RequestParam String cidade) {

        if (pular.isEmpty()) pular = Optional.of(0);
        if (buscar.isEmpty()) buscar = Optional.of(10);
        var contatos = repository.listar(pular.get(), buscar.get(), cidade);
        if (contatos.size() > 0) {
            return ResponseEntity.ok(contatos);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ContatoViewModel> remover(
            @PathVariable ObjectId id) {
                        
        repository.remover(id);
        return ResponseEntity.noContent().build();
    }
}