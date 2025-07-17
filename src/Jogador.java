import java.util.*;

public class Jogador {
    protected String nome;
    protected int pontuacao;
    protected Personagem personagemEscolhido;
    protected ArrayList<Character> letrasAcertadas;
    protected int tentativasRestantes;
    protected static final int MAX_TENTATIVAS = 6;
    protected Random random;

    // Para controlar personagens já escolhidos
    private static Map<Integer, Boolean> personagensDisponiveis = new HashMap<>();

    static {
        personagensDisponiveis.put(1, true); // Sova
        personagensDisponiveis.put(2, true); // Beatrix
        personagensDisponiveis.put(3, true); // Lucky
    }

    public Jogador(String nome) {
        this.nome = nome;
        this.pontuacao = 0;
        this.letrasAcertadas = new ArrayList<>();
        this.tentativasRestantes = MAX_TENTATIVAS;
        this.random = new Random();
    }

    public void resetTentativas() {
        this.tentativasRestantes = MAX_TENTATIVAS;
    }

    public boolean temTentativas() {
        return tentativasRestantes > 0;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }

    public Personagem getPersonagemEscolhido() {
        return personagemEscolhido;
    }

    public boolean setPersonagem(int idPersonagem) {
        try {
            if (!personagensDisponiveis.containsKey(idPersonagem) || !personagensDisponiveis.get(idPersonagem)) {
                if (!personagensDisponiveis.containsKey(idPersonagem)) {
                    throw new IllegalArgumentException("Personagem com ID " + idPersonagem + " não existe!");
                } else {
                    throw new IllegalArgumentException("Personagem já foi escolhido por outro jogador!");
                }
            }

            switch (idPersonagem) {
                case 1:
                    this.personagemEscolhido = new Sova(this.nome);
                    break;
                case 2:
                    this.personagemEscolhido = new Beatrix(this.nome);
                    break;
                case 3:
                    this.personagemEscolhido = new Lucky(this.nome);
                    break;
                default:
                    throw new IllegalArgumentException("ID de personagem desconhecido.");
            }

            personagensDisponiveis.put(idPersonagem, false);
            System.out.println(this.nome + " escolheu " + this.personagemEscolhido.getNomePersonagem() + "!");
            this.personagemEscolhido.contarHistoria();
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public Minigames pensarEstrategia(Scanner scanner, Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta) {
        System.out.println("\nEscolha o minijogo:");
        System.out.println("1 - Codificador");
        System.out.println("2 - Decifrador");
        System.out.println("3 - Roleta Especial");
        System.out.print("Opção: ");

        int opcaoMinijogo;
        try {
            opcaoMinijogo = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Digite um número entre 1 e 3.");
            scanner.nextLine();
            return null;
        }

        if (opcaoMinijogo == 3) {
            return new RoletaEspecial(this, this.random);
        }

        char letraEscolhida;
        if (opcaoMinijogo == 1) {
            letraEscolhida = escolherLetraParaDesbloquear(letrasDesbloqueadasGlobal, palavraSecreta);
            return new Codificador(this, this.random);
        } else if (opcaoMinijogo == 2) {
            letraEscolhida = escolherLetraParaDecifrar(letrasDesbloqueadasGlobal, palavraSecreta);
            return new Decifrador(this, this.random, letraEscolhida);
        } else {
            System.out.println("Opção inválida! Escolha entre 1 e 3.");
            return null;
        }
    }

    private char escolherLetraParaDesbloquear(Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta) {
        List<Character> letrasDisponiveis = new ArrayList<>();
        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadasGlobal.contains(c)) {
                letrasDisponiveis.add(c);
            }
        }

        if (!letrasDisponiveis.isEmpty()) {
            return letrasDisponiveis.get(random.nextInt(letrasDisponiveis.size()));
        }

        // Se todas as letras da palavra já foram desbloqueadas
        char letraAleatoria;
        do {
            letraAleatoria = (char) ('A' + random.nextInt(26));
        } while (letrasDesbloqueadasGlobal.contains(letraAleatoria));

        return letraAleatoria;
    }

    private char escolherLetraParaDecifrar(Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta) {
        List<Character> letrasDaPalavra = new ArrayList<>();
        for (char c : palavraSecreta.toCharArray()) {
            if (!letrasDesbloqueadasGlobal.contains(c)) {
                letrasDaPalavra.add(c);
            }
        }

        if (!letrasDaPalavra.isEmpty()) {
            return letrasDaPalavra.get(random.nextInt(letrasDaPalavra.size()));
        }

        // Se todas as letras da palavra já foram desbloqueadas
        return (char) ('A' + random.nextInt(26));
    }

    public void aplicarEstrategia(Minigames minigame, char letraDesbloqueada, Set<Character> letrasDesbloqueadasGlobal) {
        if (minigame != null) {
            if (minigame.verificarVitoria()) {
                this.adicionarPontos(minigame.getRecompensa());
                letrasDesbloqueadasGlobal.add(letraDesbloqueada);
                this.letrasAcertadas.add(letraDesbloqueada);
                System.out.println("✅ " + this.nome + " ganhou " + minigame.getRecompensa() +
                        " pontos e desbloqueou a letra '" + letraDesbloqueada + "'!");
            } else {
                System.out.println("⚠️ " + this.nome + " não conseguiu desbloquear a letra desta vez!");
            }
            System.out.println("Pontuação atual: " + this.pontuacao);
        }
    }

    public void reduzirTentativa() {
        if (this.tentativasRestantes > 0) {
            this.tentativasRestantes--;
        }
    }

    public String getLetrasDesbloqueadasFormatadas() {
        if (letrasAcertadas.isEmpty()) {
            return "Nenhuma letra desbloqueada";
        }
        return letrasAcertadas.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", "");
    }

    public int getTentativasRestantes() {
        return this.tentativasRestantes;
    }

    public ArrayList<Character> getLetrasAcertadas() {
        return letrasAcertadas;
    }

    public void acertarLetra(char letra) {
        letrasAcertadas.add(letra);
        System.out.println("🎉 " + this.nome + " desbloqueou a letra '" + letra + "'!");
    }

    public void removerLetra(char letra) {
        if (letrasAcertadas.remove(Character.valueOf(letra))) {
            System.out.println("❌ " + this.nome + " perdeu a letra '" + letra + "'!");
        } else {
            System.out.println(this.nome + " não tinha a letra '" + letra + "' para perder.");
        }
    }
}