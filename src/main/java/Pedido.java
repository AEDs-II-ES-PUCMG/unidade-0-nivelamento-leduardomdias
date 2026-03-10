import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido {

	/** Quantidade máxima de produtos de um pedido */
	private static final int MAX_PRODUTOS = 10;
	
	/** Porcentagem de desconto para pagamentos à vista */
	private static final double DESCONTO_PG_A_VISTA = 0.15;
	
	/** Vetor para armazenar os itens do pedido */
	private ItemDePedido[] itens;
	
	/** Data de criação do pedido */
	private LocalDate dataPedido;
	
	/** Indica a quantidade total de itens no pedido até o momento */
	private int quantItens = 0;
	
	/** Indica a forma de pagamento do pedido sendo: 1, pagamento à vista; 2, pagamento parcelado */
	private int formaDePagamento;
	
	/** Construtor do pedido.
	 *  Deve criar o vetor de produtos do pedido, 
	 *  armazenar a data e a forma de pagamento informadas para o pedido. 
	 */  
	public Pedido(LocalDate dataPedido, int formaDePagamento) {
		
		itens = new ItemDePedido[MAX_PRODUTOS];
		quantItens = 0;
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
	}
	
	/**
     * Inclui um produto neste pedido com quantidade 1, congelando o preço de venda atual.
     * @param novo O produto a ser incluído no pedido
     * @return true/false indicando se a inclusão foi realizada com sucesso.
     */
	public boolean incluirProduto(Produto novo) {
		return incluirItem(novo, 1);
	}

	/**
     * Inclui um produto neste pedido com a quantidade informada, congelando o preço de venda atual.
     * @param novo O produto a ser incluído
     * @param quantidade A quantidade do produto
     * @return true/false indicando se a inclusão foi realizada com sucesso.
     */
	public boolean incluirItem(Produto novo, int quantidade) {
		if (quantItens < MAX_PRODUTOS) {
			itens[quantItens++] = new ItemDePedido(novo, quantidade, novo.valorDeVenda());
			return true;
		}
		return false;
	}
	
	/**
     * Calcula e retorna o valor final do pedido (soma do valor de venda de todos os produtos do pedido).
     * Caso a forma de pagamento do pedido seja à vista, aplica o desconto correspondente.
     * @return Valor final do pedido (double)
     */
	public double valorFinal() {
		
		double valorPedido = 0;
		
		for (int i = 0; i < quantItens; i++) {
			valorPedido += itens[i].calcularSubtotal();
		}
		
		if (formaDePagamento == 1) {
			valorPedido = valorPedido * (1.0 - DESCONTO_PG_A_VISTA);
		}
		return valorPedido;
	}

	/**
	 * Mescla os itens de outroPedido neste pedido (operação atômica).
	 * Itens com o mesmo produto têm suas quantidades somadas e é mantido o menor preço.
	 * Lança IllegalStateException se a capacidade do vetor for insuficiente.
	 * Após a mesclagem, o pedido secundário é esvaziado.
	 * @param outroPedido O pedido cujos itens serão transferidos para este.
	 */
	public void mesclarPedido(Pedido outroPedido) {
		// Conta apenas os itens do pedido secundário que NÃO existem neste pedido
		int novosItens = 0;
		for (int i = 0; i < outroPedido.quantItens; i++) {
			boolean encontrado = false;
			for (int j = 0; j < quantItens; j++) {
				if (itens[j].equals(outroPedido.itens[i])) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado) novosItens++;
		}

		// Validação de capacidade (operação atômica: falha antes de qualquer alteração)
		if (quantItens + novosItens > MAX_PRODUTOS) {
			throw new IllegalStateException(
				"Capacidade insuficiente: a mesclagem excederia o limite de " + MAX_PRODUTOS + " itens.");
		}

		// Realiza a mesclagem
		for (int i = 0; i < outroPedido.quantItens; i++) {
			boolean encontrado = false;
			for (int j = 0; j < quantItens; j++) {
				if (itens[j].equals(outroPedido.itens[i])) {
					// Produto já existe: soma quantidades e mantém o menor preço
					itens[j].setQuantidade(itens[j].getQuantidade() + outroPedido.itens[i].getQuantidade());
					if (outroPedido.itens[i].getPrecoVenda() < itens[j].getPrecoVenda()) {
						itens[j].setPrecoVenda(outroPedido.itens[i].getPrecoVenda());
					}
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				// Produto novo: ocupa a próxima posição livre
				itens[quantItens++] = outroPedido.itens[i];
			}
		}

		// Esvazia logicamente o pedido secundário
		for (int i = 0; i < outroPedido.quantItens; i++) {
			outroPedido.itens[i] = null;
		}
		outroPedido.quantItens = 0;
	}

	/**
	 * Imprime um recibo de venda no terminal com nome, quantidade, preço unitário e subtotal
	 * de cada item, além do total geral do pedido.
	 * Itens com mais de 10 unidades recebem 5% de desconto no subtotal.
	 */
	public void imprimirRecibo() {
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.println("========== RECIBO DE VENDA ==========");
		System.out.println("Data: " + dataPedido.format(formatoData));
		System.out.println("-------------------------------------");
		System.out.printf("%-20s | %5s | %12s | %12s%n", "Produto", "Qtd", "Preço Unit.", "Subtotal");
		System.out.println("-------------------------------------");

		double total = 0;
		for (int i = 0; i < quantItens; i++) {
			if (itens[i] == null) continue;
			String nome = itens[i].getProduto().descricao;
			int qtd = itens[i].getQuantidade();
			double precoUnit = itens[i].getPrecoVenda();
			double subtotal = itens[i].calcularSubtotal();

			if (qtd > 10) {
				subtotal *= 0.95;
				System.out.printf("%-20s | %5d | R$ %9.2f | R$ %9.2f (desc. 5%%)%n",
						nome, qtd, precoUnit, subtotal);
			} else {
				System.out.printf("%-20s | %5d | R$ %9.2f | R$ %9.2f%n",
						nome, qtd, precoUnit, subtotal);
			}
			total += subtotal;
		}

		System.out.println("-------------------------------------");
		if (formaDePagamento == 1) {
			total *= (1.0 - DESCONTO_PG_A_VISTA);
			System.out.printf("Desconto à vista (%.0f%%): aplicado%n", DESCONTO_PG_A_VISTA * 100);
		}
		System.out.printf("TOTAL GERAL: R$ %.2f%n", total);
		System.out.println("=====================================");
	}
	
	/**
     * Representação, em String, do pedido.
     * Contém um cabeçalho com sua data e o número de produtos no pedido.
     * Depois, em cada linha, a descrição de cada produto do pedido.
     * Ao final, mostra a forma de pagamento, o percentual de desconto (se for o caso) e o valor a ser pago pelo pedido.
     * Exemplo:
     * Data do pedido: 25/08/2025
     * Pedido com 2 produtos.
     * Produtos no pedido:
     * NOME: Iogurte: R$ 8.00
     * Válido até: 29/08/2025
     * NOME: Guardanapos: R$ 2.75
     * Pedido pago à vista. Percentual de desconto: 15,00%
     * Valor total do pedido: R$ 10.75 
     * @return Uma string contendo dados do pedido conforme especificado (cabeçalho, detalhes, forma de pagamento,
     * percentual de desconto - se for o caso - e valor a pagar)
     */
	@Override
	public String toString() {
		
		StringBuilder stringPedido = new StringBuilder();
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		stringPedido.append("Data do pedido:" + formatoData.format(dataPedido) + "\n");
		
		stringPedido.append("Pedido com " + quantItens + " itens.\n");
		stringPedido.append("Itens no pedido:\n");
		for (int i = 0; i < quantItens; i++ ) {
			stringPedido.append(itens[i].toString() + "\n");
		}
		
		stringPedido.append("Pedido pago ");
		if (formaDePagamento == 1) {
			stringPedido.append("à vista. Percentual de desconto: " + String.format("%.2f", DESCONTO_PG_A_VISTA * 100) + "%\n");
		} else {
			stringPedido.append("parcelado.\n");
		}
		
		stringPedido.append("Valor total do pedido: R$ " + String.format("%.2f", valorFinal()));
		
		return stringPedido.toString();
	}
	
	/**
     * Igualdade de pedidos: caso possuam a mesma data. 
     * @param obj Outro pedido a ser comparado 
     * @return booleano true/false conforme o parâmetro possua a data igual ou não a este pedido.
     */
    @Override
    public boolean equals(Object obj) {
        Pedido outro = (Pedido)obj;
        return this.dataPedido.equals(outro.dataPedido);
    }
}