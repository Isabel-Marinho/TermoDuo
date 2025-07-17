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
            if (!personagemValido2 == personagemValido1) {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        }
        jogador1.resetTentativas();
        jogador2.resetTentativas();

        return jogadores;
    }

    public void criarCodificador(Jogador jogador) {
        this.codificador = new Codificador(jogador, random);
    }

    public void criarDecifrador(Jogador jogador, char letra) {
        this.decifrador = new Decifrador(jogador, random, letra);
    }

    public boolean jogarCodificador() {
        if (codificador != null) {
            codificador.iniciar();
            return codificador.verificarVitoria();
        }
        return false;
    }

    public boolean jogarDecifrador() {
        if (decifrador != null) {
            decifrador.iniciar();
            return decifrador.verificarVitoria();
        }
        return false;
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

            boolean podeAdivinhar = jogador.getLetrasAcertadas().size() >= palavraSecreta.length() / 2;

            System.out.println("1 - " + (podeAdivinhar ? "Tentar adivinhar a palavra" : "Você precisa desbloquear mais letras para adivinhar"));
            System.out.println("2 - Jogar minijogo para desbloquear letras");
            System.out.println("3 - Ver status");
            System.out.print("Escolha: ");

            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    if (podeAdivinhar) {
                        System.out.print("Digite seu palpite para a palavra completa: ");
                        String palpite = scanner.nextLine().toUpperCase();

                        if (palpite.equals(palavraSecreta)) {
                            for (char c : palavraSecreta.toCharArray()) {
                                jogador.acertarLetra(c);
                            }
                            System.out.println("Correto! Você acertou a palavra!");
                            turnoConcluido = true;
                        } else {
                            jogador.reduzirTentativa();
                            System.out.println("Tentativa errada! Agora você tem " + jogador.getTentativasRestantes() +
                                    "/" + Jogador.MAX_TENTATIVAS + " tentativas restantes.");

                            for (int i = 0; i < palavraSecreta.length(); i++) {
                                if (i < palpite.length() && palpite.charAt(i) == palavraSecreta.charAt(i)) {
                                    System.out.print(palpite.charAt(i) + " ");
                                } else {
                                    System.out.print("_ ");
                                }
                            }
                            System.out.println();
                            turnoConcluido = true;
                        }
                    } else {
                        System.out.println("Você precisa desbloquear pelo menos " + (palavraSecreta.length()) +
                                " letras antes de tentar adivinhar a palavra completa.");
                    }
                    break;

                case 2:
                    Minigames minigame = jogador.pensarEstrategia(scanner, letrasDesbloqueadas, palavraSecreta);
                    if (minigame != null) {
                        boolean venceu = minigame.verificarVitoria();
                        if (venceu) {
                            char letraGanha = escolherLetraParaDesbloquear();
                            jogador.acertarLetra(letraGanha);
                            System.out.println("✅ Letra desbloqueada: " + letraGanha);
                            if (jogador.getLetrasAcertadas().size() >= palavraSecreta.length()) {
                                System.out.println("💡 Dica: Você já pode tentar adivinhar a palavra completa!");
                            }
                        } else {
                            System.out.println("Não foi dessa vez!");
                        }
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

    @Override
    public boolean jogoAcabou() {
        // Verifica se algum jogador acertou todas as letras
        for (Jogador jogador : jogadores) {
            if (jogador.getLetrasAcertadas().size() == palavraSecreta.length()) {
                this.vencedor = jogador;
                return true;
            }
        }

        // Verifica se ambos esgotaram as tentativas
        if (!jogador1.temTentativas() && !jogador2.temTentativas()) {
            // Define vencedor por quem tem mais letras
            if (jogador1.getLetrasAcertadas().size() > jogador2.getLetrasAcertadas().size()) {
                this.vencedor = jogador1;
            } else if (jogador2.getLetrasAcertadas().size() > jogador1.getLetrasAcertadas().size()) {
                this.vencedor = jogador2;
            }
            return true;
        }

        return false;
    }

    private char escolherLetraParaDesbloquear() {

        List<Character> letrasDisponiveis = new ArrayList<>();

        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadas.contains(c)) {
                letrasDisponiveis.add(c);
            }
        }

        if (!letrasDisponiveis.isEmpty()) {
            char letra = letrasDisponiveis.get(random.nextInt(letrasDisponiveis.size()));
            letrasDesbloqueadas.add(letra); // Marca como desbloqueada
            return letra;
        }

        char letraAleatoria;
        do {
            letraAleatoria = (char) ('A' + random.nextInt(26));
        } while (letrasDesbloqueadas.contains(letraAleatoria));

        letrasDesbloqueadas.add(letraAleatoria);
        return letraAleatoria;
    }

    private char escolherLetraParaDecifrar() {
        // Prioriza letras da palavra secreta ainda não desbloqueadas
        List<Character> letrasDaPalavra = new ArrayList<>();
        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadas.contains(c)) {
                letrasDaPalavra.add(c);
            }
        }

        if (!letrasDaPalavra.isEmpty()) {
            return letrasDaPalavra.get(random.nextInt(letrasDaPalavra.size()));
        }

        // Se todas as letras da palavra já foram desbloqueadas
        return (char) ('A' + random.nextInt(26));
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