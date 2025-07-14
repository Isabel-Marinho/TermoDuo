public abstract class Minigames{
    protected String nome;
    protected boolean concluido;
    protected int recompensa; // Pontos ou vantagens concedidas

    // Construtor
    public Minigames(String nome) {
        this.nome = nome;
        this.concluido = false;
        this.recompensa = 0;
    }

    // Métodos abstratos (devem ser implementados pelas classes filhas)
    public abstract void iniciar();         // Inicia o minijogo

    public abstract boolean verificarVitoria(); // Verifica se o jogador venceu

    public abstract void exibirRegras();    // Mostra as regras do minijogo

    // Métodos concretos (compartilhados por todos os minijogos)
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