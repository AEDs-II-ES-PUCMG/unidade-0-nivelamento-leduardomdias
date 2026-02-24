import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdutoPerecivel extends Produto {

    private final static double DESCONTO = .25;
    private final static int PRAZO_DESCONTO = 7;
    private LocalDateTime dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDateTime dataDeValidade) {
        super(desc, precoCusto, margemLucro);
        if (dataDeValidade == null) {
            throw new IllegalArgumentException("Data de validade eh obrigatória.");
        }
        if (dataDeValidade.toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade nao pode ser anterior ao dia atual.");
        }
        this.dataDeValidade = dataDeValidade;
    }

    @Override
    public double valorDeVenda() {
        if (dataDeValidade.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Produto fora do prazo de validade.");
        }

        double valor = super.valorDeVenda();

        if (!dataDeValidade.isAfter(LocalDateTime.now().plusDays(PRAZO_DESCONTO))) {
            valor *= (1.0 - DESCONTO);
        }

        return valor;
    }
}
