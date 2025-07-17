import java.util.Random;

public class RoletaEspecial extends Minigames {
    public static final String GANHAR_LETRA_GRATIS = "Ganhar Letra Gratis";
    public static final String TIRAR_UMA_LETRA_ADVERSARIO = "Tirar Uma Letra Adversario";
    public static final String PERDER_VEZ = "Perder Vez";
    public static final String TIRAR_DUAS_LETRAS_ADVERSARIO = "Tirar Duas Letras Adversario";

    private Jogador jogadorAtual;
    private Random random;

    // Probabilidades padrão (serão ajustadas se for a Lucky)
    private double probGanharLetra = 0.30;
    private double probTirarUma = 0.15;
    private double probPerderVez = 0.50;
    private double probTirarDuas = 0.05;

    private String ultimoResultado;

    public RoletaEspecial(Jogador jogador, Random random) {
        super("Roleta Especial");
        this.jogadorAtual = jogador;
        this.random = random;
        this.ultimoResultado = null;
    }

    @Override
    public void exibirRegras() {
        System.out.println("\n=== REGRAS DA ROLETA ESPECIAL ===");
        System.out.println("- Gire a roleta para obter um resultado aleatório.");
        System.out.println("- As chances de cada resultado são:");
        System.out.println("  - Ganhar 1 Letra Grátis: " + (probGanharLetra * 100) + "%");
        System.out.println("  - Tirar 1 Letra do Adversário: " + (probTirarUma * 100) + "%");
        System.out.println("  - Perder a Vez: " + (probPerderVez * 100) + "%");
        System.out.println("  - Tirar 2 Letras do Adversário: " + (probTirarDuas * 100) + "%");
        System.out.println("Sua recompensa (e a do adversário) dependerá do resultado!");
    }

    @Override
    public void iniciar() {
        exibirRegras();
        System.out.println("Pressione ENTER para girar a roleta...");
        new java.util.Scanner(System.in).nextLine();

        System.out.println("\nGirando a roleta...");

        if ("Lucky".equals(jogadorAtual.personagemEscolhido.getTipoPersonagem())) {
            Lucky lucky = (Lucky) jogadorAtual.personagemEscolhido;
            lucky.modificaRegra(this); // Lucky alterando a sorte
        }

        this.ultimoResultado = sortearResultado();
        System.out.println("\nResultado da roleta: " + ultimoResultado);

        if (ultimoResultado.equals(GANHAR_LETRA_GRATIS)) {
            this.setRecompensa(20);
            System.out.println("Você ganhou 20 pontos!");
        } else if (ultimoResultado.equals(TIRAR_UMA_LETRA_ADVERSARIO)) {
            this.setRecompensa(45);
            System.out.println("Você ganhou 45 pontos!");
        } else if (ultimoResultado.equals(TIRAR_DUAS_LETRAS_ADVERSARIO)) {
            this.setRecompensa(75);
            System.out.println("Você ganhou 75 pontos!");
        } else if (ultimoResultado.equals(PERDER_VEZ)) {
            this.setRecompensa(0);
            System.out.println("Você não ganhou pontos desta vez.");
        }
        this.setConcluido(true);
    }

    private String sortearResultado() {
        double sorteio = random.nextDouble();

        if (sorteio < probGanharLetra) {
            return GANHAR_LETRA_GRATIS;
        } else if (sorteio < probGanharLetra + probTirarUma) {
            return TIRAR_UMA_LETRA_ADVERSARIO;
        } else if (sorteio < probGanharLetra + probTirarUma + probPerderVez) {
            return PERDER_VEZ;
        } else {
            return TIRAR_DUAS_LETRAS_ADVERSARIO;
        }
    }

    @Override
    public boolean verificarVitoria() {
        // Verifica se o resultado é um dos que podem ser considerados "vitória"
        return ultimoResultado.equals(GANHAR_LETRA_GRATIS) ||
                ultimoResultado.equals(TIRAR_UMA_LETRA_ADVERSARIO) ||
                ultimoResultado.equals(TIRAR_DUAS_LETRAS_ADVERSARIO);
    }

    public String getUltimoResultado() {
        return ultimoResultado;
    }

    public void ajustarProbabilidadesParaLucky() {
        System.out.println("Vantagem de Lucky sendo aplicada: Maior chance de pegar o prêmio especial da roleta!");

        // Reduz a chance de perder a vez em 20%
        this.probPerderVez -= 0.20;

        // Aumenta a chance de tirar duas letras em 20%
        this.probTirarDuas += 0.20;

        // Imprimir as novas probabilidades para o usuário ver o efeito da vantagem
        System.out.println("Probabilidades da roleta ajustadas para Lucky:");
        System.out.println("  Ganhar 1 Letra Grátis: " + String.format("%.0f", (this.probGanharLetra * 100)) + "%");
        System.out.println("  Tirar 1 Letra Adversário: " + String.format("%.0f", (this.probTirarUma * 100)) + "%");
        System.out.println("  Perder a Vez: " + String.format("%.0f", (this.probPerderVez * 100)) + "%");
        System.out.println("  Tirar 2 Letras Adversário: " + String.format("%.0f", (this.probTirarDuas * 100)) + "%");
    }
}