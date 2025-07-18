import java.util.*;

public class Jogo implements InterfaceJogo {
    protected ArrayList<Jogador> jogadores;
    protected String palavraSecreta;
    protected Set<Character> letrasDesbloqueadas;
    protected Jogador vencedor;
    protected Random random;
    protected Codificador codificador;
    protected Decifrador decifrador;

    public Jogo() {
        this.jogadores = new ArrayList<>();
        this.letrasDesbloqueadas = new HashSet<>();
        this.random = new Random();  // Inicializa o Random
        this.codificador = null;
        this.decifrador = null;
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
            if (!personagemValido2) {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        }
        jogador1.resetTentativas();
        jogador2.resetTentativas();

        return jogadores;
    }

    public void definirPalavraSecreta(String palavra) {
        this.palavraSecreta = palavra.toUpperCase();
        this.letrasDesbloqueadas = new HashSet<>();
        System.out.println("Palavra secreta definida: " + palavraSecreta.replaceAll(".", "_"));
    }

    public void jogarTurno(Jogador jogador) {
        if (!jogador.temTentativas()) {
            System.out.println(jogador.getNome() + " não tem mais tentativas!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean turnoConcluido = false;

        while (!turnoConcluido) {
            System.out.println("\nTentativas restantes: " + jogador.getTentativasRestantes() + "/" + Jogador.MAX_TENTATIVAS);
            System.out.println("Letras desbloqueadas: " + jogador.getLetrasDesbloqueadasFormatadas());

            boolean podeAdivinhar = jogador.getLetrasAcertadas().size() >= palavraSecreta.length();

            System.out.println("1 - " + (podeAdivinhar ? "Tentar adivinhar a palavra" : "Você precisa desbloquear mais letras para adivinhar"));
            System.out.println("2 - Jogar minijogo para desbloquear letras");
            System.out.println("3 - Ver status");
            System.out.print("Escolha: ");

            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    if (podeAdivinhar) {
                        System.out.print("Digite sua tentativa: ");
                        String tentativa = scanner.nextLine().toUpperCase();

                        if (tentativa.equals(palavraSecreta)) {
                            jogador.adicionarPontos(500);
                            jogador.marcarComoVencedor(); // Marca explicitamente
                            System.out.println("✅ " + jogador.getNome() + " acertou a palavra e ganhou 500 pontos!");
                            return; // Sai do turno
                        } else {
                            jogador.reduzirTentativa();
                            System.out.println("❌ Palavra incorreta! Tentativas restantes: " +
                                    jogador.getTentativasRestantes());

                            if (!jogador.temTentativas()) {
                                System.out.println("⛔ " + jogador.getNome() + " ficou sem tentativas!");
                            }
                        }
                    } else {
                        System.out.println("🔒 Você precisa desbloquear mais letras para adivinhar!");
                    }
                    break;

                case 2:
                    Jogador adversario = (jogador == jogador1) ? jogador2 : jogador1;
                    Minigames minigame = jogador.pensarEstrategia(scanner, letrasDesbloqueadas, palavraSecreta, adversario);
                    if (minigame != null) {
                        minigame.iniciar();
                        jogador.aplicarEstrategia(minigame, letrasDesbloqueadas);
                        turnoConcluido = true;
                    }
                    break;

                case 3:
                    mostrarStatus(jogador1, jogador2);
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public void mostrarStatus(Jogador jogador1, Jogador jogador2) {
        System.out.println("\n=== STATUS DO JOGO ===");
        System.out.println("-----------------------");
        imprimirStatusJogador(jogador1);
        System.out.println("-----------------------");
        imprimirStatusJogador(jogador2);
        System.out.println("=======================");
    }

    private void imprimirStatusJogador(Jogador jogador) {
        System.out.println("Jogador: " + jogador.getNome());
        System.out.println("Letras Acertadas: " + jogador.getLetrasAcertadas());
        System.out.println("Pontuação: " + jogador.getPontuacao());

    }

    public boolean jogoAcabou() {
        // Verifica vitória por adivinhação da palavra
        if (jogador1.isVencedor() || jogador2.isVencedor()) {
            this.vencedor = jogador1.isVencedor() ? jogador1 : jogador2;
            return true;
        }

        // Verifica vitória por letras desbloqueadas
        for (Jogador jogador : Arrays.asList(jogador1, jogador2)) {
            if (jogador.getLetrasAcertadas().size() == palavraSecreta.length()) {
                jogador.marcarComoVencedor();
                this.vencedor = jogador;
                return true;
            }
        }

        // Verifica fim por tentativas esgotadas
        if (!jogador1.temTentativas() && !jogador2.temTentativas()) {
            // Critério de desempate
            int comparacao = Integer.compare(
                    jogador1.getLetrasAcertadas().size(),
                    jogador2.getLetrasAcertadas().size()
            );

            this.vencedor = comparacao > 0 ? jogador1 :
                    comparacao < 0 ? jogador2 :
                            jogador1.getPontuacao() >= jogador2.getPontuacao() ? jogador1 : jogador2;
            return true;
        }

        return false;
    }

    private char escolherLetraParaDesbloquear() {
        List<Character> letrasDisponiveis = new ArrayList<>();

        // 1. Coletar letras da palavra secreta não desbloqueadas
        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadas.contains(c)) {
                letrasDisponiveis.add(c);
            }
        }

        if (!letrasDisponiveis.isEmpty()) {
            char letra = letrasDisponiveis.get(random.nextInt(letrasDisponiveis.size()));
            letrasDesbloqueadas.add(letra);
            return letra;
        }
        throw new IllegalStateException("Todas as letras já foram desbloqueadas!");
    }

    public Jogador getVencedor() {
        return this.vencedor;
    }


    public void pontuacao() {
        System.out.println("\nPontuação Final");
        for (Jogador j : jogadores) {
            System.out.println(j.getNome() + ": " + j.letrasAcertadas.size() + " letras acertadas");
        }
    }

}