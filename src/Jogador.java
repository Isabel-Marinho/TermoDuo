import java.util.*;

public class Jogador {
    protected String nome;
    protected int pontuacao;
    protected Personagem personagemEscolhido;
    protected ArrayList<Character> letrasAcertadas; // Para o jogo principal

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

    public int pensarEstrategia() {
            Scanner tec = new Scanner(System.in);
            System.out.println("\n--- Escolha um minijogo para jogar neste turno ---");
            System.out.println("Digite (1) para escolher a Roleta Especial!");
            System.out.println("Digite (2) para escolher o Codificador!");
            System.out.println("Digite (3) para escolher o Decifrador!");
            try {
                int minijogo = tec.nextInt();
                if (minijogo < 1 || minijogo > 3) {
                    System.out.println("Essa opção é inválida, digite apenas '1', '2' ou '3'.");
                    return -1; // Retorna -1 para mostrar uma escolha que não existe
                }
                return minijogo;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                tec.nextLine(); // Evitar loop infinito
                return -1; // Retorna -1 para mostrar uma escolha que não existe
            }
        }


    public void aplicarEstrategia(String palavraSecreta, Set<Character> letrasBloqueadas) {
        System.out.println(this.nome + " está aplicando sua estratégia...");
    }

    public ArrayList<Character> getLetrasAcertadas() {
        return letrasAcertadas;
    }

    public void acertarLetra(char letra) {
        if (!letrasAcertadas.contains(Character.valueOf(letra))) {
            letrasAcertadas.add(Character.valueOf(letra));
            if (letrasAcertadas.size() == 0)
                System.out.println(this.nome + " acertou uma letra");
            else
                System.out.println(this.nome + " mais uma letra");
        }
    }
}