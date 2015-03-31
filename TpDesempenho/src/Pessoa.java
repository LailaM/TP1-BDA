import java.util.Random;


public class Pessoa {
	long sexo;
	long idade;
	long renda;
	long escolaridade;
	long idioma;
	long pais;
	long localizador;
	
	public Pessoa(long sexo, long pais){
		this.sexo = sexo;
		this.pais = pais;
		
		Random random = new Random();
		this.idade = ((random.nextLong()<< 1) >>> 1) % 127;
		this.renda = ((random.nextLong()<< 1) >>> 1) % 1023;
		this.escolaridade = ((random.nextLong()<< 1) >>> 1) % 3;
		this.idioma = ((random.nextLong()<< 1) >>> 1) % 4095;
		this.localizador = ((random.nextLong()<< 1) >>> 1) % 16777215;
	}
	
	public long getSexo(){
		return this.sexo;
	}
	public long getIdade(){
		return this.idade;
	}
	public long getRenda(){
		return this.renda;
	}
	public long getEscolaridade(){
		return this.escolaridade;
	}
	public long getIdioma(){
		return this.idioma;
	}
	public long getPais(){
		return this.pais;
	}
	public long getLocalizador(){
		return this.localizador;
	}
	
	public long getPessoaBinario(){
		long pessoa = 0L;
		pessoa = pessoa | (this.idade << 56);
		pessoa = pessoa | (this.renda << 46);
		pessoa = pessoa | (this.escolaridade << 44);
		pessoa = pessoa | (this.idioma << 32);
		pessoa = pessoa | (this.pais << 24);
		pessoa = pessoa | (this.localizador);
		if(this.sexo == 0)
			return pessoa;
		else return -1*pessoa;
	}
}
