import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String nomeBanco = "./Banco.bin";
		Arquivo.escreveArquivoBinario(nomeBanco);
		Arquivo.criaArquivosTemp();
		Arquivo.escreveArquivoBinarioOrdenado(nomeBanco);
		Arquivo.apagaArquivosTemp();
		//Arquivo.leArquivoBinario(nomeBanco);
		
	}
}
