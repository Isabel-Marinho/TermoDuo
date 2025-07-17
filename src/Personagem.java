public abstract class Personagem extends Jogador {
    protected String nomePersonagem;
    protected String descricao;
    protected int idade;
    protected String regraModificada; // Descrição da regra que o personagem modifica

    public Personagem(String nomeJogador, String nomePersonagem, String descricao, int idade, String regraModificada) {
        super(nomeJogador);
        this.nomePersonagem = nomePersonagem;
        this.descricao = descricao;
        this.idade = idade;
        this.regraModificada = regraModificada;
    }

    public String getNomePersonagem() {
        return nomePersonagem;
    }

    public void contarHistoria() {
        System.out.println("\n--- Personagem Escolhido: " + nomePersonagem + " ---");
        System.out.println("Nome: " + nomePersonagem);
        System.out.println("Descrição: " + descricao);
        System.out.println("Idade: " + idade);
        System.out.println("Vantagem: " + regraModificada);
    }

    public abstract void modificaRegra();
}