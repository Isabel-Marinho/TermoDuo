import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Jogador {
    protected String nome;
    protected int pontuacao;
    protected Personagem personagemEscolhido;
    protected ArrayList<Character> letrasAcertadas; // Para o jogo principal

    // Para controlar personagens já escolhidos
    private static Map<Integer, Boolean> personagensDisponiveis = new HashMap<>();

    static {
        // Inicializa a disponibilidade de personagens. ID 1 para Sova, 2 para Beatrix, 3 para Lucky.
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

    public boolean setPersonagem(int idPersonagem) {
        if (personagensDisponiveis.getOrDefault(idPersonagem, false)) {
            // Instancia o personagem específico com base no ID
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
                    System.out.println("Personagem com ID " + idPersonagem + " não existe.");
                    return false;
            }
            personagensDisponiveis.put(idPersonagem, false); // Marca como indisponível
            System.out.println(this.nome + " escolheu " + this.personagemEscolhido.getNomePersonagem() + "!");
            this.personagemEscolhido.contarHistoria(); // Apresenta as características e vantagens
            return true;
        } else {
            System.out.println("Personagem com ID " + idPersonagem + " já foi escolhido ou é inválido.");
            return false;
        }
    }

    public void pensarEstrategia() { //Jogada no turno
        System.out.println(this.nome + " está pensando em sua próxima jogada...");
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
                System.out.println(this.nome + " mais uma uma letra");
        }
    }
}