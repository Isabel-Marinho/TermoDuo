class Lucky extends Personagem {
    public Lucky(String nomeJogador) {
        super(nomeJogador, "Lucky", "Sortuda, engraçada, mão de vaca", 77, "Maior chance de pegar o prêmio especial da roleta (+20%)", "Lucky");
    }


    public String modificaRegra(){
        System.out.println(nomePersonagem + ": Sua sorte na Roleta Especial está turbinada!");
    }
}