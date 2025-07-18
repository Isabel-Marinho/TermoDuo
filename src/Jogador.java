import java.util.*;

public class Jogador {
    protected String nome;
    protected int pontuacao;
    protected Personagem personagemEscolhido;
    protected ArrayList<Character> letrasAcertadas;
    protected int tentativasRestantes;
    protected static final int MAX_TENTATIVAS = 6;
    protected Random random;
    private Set<Character> letrasDesbloqueadas;
    private boolean vencedor = false;

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
        this.letrasDesbloqueadas = new HashSet<>();
    }

    public void resetTentativas() {
        this.tentativasRestantes = MAX_TENTATIVAS;
    }

    public boolean temTentativas() {
        return tentativasRestantes > 0;
    }

    public boolean isVencedor() {
        return this.vencedor;
    }

    public void marcarComoVencedor() {
        this.vencedor = true;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public Set<Character> getLetrasDesbloqueadas() {
        return this.letrasDesbloqueadas;
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
            this.personagemEscolhido.contarHistoria(); // Exibe a história do personagem.
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    public Minigames pensarEstrategia(Scanner scanner, Set<Character> letrasDesbloqueadasGlobal, String palavraSecreta, Jogador adversario) {
        // Verifica se todas letras já foram desbloqueadas
        boolean todasLetrasDesbloqueadas = palavraSecreta.chars()
                .allMatch(c -> letrasDesbloqueadasGlobal.contains((char)c));

        if (todasLetrasDesbloqueadas) {
            System.out.println("Parabéns! Você já desbloqueou todas as letras da palavra secreta!");
            return null;
        }

        System.out.println("\nEscolha o minijogo:");
        System.out.println("1 - Codificador (Foca em uma letra específica)");
        System.out.println("2 - Decifrador (Foca em uma letra específica)");
        System.out.println("3 - Roleta Especial (Chance de ganhar pontos ou bônus)");
        System.out.print("Opção: ");

        try {
            int opcaoMinijogo = scanner.nextInt();
            scanner.nextLine();

            char letraAlvo = selecionarLetraDisponivel(this, palavraSecreta);
            if (letraAlvo == '\0') {
                System.out.println("Todas letras já desbloqueadas!");
                return null;
            }

            switch (opcaoMinijogo) {
                case 1:
                    return new Codificador(this, random, letraAlvo);
                case 2:
                    return new Decifrador(this, random, letraAlvo);
                case 3:
                    return new RoletaEspecial(this, adversario, random, letraAlvo);
                default:
                    System.out.println("Opção inválida!");
                    return null;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite um número.");
            scanner.nextLine();
            return null;
        }
    }

    private char selecionarLetraDisponivel(Jogador jogador, String palavraSecreta) {
        Set<Character> letrasPalavra = new HashSet<>();
        Set<Character> letrasDisponiveis = new HashSet<>();

        for (char c : palavraSecreta.toCharArray()) {
            letrasPalavra.add(c);
        }

        letrasDisponiveis.addAll(letrasPalavra);
        letrasDisponiveis.removeAll(jogador.getLetrasDesbloqueadas());

        if (letrasDisponiveis.isEmpty()) {
            return '\0';
        }

        List<Character> listaDisponiveis = new ArrayList<>(letrasDisponiveis);
        return listaDisponiveis.get(random.nextInt(listaDisponiveis.size()));
    }


    public void aplicarEstrategia(Minigames minigame, Set<Character> letrasDesbloqueadasGlobal) {
        if (minigame != null) {
            if (minigame.verificarVitoria()) {
                this.adicionarPontos(minigame.getRecompensa());
                if (minigame instanceof Codificador) {
                    char letraGanha = ((Codificador)minigame).getLetraAlvo();
                    letrasDesbloqueadasGlobal.add(letraGanha);
                    this.letrasAcertadas.add(letraGanha);
                    SoundManager.playSound("sounds/letrarevelada.wav");
                    System.out.println("\n✅ " + this.nome + " ganhou " + minigame.getRecompensa() +
                            " pontos e desbloqueou a letra '" + letraGanha + "'!");
                }
                else if (minigame instanceof Decifrador) {
                    char letraGanha = ((Decifrador)minigame).getLetraAlvo();
                    letrasDesbloqueadasGlobal.add(letraGanha);
                    this.letrasAcertadas.add(letraGanha);
                    SoundManager.playSound("sounds/letrarevelada.wav");
                    System.out.println("✅ " + this.nome + " ganhou " + minigame.getRecompensa() +
                            " pontos e desbloqueou a letra '" + letraGanha + "'!");
                } else {
                    // Para minigames que não desbloqueiam letras
                    SoundManager.playSound("sounds/letrarevelada.wav");
                    System.out.println("\n✅ " + this.nome + " ganhou " + minigame.getRecompensa() + " pontos!");
                }
            } else {
                System.out.println("\n⚠️ " + this.nome + " não conseguiu completar o minijogo!");
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