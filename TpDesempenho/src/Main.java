import java.util.Random;



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PopulacaoMundial pop = new PopulacaoMundial(6000000000L);
		pop.inicializaPaises();
		
		/*for(int i = 0; i < 256; i++){
			System.out.println(pop.getPopulacaoPais(i) + "," + pop.getPopulacaoMascPais(i) + "," + pop.getPopulacaoFemPais(i));
		}*/
		
		long[] buffer = new long[Arquivo.TAMANHO_MEM];
		Random random = new Random();
		for(int i = 0; i < buffer.length; i++){
			int sexo = random.nextInt(2);
			int pais = random.nextInt(256);;
			Pessoa p = new Pessoa(sexo, pais);
			buffer[i] = p.getPessoaBinario();
		}
		
		Arquivo.escreveArquivoBinário(buffer, "./entrada.txt");
		buffer = Arquivo.leArquivoBinário("./entrada.txt");
		/*for (int i = 0; i < buffer.length; i++) {
			System.out.println(buffer[i]);
		}*/

		
		//System.out.println(p.getSexo() + ", " +p.getIdade() + ", " +p.getRenda() + ", " +p.getEscolaridade() + ", " +p.getIdioma() + ", " +p.getPais() + ", " +p.getLocalizador() );
		//System.out.println(p.getPessoaBinario());
		
	}

}
