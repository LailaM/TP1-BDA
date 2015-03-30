
public class PopulacaoMundial {
	
	Pais[] paises;
	long populacaoMundial;
	
	public PopulacaoMundial(long populacaoMundial){
		this.paises = new Pais[256];
		this.populacaoMundial = populacaoMundial;
	}
	
	public void inicializaPaises(){
		long totalParaAtribuir = this.populacaoMundial;
		for(int i = 0; i < 256; i++){
			paises[i].randomSetPopulacao(this.populacaoMundial, totalParaAtribuir);
			paises[i].randomSetPopulacaoMasc();
			paises[i].setPopulacaoFem(paises[i].getPopulacaoMasc());
			totalParaAtribuir -= paises[i].getPopulacao();
		}
	}
	
	public int getPopulacaoPais(int idPais){
		return paises[idPais].getPopulacao();
	}
}
