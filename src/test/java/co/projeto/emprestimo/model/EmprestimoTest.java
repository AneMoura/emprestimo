package co.projeto.emprestimo.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    @Test
    void deveCriarEmprestimoComPrazoDe14Dias() {
        LocalDate hoje = LocalDate.of(2026, 11, 14);
        Emprestimo emp = new Emprestimo(1L, 2L, hoje);

        assertEquals(hoje, emp.getDataEmprestimo());
        assertEquals(LocalDate.of(2026, 11, 28), emp.getDataPrevistaDevolucao());
        assertNull(emp.getDataDevolucao());
    }

    @Test
    void deveRegistrarDataDeDevolucao() {
        LocalDate hoje = LocalDate.of(2026, 11, 14);
        Emprestimo emp = new Emprestimo(1L, 2L, hoje);

        LocalDate dataDevolucao = LocalDate.of(2026, 11, 20);
        emp.devolver(dataDevolucao);

        assertEquals(dataDevolucao, emp.getDataDevolucao());
    }

    @Test
    void naoDeveDevolverEmprestimoJaDevolvido() {
        LocalDate hoje = LocalDate.of(2026, 11, 14);
        Emprestimo emp = new Emprestimo(1L, 2L, hoje);
        emp.devolver(LocalDate.of(2026, 11, 15));

        assertThrows(IllegalStateException.class, () -> emp.devolver(LocalDate.of(2026, 11, 16)));
    }

    @Test
    void naoEstaAtrasadoAntesDaDataPrevista() {
        Emprestimo emp = new Emprestimo(1L, 2L, LocalDate.of(2026, 11, 14));
        assertFalse(emp.estaAtrasadoEm(LocalDate.of(2026, 11, 27)));
    }

    @Test
    void naoEstaAtrasadoNoDiaPrevisto() {
        Emprestimo emp = new Emprestimo(1L, 2L, LocalDate.of(2026, 11, 14));
        assertFalse(emp.estaAtrasadoEm(LocalDate.of(2026, 11, 28)));
    }

    @Test
    void estaAtrasadoAposDataPrevistaSemDevolucao() {
        Emprestimo emp = new Emprestimo(1L, 2L, LocalDate.of(2026, 11, 14));
        assertTrue(emp.estaAtrasadoEm(LocalDate.of(2026, 11, 29)));
    }

    @Test
    void naoEstaAtrasadoAposDataPrevistaSeJaFoiDevolvido() {
        Emprestimo emp = new Emprestimo(1L, 2L, LocalDate.of(2026, 11, 14));
        emp.devolver(LocalDate.of(2026, 11, 20));
        assertFalse(emp.estaAtrasadoEm(LocalDate.of(2026, 11, 29)));
    }
}
