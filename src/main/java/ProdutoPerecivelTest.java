import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoPerecivelTest {

    @Test
    public void semDesconto_quandoPrazoMaiorQue7Dias() {
        LocalDateTime data = LocalDateTime.now().plusDays(8);
        ProdutoPerecivel p = new ProdutoPerecivel("Leite quase azedo", 5.0, 0.2, data);
        double esperado = 5.0 * (1.0 + 0.2);
        assertEquals(esperado, p.valorDeVenda(), 1e-9);
    }

    @Test
    public void aplicaDesconto_quandoPrazoMenorOuIgual7Dias() {
        LocalDateTime data = LocalDateTime.now().plusDays(7);
        ProdutoPerecivel p = new ProdutoPerecivel("Iogurte até que novo", 4.0, 0.25, data);
        double esperado = 4.0 * (1.0 + 0.25) * (1.0 - 0.25);
        assertEquals(esperado, p.valorDeVenda(), 1e-9);
    }

    @Test
    public void construtorRejeitaNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProdutoPerecivel("TesteProduto", 1.0, 0.1, null);
        });
    }

    @Test
    public void construtorRejeitaDataPassada() {
        LocalDateTime passada = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> {
            new ProdutoPerecivel("TesteProduto", 1.0, 0.1, passada);
        });
    }
}
