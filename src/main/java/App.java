import java.time.LocalDateTime;
public class App {

	public static void main(String[] args) {
		Produto p1 = new ProdutoNaoPerecivel("Arroz", 10.0, 0.5);
		System.out.println(p1);
		
		Produto p2 = new ProdutoPerecivel("Leite", 5.0, 0.3, LocalDateTime.now().plusDays(5));
		System.out.println(p2);
	}
}
