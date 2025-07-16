import java.util.*;

public class Jogo implements InterfaceJogo {
    protected ArrayList<Jogador> jogadores;
    protected String palavraSecreta;
    protected Set<Character> letrasDesbloqueadas;
    protected boolean jogoTerminado;
    protected Jogador vencedor;
    protected Scanner scanner;

    public Jogo() {
        this.jogadores = new ArrayList<>();
        this.letrasDesbloqueadas = new HashSet<>();
        this.jogoTerminado = false;
        this.scanner = new Scanner(System.in);
    }

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

        return jogadores;
    }

    @Override
    public void jogarTurno(Jogador jogador) {
        jogador.pensarEstrategia();
        jogador.aplicarEstrategia();
    }

    @Override
    public boolean jogoAcabou() {
        return false;
    }

    public void pontuacao() {
    }
}