import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class Arquivo {
	
	public final static int TAMANHO_MEM = 100;//10000000;
	public final static int NUM_BUFFERS = 6;//600;
	public final static long TAMANHO_ARQ = 600;//6000000000L;
	public final static long INFINITO = Long.MAX_VALUE;
	
	public static RandomAccessFile arquivo = null;
	public static FileChannel aChannel = null;
	
	
	/**
	 * Cria arquivos temporarios para auxiliar a ordenação
	 * @throws IOException
	 */
	public static void criaArquivosTemp() throws IOException
    {
		for(int i =0; i<NUM_BUFFERS; i++){
			File file = new File("./arquivoTemp"+i+".bin");
			if (!file.exists()) {
				 file.createNewFile();
			}
		}
    }
	/**
	 * Apaga arquivos temporarios criados para a ordenação
	 * @throws IOException
	 */
	public static void apagaArquivosTemp() throws IOException
    {
		for(int i =0; i<NUM_BUFFERS; i++){
			File file = new File("./arquivoTemp"+i+".bin");
			if (file.exists()) {
				 file.delete();
			}
		}
    }
	
	/**
     * Abre um arquivo para escrita usando nio.
     */
	public static void abreArquivoBinario(String nomeArquivo) throws IOException
    {
        arquivo = new RandomAccessFile (nomeArquivo, "rw");
        aChannel = arquivo.getChannel();
    }
	
	/**
	 * Fecha o arquivo que foi aberto para escrita usando nio.
	 */
    public static void fechaArquivoBinario() throws IOException
    {
        aChannel.close();
        arquivo.close();
    }
	
    /**
	 * Imprime um buffer de bytes no arquivo aberto para escrita
	 */
    public static void imprimeBufferArquivoBinario(ByteBuffer buffer) throws IOException
    {
    	aChannel.write(buffer);
    	buffer.rewind();
    }
    
	/**
     * Le uma lista de longs em um arquivo usando nio e imprime o resultado na tela.
     */
	public static void leArquivoBinario(String nomeArquivo) throws IOException
    {
        RandomAccessFile arquivo = new RandomAccessFile (nomeArquivo, "r");
        FileChannel aChannel = arquivo.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(8 * TAMANHO_MEM);
        while(aChannel.read(buffer) > 0)
        {
            buffer.flip();
            while (buffer.hasRemaining())
            {
            	Pessoa p = new Pessoa(buffer.getLong());
                System.out.println(
                		"sexo: " + p.getSexo() + 
                		", idade: " +p.getIdade() + 
                		", Renda: " +p.getRenda() + 
                		", Escolaridade: " +p.getEscolaridade() + 
                		", Idioma: " +p.getIdioma() + 
                		", Pais: " +p.getPais() + 
                		", Localizador: " +p.getLocalizador()
                		);
            }
            buffer.clear(); 
        }
        aChannel.close();
        arquivo.close();
    }
	
	/**
     * Pesquisa por um determinado pais e sexo no arquivo binário não ordenado.
     * @return total encontrado.
	 */
	public static long pesquisaArquivoBinario(String nomeArquivo, int pais, int sexo) throws IOException
    {
        RandomAccessFile arquivo = new RandomAccessFile (nomeArquivo, "r");
        FileChannel aChannel = arquivo.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(8 * TAMANHO_MEM);
        long total = 0;
        while(aChannel.read(buffer) > 0)
        {
            buffer.flip();
            while (buffer.hasRemaining())
            {
            	Pessoa p = new Pessoa(buffer.getLong());
            	if(p.getSexo() == sexo && p.getPais() == pais)
            		total++;
            }
            buffer.clear(); 
        }
        aChannel.close();
        arquivo.close();
        return total;
    }

	/** Ordena arquivo de longs. 
     *  Adaptado do código "External Sort" de Gregory R. Everitt.
     *  Ver licensa em anexo.
	 * @throws IOException 
     */
	public static void escreveArquivoBinarioOrdenado(String nomeArquivo) throws IOException {
		
		int idBuffer = 0; //para o nome dos arquivos temporários
		long[] buffer = new long[TAMANHO_MEM];
		
		InputStream entrada = null;
		try {
			entrada = Files.newInputStream(Paths.get(nomeArquivo));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataInputStream dadosEntrada = new DataInputStream(entrada);

		for( int i = 0; i<NUM_BUFFERS; i++) {
			
			// carrega buffer
			for(int j=0; j<TAMANHO_MEM; j++){
				buffer[j]= dadosEntrada.readLong();
			}

			long[] tempBuffer = new long[TAMANHO_MEM];
			long[] bufferOrdenado = Ordena(TAMANHO_MEM, buffer, tempBuffer);

			// imprime buffer ordenado em arquivo temporario
			OutputStream temp = null;
			try {
				temp = Files.newOutputStream(Paths.get("./arquivoTemp"+idBuffer+".bin"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			DataOutputStream dadosTemp = new DataOutputStream(temp);

			for (long j : bufferOrdenado) { // imprime os elementos do buffer no arquivo
				dadosTemp.writeLong(j);
			}

			dadosTemp.close();
			idBuffer++;
		}
		
		// carrega os primeiros números de cada buffer
		long[] primeirosNum = new long[NUM_BUFFERS];
		InputStream[] bEntradas = new InputStream[NUM_BUFFERS]; // um Stream para cada buffer
		DataInputStream[] bDadosEntrada = new DataInputStream[NUM_BUFFERS];
		
		idBuffer = 0;
		for(int i = 0; i < NUM_BUFFERS; i++){
			
			try {
				bEntradas[i] = Files.newInputStream(Paths.get("./arquivoTemp"+idBuffer+".bin"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bDadosEntrada[i] = new DataInputStream(bEntradas[i]);

			primeirosNum[i] = bDadosEntrada[i].readLong();
			idBuffer++;
		}

		// imprime os números de cada buffer de maneira ordenada
		OutputStream saidaOrdenada = null;
		try {
			saidaOrdenada = Files.newOutputStream(Paths.get("BancoOrdenado.bin"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DataOutputStream dadosSaidaOrdenada = new DataOutputStream(saidaOrdenada);

		for (long i = 0; i<TAMANHO_ARQ; i++) {
			long vMin = primeirosNum[0]; //valor
			int iMin = 0;//indice do arquivo
			for (int j = 0; j<NUM_BUFFERS; j++) {
				if (vMin > primeirosNum[j]){
					vMin = primeirosNum[j];
					iMin = j;
				}

			}
			dadosSaidaOrdenada.writeLong(vMin); //imprime menor valor
			
			try{
				primeirosNum[iMin] = bDadosEntrada[iMin].readLong();
			}
			catch(IOException e){
				primeirosNum[iMin] = INFINITO;
			}
		}
		dadosSaidaOrdenada.close();

	}
    
	/**
	 * Ordena um array de longs que está na memória através do Merge Sort
	 */
	private static long[] Ordena(int n, long[] buffer, long[] tempBuff) {

		for(int tamanho = 1; tamanho < n; tamanho = 2 * tamanho) {
			for(int i= 0; i < n; i = i + 2 * tamanho) {
				Merge(buffer, i, Math.min(i+tamanho, n), Math.min(i+2*tamanho, n), tempBuff);
			}
			buffer = Arrays.copyOf(tempBuff, tempBuff.length);
		}
		return buffer;
	}
	
	/**
	 * Etapa de merge do Merge Sort
	 */
	private static void Merge(long[] A, int esquerda, int direita, long fim, long[] B)
	{
		int i0 = esquerda;
		int i1 = direita;

		/* enquanto existir elementos na lista da esquerda e/ou direita */
		for (int j = esquerda; j < fim; j++)
		{
			/* se a cabeca da lista da esquerda existe e é <= a cabeca da lista da direita */
			if (i0 < direita && (i1 >= fim || A[i0] <= A[i1]))
			{
				B[j] = A[i0];
				i0++;
			}
			else
			{
				B[j] = A[i1];
				i1++;
			}
		}
	}
	
	public static void consulta1(String nomeArquivo) throws IOException {
		long startTimer = System.currentTimeMillis();
		//System.out.println("pais|sexo|count(*)");
		for(int idpais = 0; idpais < 256; idpais++){
			for( int idsexo = 0; idsexo < 2; idsexo++){
				long cont = pesquisaArquivoBinario(nomeArquivo, idpais, idsexo);
				//System.out.println(idpais +"|"+ idsexo +"|"+ cont);
			}
		}
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 1 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta1_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 1 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta2(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 2 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta2_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 2 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta3(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 3 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta3_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 3 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta4(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 4 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta4_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 4 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta5(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 5 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta5_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 5 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta6(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 6 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta6_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 6 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta7(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 7 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta7_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 7 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta8(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 8 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta8_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 8 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta9(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 9 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");	
	}
	
	public static void consulta9_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 9 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta10(String nomeArquivo) {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 10 no arquivo binario nao ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
	
	public static void consulta10_Ordenado() {
		long startTimer = System.currentTimeMillis();
		
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 10 no arquivo binario ordenado: " + (endTimer - startTimer) + " milisegundos");
	}
}
