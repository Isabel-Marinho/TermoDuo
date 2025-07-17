public abstract class Minigames {
    protected String nome;
    protected boolean concluido;
    protected int recompensa;

    public Minigames(String nome) {
        this.nome = nome;
        this.concluido = false;
        this.recompensa = 0; // Pontuação ajustada em cada minijogo
    }

    public String getNome() {
        return nome;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public int getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(int recompensa) {
        this.recompensa = recompensa;
    }

    public abstract void exibirRegras();

    public abstract void iniciar();

    public abstract boolean verificarVitoria();
}