import java.util.Random;

public class Pais {
	int populacao;
	int populacaoFem;
	int populacaoMasc;
	
	public Pais(){

	}
	
	public void setPopulacao(long totalParaAtribuir){
		this.populacao = (int)totalParaAtribuir;
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
		int dif = max - min + 1;
		this.populacao = random.nextInt(dif) + min;
	}
	
	public void randomSetPopulacaoMasc(){
		int max, min, dif;
		max = (int)((this.populacao/2)*1.2);
		min = (int)((this.populacao/2)*0.75);
		dif = max - min +1;
		Random random = new Random();
		this.populacaoMasc = random.nextInt(dif) + min;
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
