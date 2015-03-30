import java.util.Random;

public class Pais {
	int populacao;
	int populacaoFem;
	int populacaoMasc;
	
	public Pais(){

	}
	
	public void randomSetPopulacao(long totalPessoasMundo, long totalParaAtribuir){
		int max, min;
		min = (int)totalPessoasMundo/6000000;;
		if(totalParaAtribuir > totalPessoasMundo/2){
			max = (int)totalPessoasMundo/6;
		}
		else{
			if(totalParaAtribuir > totalPessoasMundo/6){
				max = (int)totalPessoasMundo/60;
			}
			else{
				if(totalParaAtribuir > totalPessoasMundo/60){
					max = (int)totalPessoasMundo/600;
				}
				else{
					if(totalParaAtribuir > totalPessoasMundo/600){
						max = (int)totalPessoasMundo/6000;
					}
					max = (int)totalPessoasMundo/60000;
				}
			}
		}
		Random random = new Random();
		this.populacao = random.nextInt(max) + min;
	}
	
	public void randomSetPopulacaoMasc(){
		Random random = new Random();
		this.populacaoMasc = random.nextInt( (int)((this.populacao/2)*1.2) ) + (int)((this.populacao/2)*0.75);
	}
	
	public void setPopulacaoFem(int populacaoMasc){
		this.populacaoFem = this.populacao - populacaoMasc;
	}
	
	public int getPopulacao(){
		return populacao;
		
	}
	
	public int getPopulacaoFem(){
		return populacaoFem;
		
	}
	
	public int getPopulacaoMasc(){
		return populacaoMasc;
		
	}
	
}
