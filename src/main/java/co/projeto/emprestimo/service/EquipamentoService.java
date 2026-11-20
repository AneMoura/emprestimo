package co.projeto.emprestimo.service;

import co.projeto.emprestimo.model.Equipamento;
import co.projeto.emprestimo.model.TipoEquipamento;
import co.projeto.emprestimo.repository.EmprestimoRepository;
import co.projeto.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final EmprestimoRepository emprestimoRepository;

    public EquipamentoService(EquipamentoRepository repository, EmprestimoRepository emprestimoRepository) {
        this.repository = repository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public List<Equipamento> listar(TipoEquipamento tipo) {
        if (tipo == null) {
            return repository.findAll();
        }
        return repository.findByTipo(tipo);
    }

    public Equipamento buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
    }

    public Equipamento cadastrar(Equipamento equipamento) {
        if (repository.existsByPatrimonio(equipamento.getPatrimonio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patrimônio já cadastrado");
        }
        equipamento.setDisponivel(true);
        return repository.save(equipamento);
    }

    public Equipamento atualizar(Long id, Equipamento dados) {
        Equipamento existente = buscar(id);

        if (!existente.getPatrimonio().equals(dados.getPatrimonio())
                && repository.existsByPatrimonio(dados.getPatrimonio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patrimônio já cadastrado");
        }

        existente.setPatrimonio(dados.getPatrimonio());
        existente.setNome(dados.getNome());
        existente.setTipo(dados.getTipo());
        return repository.save(existente);
    }

    public void remover(Long id) {
        Equipamento existente = buscar(id);

        if (emprestimoRepository.existsByEquipamentoId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Equipamento já teve empréstimo e não pode ser removido");
        }

        repository.delete(existente);
    }
}
