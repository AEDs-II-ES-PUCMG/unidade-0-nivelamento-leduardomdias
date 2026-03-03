import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas decimais.
     * Data de validade vai no formato dd/mm/aaaa
     * @return Uma string no formato "2; descrição; preçoDeCusto; margemDeLucro; dataDeValidade"
     */
    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("2; %s; %.2f; %.2f; %s", descricao, precoCusto, margemLucro, dataDeValidade.format(formato));
    }
}
