class Sova extends Personagem {
    public Sova(String nomeJogador) {
        super(nomeJogador, "Sova", "Russo, usa um arco", 30, "Tem frases menores para codificar");
    }

    @Override
    public void modificaRegra(){
        System.out.println(nomePersonagem + ": Sua frase para codificar é menor!");
    }
}