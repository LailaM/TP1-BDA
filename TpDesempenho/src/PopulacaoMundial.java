
public class PopulacaoMundial {
	
	Pais[] paises;
	long populacaoMundial;
	
	public PopulacaoMundial(long populacaoMundial){
		this.paises = new Pais[256];
		this.populacaoMundial = populacaoMundial;
		for(int i = 0; i < 256 ; i++)
		{
		    this.paises[i] = new Pais();
		}
	}
	
	public void inicializaPaises(){
		long totalParaAtribuir = this.populacaoMundial;
		for(int i = 0; i < 255; i++){
			paises[i].randomSetPopulacao(this.populacaoMundial, totalParaAtribuir);
			paises[i].randomSetPopulacaoMasc();
			paises[i].setPopulacaoFem(paises[i].getPopulacaoMasc());
			totalParaAtribuir -= paises[i].getPopulacao();
		}
		
		paises[255].setPopulacao(totalParaAtribuir);
		paises[255].randomSetPopulacaoMasc();
		paises[255].setPopulacaoFem(paises[255].getPopulacaoMasc());
	}
	
	public int getPopulacaoPais(int idPais){
		return paises[idPais].getPopulacao();
	}
	
	public int getPopulacaoFemPais(int idPais){
		return paises[idPais].getPopulacaoFem();
	}
	
	public int getPopulacaoMascPais(int idPais){
		return paises[idPais].getPopulacaoMasc();
	}
}
