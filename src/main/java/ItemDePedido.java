public class ItemDePedido {

    // Atributos encapsulados
    private Produto produto;
    private int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda é "congelado" no momento da criação do item, garantindo que
     * alterações futuras no preço do produto original não afetem este registro.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double calcularSubtotal() {
        return precoVenda * quantidade;
    }

    @Override
    public String toString() {
        return String.format("%s | Qtd: %d | Preço Unit.: R$ %.2f | Subtotal: R$ %.2f",
                produto.descricao, quantidade, precoVenda, calcularSubtotal());
    }

    // --- Sobrescrita do método equals ---

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto,
     * independentemente da quantidade ou do preço congelado.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof ItemDePedido)) return false;
        ItemDePedido outro = (ItemDePedido) obj;
        return this.produto.equals(outro.produto);
    }
}
