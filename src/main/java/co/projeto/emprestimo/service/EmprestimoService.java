
package co.projeto.emprestimo.service;

import co.projeto.emprestimo.model.Emprestimo;
import co.projeto.emprestimo.model.Equipamento;
import co.projeto.emprestimo.repository.ColaboradorRepository;
import co.projeto.emprestimo.repository.EmprestimoRepository;
import co.projeto.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ColaboradorRepository colaboradorRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             EquipamentoRepository equipamentoRepository,
                             ColaboradorRepository colaboradorRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<Emprestimo> listar(Boolean atrasados) {
        if (Boolean.TRUE.equals(atrasados)) {
            return emprestimoRepository.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
        }
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscar(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }

    public List<Emprestimo> listarDoColaborador(Long colaboradorId) {
        if (!colaboradorRepository.existsById(colaboradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado");
        }
        return emprestimoRepository.findByColaboradorId(colaboradorId);
    }

    @Transactional
    public Emprestimo emprestar(Long equipamentoId, Long colaboradorId) {
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));

        if (!colaboradorRepository.existsById(colaboradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado");
        }

        LocalDate hoje = LocalDate.now();

        if (emprestimoRepository.existsByColaboradorIdAndDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(colaboradorId, hoje)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Colaborador possui empréstimo atrasado");
        }

        try {
            equipamento.emprestar();
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        equipamentoRepository.save(equipamento);
        Emprestimo emprestimo = new Emprestimo(equipamentoId, colaboradorId, hoje);
        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo devolver(Long id) {
        Emprestimo emprestimo = buscar(id);
        Equipamento equipamento = equipamentoRepository.findById(emprestimo.getEquipamentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));

        try {
            emprestimo.devolver(LocalDate.now());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        equipamento.devolver();
        equipamentoRepository.save(equipamento);
        return emprestimoRepository.save(emprestimo);
    }
}
