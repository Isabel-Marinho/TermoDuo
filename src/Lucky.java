class Lucky extends Personagem {
    public Lucky(String nomeJogador) {
        super(nomeJogador, "Lucky", "Sortuda, engraçada, mão de vaca", 77, "Maior chance de pegar o prêmio especial da roleta (+20%)", "Lucky");
    }


    public void modificaRegra(RoletaEspecial roleta){
        System.out.println("\n--- VANTAGEM DE " + nomePersonagem.toUpperCase() + " ---");
        System.out.println("Como " + nomePersonagem + ", sua sorte aumentará na Roleta Especial!");
        roleta.ajustarProbabilidadesParaLucky(); // Chama o metodo da roleta para ajustar as chances
    }
}