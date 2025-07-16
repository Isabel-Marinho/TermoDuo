import java.util.*;

public abstract class Jogador {
    protected String nome;
    protected HashSet<Character> letrasDesbloqueadas;
    protected String palavraSecreta;

    public Jogador(String nome) {
        this.nome = nome;
        this.letrasDesbloqueadas = new HashSet<>();
    }

    public abstract void pensarEstrategia();
    public abstract void aplicarEstrategia();

    public String getNome() {
        return nome;
    }
}
