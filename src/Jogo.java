import java.util.*;

public class Jogo implements InterfaceJogo {
    protected ArrayList<Jogador> jogadores;
    protected String palavraSecreta;
    protected Set<Character> letrasDesbloqueadas;
    protected Jogador vencedor;

    public Jogo() {
        this.jogadores = new ArrayList<>();
        this.letrasDesbloqueadas = new HashSet<>();
    }

    Jogador jogador1 = new Jogador("Jogador 1");
    Jogador jogador2 = new Jogador("Jogador 2");

    @Override
    public ArrayList<Jogador> iniciarJogo() {

        System.out.println("TERMO DUO");
        System.out.println("\nREGRAS DO JOGO:");
        System.out.println("Cada jogador tem 6 tentativas para descobrir a palavra secreta");
        System.out.println("A palavra tem até 6 letras");
        System.out.println("As letras começam bloqueadas e devem ser desbloqueadas através de minijogos");
        System.out.println("Só pode ser jogado 1 minijogo por turno");
        System.out.println("Vence quem descobrir a palavra primeiro!\n");

        System.out.println("PERSONAGENS DISPONÍVEIS:");
        System.out.println("Sova | Especial: Frases menores para codificar");
        System.out.println("Beatrix | Especial: Acerta 2 números automaticamente");
        System.out.println("Lucky | Especial: +20% chance na roleta especial\n");

        jogadores.add(jogador1);
        jogadores.add(jogador2);

        Scanner tec = new Scanner(System.in);

        boolean personagemValido1 = false;
        while (!personagemValido1) {
            System.out.println("Jogador 1, escolha seu personagem:\n");
            System.out.println("Digite (1) para escolher Sova!");
            System.out.println("Digite (2) para escolher Beatrix!");
            System.out.println("Digite (3) para escolher Lucky!");
            int a = tec.nextInt();
            tec.nextLine();
            personagemValido1 = jogador1.setPersonagem(a);
            if (!personagemValido1) {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        }

        boolean personagemValido2 = false;
        while (!personagemValido2) {
            System.out.println("\nJogador 2, escolha seu personagem:\n");
            System.out.println("Digite (1) para escolher Sova!");
            System.out.println("Digite (2) para escolher Beatrix!");
            System.out.println("Digite (3) para escolher Lucky!");
            int a = tec.nextInt();
            tec.nextLine();
            personagemValido2 = jogador2.setPersonagem(a);
            if (!personagemValido2 == personagemValido1) {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        }
        return jogadores;
    }

    public void jogarTurno(Jogador jogador) {
        Scanner scanner = new Scanner(System.in);

        Jogador jogadorOponente = (jogador.equals(jogador1)) ? jogador2 : jogador1;


        for (int rodada = 1; ; rodada++) {
            System.out.println("\nRodada " + rodada + " ");

            Jogador jogadorDaVez = (rodada % 2 == 1) ? jogador : jogadorOponente;
            System.out.println("Vez de: " + jogadorDaVez.getNome());

            System.out.println("1 - Tentar adivinhar a palavra");
            System.out.println("2 - Jogar minijogo");
            System.out.println("3 - Ver status");
            System.out.print("Escolha: ");

            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    System.out.println(jogadorDaVez.getNome() + " está tentando adivinhar...");
                    break;

                case 2:
                    jogadorDaVez.pensarEstrategia();
                    //jogadorDaVez.pensarEstrategia();
                    break;

                case 3:
                    //mostrarStatus(jogador, jogadorOponente);
                    rodada--;
                    break;

                default:
                    System.out.println("Opção inválida!");
                    rodada--;
            }

            if (jogoAcabou()) {
                System.out.println(jogadorDaVez.getNome() + " venceu o jogo!");
                break;
            }
        }
    }

   /* public void mostrarStatus(Jogador jogador1, Jogador jogador2) {
        System.out.println("\nSTATUS: ");
        System.out.println(jogador1.getNome() + ":");
        System.out.println("  Tentativas restantes: " + jogador1.getTentativasRestantes());
        System.out.println("  Letras desbloqueadas: " + jogador1.getLetrasDesbloqueadas());

        System.out.println("\n" + jogador2.getNome() + ":");
        System.out.println("  Tentativas restantes: " + jogador2.getTentativasRestantes());
        System.out.println("  Letras desbloqueadas: " + jogador2.getLetrasDesbloqueadas());
    } */

    @Override
    public boolean jogoAcabou() {
        return false;
    }
/*
    public void pontuacao() {
        System.out.println("\nPontuação Final");
        for (Jogador j : jogadores) {
            System.out.println(j.getNome() + ": " + j.letrasDesbloqueadas.size() + " letras desbloqueadas");
        }
    } */

}