import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Comercio {

    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;

    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /**
     * Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * @return Um inteiro com a opção do usuário.
     */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos.
     * Formato: N na primeira linha, depois uma linha por produto.
     * Retorna um vetor vazio (tamanho MAX_NOVOS_PRODUTOS) em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */public class InnerComercio {
     
        
     }
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos;
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivoDados))) {
            int n = Integer.parseInt(leitor.readLine().trim());
            vetorProdutos = new Produto[n + MAX_NOVOS_PRODUTOS];
            quantosProdutos = 0;
            for (int i = 0; i < n; i++) {
                String linha = leitor.readLine();
                if (linha != null && !linha.isBlank()) {
                    vetorProdutos[i] = Produto.criarDoTexto(linha);
                    quantosProdutos++;
                }
            }
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível ler o arquivo (" + e.getMessage() + "). Iniciando com vetor vazio.");
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
            quantosProdutos = 0;
        }
        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
        if (quantosProdutos == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("--- Produtos cadastrados ---");
        for (int i = 0; i < quantosProdutos; i++) {
            try {
                System.out.printf("%d - %s%n", i + 1, produtosCadastrados[i]);
            } catch (IllegalStateException e) {
                System.out.printf("%d - %s [VENCIDO]%n", i + 1, produtosCadastrados[i].descricao);
            }
        }
    }

    /**
     * Localiza um produto no vetor de cadastrados, a partir do nome (descrição), e imprime seus dados.
     * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime mensagem padrão.
     */
    static void localizarProdutos() {
        System.out.print("Digite o nome do produto a localizar: ");
        String nome = teclado.nextLine();
        Produto busca = new ProdutoNaoPerecivel(
            nome.length() >= 3 ? nome : nome + "___", 1.0, 0.01);

        boolean encontrado = false;
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].equals(busca)) {
                try {
                    System.out.println("Produto encontrado: " + produtosCadastrados[i]);
                } catch (IllegalStateException e) {
                    System.out.println("Produto encontrado (fora de validade): " + produtosCadastrados[i].descricao);
                }
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Produto \"" + nome + "\" não encontrado.");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto,
     * lê os dados correspondentes, cria o objeto adequado e inclui no vetor.
     */
    static void cadastrarProduto() {
        // Verifica se ainda há espaço no vetor
        if (quantosProdutos >= produtosCadastrados.length) {
            System.out.println("Vetor de produtos cheio. Não é possível cadastrar novos produtos.");
            return;
        }

        System.out.println("--- Cadastro de novo produto ---");
        System.out.println("Tipo: 1 - Não perecível  |  2 - Perecível");
        System.out.print("Digite o tipo: ");
        int tipo;
        try {
            tipo = Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Tipo inválido.");
            return;
        }

        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido.");
            return;
        }

        System.out.print("Descrição: ");
        String desc = teclado.nextLine();

        System.out.print("Preço de custo (ex: 10.50): ");
        double precoCusto;
        try {
            precoCusto = Double.parseDouble(teclado.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido.");
            return;
        }

        System.out.print("Margem de lucro (ex: 0.20 para 20%): ");
        double margemLucro;
        try {
            margemLucro = Double.parseDouble(teclado.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Margem inválida.");
            return;
        }

        Produto novoProduto;
        try {
            if (tipo == 1) {
                novoProduto = new ProdutoNaoPerecivel(desc, precoCusto, margemLucro);
            } else {
                System.out.print("Data de validade (dd/mm/aaaa): ");
                String dataStr = teclado.nextLine();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime dataValidade;
                try {
                    dataValidade = LocalDate.parse(dataStr, fmt).atStartOfDay();
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
                    return;
                }
                novoProduto = new ProdutoPerecivel(desc, precoCusto, margemLucro, dataValidade);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao criar produto: " + e.getMessage());
            return;
        }

        produtosCadastrados[quantosProdutos] = novoProduto;
        quantosProdutos++;
        System.out.println("Produto cadastrado com sucesso!");
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo CSV informado.
     * Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(nomeArquivo))) {
            escritor.println(quantosProdutos);
            for (int i = 0; i < quantosProdutos; i++) {
                escritor.println(produtosCadastrados[i].gerarDadosTexto());
            }
            System.out.println("Dados salvos em \"" + nomeArquivo + "\" com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);

        int opcao = -1;
        try {
            do {
                opcao = menu();
                switch (opcao) {
                    case 1 -> listarTodosOsProdutos();
                    case 2 -> localizarProdutos();
                    case 3 -> cadastrarProduto();
                    case 0 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }
                if (opcao != 0) pausa();
            } while (opcao != 0);
        } finally {
            salvarProdutos(nomeArquivoDados);
            teclado.close();
        }
    }
}
