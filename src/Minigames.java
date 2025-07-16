public abstract class Minigames{
    protected String nome;
    protected boolean concluido;
    protected int recompensa;


    public Minigames(String nome) {
        this.nome = nome;
        this.concluido = false;
        this.recompensa = 0;
    }

    public abstract void iniciar();
    public abstract boolean verificarVitoria();
    public abstract void exibirRegras();

    public String getNome() {
        return nome;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public int getRecompensa() {
        return recompensa;
    }
}