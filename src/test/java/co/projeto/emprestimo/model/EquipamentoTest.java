package co.projeto.emprestimo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EquipamentoTest {

    @Test
    void deveEmprestarEquipamentoDisponivel() {
        Equipamento eq = new Equipamento();
        eq.setDisponivel(true);

        eq.emprestar();

        assertFalse(eq.isDisponivel());
    }

    @Test
    void naoDeveEmprestarEquipamentoIndisponivel() {
        Equipamento eq = new Equipamento();
        eq.setDisponivel(false);

        assertThrows(IllegalStateException.class, () -> eq.emprestar());
    }

    @Test
    void deveDevolverEquipamento() {
        Equipamento eq = new Equipamento();
        eq.setDisponivel(false);

        eq.devolver();

        assertTrue(eq.isDisponivel());
    }
}
