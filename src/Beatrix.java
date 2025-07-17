class Beatrix extends Personagem {
    public Beatrix(String nomeJogador) {
        super(nomeJogador, "Beatrix", "Inteligente, baixinha e irritada", 20, "Acerta dois números do minigame de codificação assim que começa o minijogo.", "Beatrix");
    }

    @Override
    public String modificaRegra(){
        System.out.println(nomePersonagem + ": Você começa o Decifrador com dois números já acertados!");
    }
}