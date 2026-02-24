

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoNaoPerecivelTest {

    @Test
    public void valorDeVenda_calculadoCorreto() {
        ProdutoNaoPerecivel p = new ProdutoNaoPerecivel("ArrozTipo1", 10.0, 0.5);
        double esperado = 10.0 * (1.0 + 0.5);
        assertEquals(esperado, p.valorDeVenda(), 1e-9);
    }
}
