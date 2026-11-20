package co.projeto.emprestimo.repository;
import co.projeto.emprestimo.model.Colaborador;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador,Long> {
    boolean existsByEmail(String email);
}
