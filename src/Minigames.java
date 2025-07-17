public abstract class Minigames {
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

    // Métodos set para atualizar o estado e recompensa, se necessário
    protected void setConcluido(boolean concluido) {
        this.concluido = concluido;
        if(getNome().equals("Codificador")||getNome().equals("Decodificador")){
            this.recompensa = 1;
        } else if (getNome().equals("Roleta")) {
            this.recompensa = //terminar codigo
        }
    }

    protected void setRecompensa(int recompensa) {
        this.recompensa = recompensa;
    }
}