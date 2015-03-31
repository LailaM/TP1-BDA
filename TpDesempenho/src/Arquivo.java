import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class Arquivo {
	
	
	public final static int TAMANHO_MEM = 100;//10000000;
	public final static int NUM_BUFFERS = 6;//600;
	public final static long TAMANHO_ARQ = 600;//6000000000L;
	public final static long INFINITO = Long.MAX_VALUE;
	 
    public Arquivo(long[] buffer, String nomeArquivo) throws IOException {
 
    	escreveArquivoBinário(buffer,nomeArquivo);
 
    }
 
    /**
     * Escreve uma lista de longs em um arquivo usando nio.
     */
    public static void escreveArquivoBinário(long[] buffer, String nomeArquivo) {
 
        Path caminhoArquivo = Paths.get(nomeArquivo);
        
        try (BufferedOutputStream bStream = new BufferedOutputStream(Files.newOutputStream(caminhoArquivo))) {
        	 
        	DataOutputStream dados = new DataOutputStream(bStream);
        	for (long i : buffer) {
            	dados.writeLong(i);
            }
    	 
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
    
    /**
     * Le uma lista de longs em um arquivo usando nio.
     */
    public static long[] leArquivoBinário(String nomeArquivo) {
    	long[] buffer = new long[TAMANHO_MEM];
    	
        Path caminhoArquivo = Paths.get(nomeArquivo);
        
        try (BufferedInputStream bStream = new BufferedInputStream(Files.newInputStream(caminhoArquivo))) {
        	 
        	DataInputStream dados = new DataInputStream(bStream);
        	for (int i = 0; i < buffer.length; i++) {
            	buffer[i] = dados.readLong();
            }
    	 
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
    
    /* Ordena arquivo de longs. 
     * Adaptado do código "External Sort" de Gregory R. Everitt.
     * Ver licensa em anexo.
    */
    public void escreveArquivoBinárioOrdenado(String nomeArquivo) throws FileNotFoundException {
		
		int idBuffer = 0; //para o nome dos arquivos temporários
		long[] buffer = new long[TAMANHO_MEM];
		Scanner aScanner = new Scanner(new File(nomeArquivo));

		while (aScanner.hasNext()) {
			
			// carrega buffer
			for(int i=0; i<TAMANHO_MEM; i++){
				buffer[i]= aScanner.nextInt();
			}

			long[] tempBuffer = new long[TAMANHO_MEM];
			long[] bufferOrdenado = Ordena(TAMANHO_MEM, buffer, tempBuffer);

			// imprime buffer ordenado em arquivo temporario
			PrintStream arquivoTemp = null;
			try {
				arquivoTemp = new PrintStream("arquivoTemp"+idBuffer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			PrintStream saida = new PrintStream(arquivoTemp);

			for (long i : bufferOrdenado) { // imprime os elementos do buffer no arquivo
				saida.println(i);
			}

			saida.close();
			idBuffer++;
		}

		// carrega os primeiros números de cada buffer
		long[] primeirosNum = new long[NUM_BUFFERS];
		Scanner[] bScanners = new Scanner[NUM_BUFFERS]; // um Scanner para cada buffer
		idBuffer = 0;
		for (int i = 0; i<NUM_BUFFERS; i++) {
			bScanners[i] = new Scanner(new File("arquivoTemp"+idBuffer));
			if (bScanners[i].hasNextInt())
				primeirosNum[i] = bScanners[i].nextInt();
			else
				primeirosNum[i] = INFINITO;
			idBuffer++;
		}

		// imprime os números de cada buffer de maneira ordenada
		PrintStream saida = null;
		try {
			saida = new PrintStream(nomeArquivo + "Ordenado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream saidaOrdenada = new PrintStream(saida);

		for (long i = 0; i<TAMANHO_ARQ; i++) {
			long vMin = primeirosNum[0]; //valor
			int iMin = 0;//indice
			for (int j = 0; j<NUM_BUFFERS; j++) {
				if (vMin > primeirosNum[j]){
					vMin = primeirosNum[j];
					iMin = j;
				}

			}
			saidaOrdenada.println(vMin); //imprime menor valor
			if (bScanners[iMin].hasNextInt())
				primeirosNum[iMin] = bScanners[iMin].nextInt();
			else
				primeirosNum[iMin] = INFINITO;
		}
		saidaOrdenada.close();

	}
    
	public static long[] Ordena(int n, long[] buffer, long[] tempBuff) {

		for(int tamanho = 1; tamanho < n; tamanho = 2 * tamanho) {
			for(int i= 0; i < n; i = i + 2 * tamanho) {
				Merge(buffer, i, Math.min(i+tamanho, n), Math.min(i+2*tamanho, n), tempBuff);
			}
			buffer = Arrays.copyOf(tempBuff, tempBuff.length);
		}
		return buffer;
	}
	
	public static void Merge(long[] A, int esquerda, int direita, long fim, long[] B)
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
}

