import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String nomeBanco = "./Banco.bin";
		Arquivo.escreveArquivoBinario(nomeBanco);
		Arquivo.criaArquivosTemp();
		Arquivo.escreveArquivoBinarioOrdenado(nomeBanco);
		Arquivo.apagaArquivosTemp();
        //leArquivoBinario(nomeBanco);
		
		/*Pessoa p = new Pessoa();
		System.out.println(p.getSexo() + ", " +p.getIdade() + ", " +p.getRenda() + ", " +p.getEscolaridade() + ", " +p.getIdioma() + ", " +p.getPais() + ", " +p.getLocalizador() );
		Pessoa p2 = new Pessoa(p.getPessoaBinario());
		System.out.println(p.getPessoaBinario());
		System.out.println(p2.getSexo() + ", " +p2.getIdade() + ", " +p2.getRenda() + ", " +p2.getEscolaridade() + ", " +p2.getIdioma() + ", " +p2.getPais() + ", " +p2.getLocalizador() );
		*/
	}
}
