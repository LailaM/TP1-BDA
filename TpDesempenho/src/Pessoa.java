import java.util.Random;

public class Pessoa {
	long sexo;
	long idade;
	long renda;
	long escolaridade;
	long idioma;
	long pais;
	long localizador;
	
	/**
	 * Cria um tipo "Pessoa" de maneira aleatória.
	 */
	public Pessoa(){
		Random random = new Random();
		// Gera numeros aleatórios e elimina-se o sinal.
		this.sexo = ((random.nextLong()<< 1) >>> 1) % 2;
		this.pais = ((random.nextLong()<< 1) >>> 1) % 256;
		this.idade = ((random.nextLong()<< 1) >>> 1) % 128;
		this.renda = ((random.nextLong()<< 1) >>> 1) % 1024;
		this.escolaridade = ((random.nextLong()<< 1) >>> 1) % 4;
		this.idioma = ((random.nextLong()<< 1) >>> 1) % 4096;
		this.localizador = ((random.nextLong()<< 1) >>> 1) % 16777216;
	}
	
	/**
	 * Cria um tipo "Pessoa" a partir de um numero binario passado como parametro.
	 */
	public Pessoa(long pessoaBinario){
		long pessoa = pessoaBinario;
		if(pessoaBinario < 0){
			this.sexo = 1;
			pessoa = -1 * pessoa; //multiplica por -1 de novo por causa do complemento de 1.
		}
		else{
			this.sexo = 0;
		}
		this.pais = (pessoa >> 55) & 255L; // mascara com 8 1s
		this.idade = (pessoa >> 48) & 127L; // mascara com 7 1s
		this.renda = (pessoa >> 38) & 1023L; // mascara com 10 1s
		this.escolaridade = (pessoa >> 36) & 3L; // mascara com 2 1s
		this.idioma = (pessoa >> 24) & 4095L; // mascara com 12 1s
		this.localizador = pessoa & 16777215L; // mascara com 24 1s
	}
	
	/**
	 * Retorna o numero binario que representa a pessoa. 
	 * O numero binario é representado por um long.
	 * @return numero binario de 64 bits
	 */
	public long getPessoaBinario(){
		long pessoaBinario = 0L;
		pessoaBinario = pessoaBinario | (this.pais << 55);
		pessoaBinario = pessoaBinario | (this.idade << 48);
		pessoaBinario = pessoaBinario | (this.renda << 38);
		pessoaBinario = pessoaBinario | (this.escolaridade << 36);
		pessoaBinario = pessoaBinario | (this.idioma << 24);
		pessoaBinario = pessoaBinario | (this.localizador);
		if(this.sexo == 0)
			return pessoaBinario;
		else return -1*pessoaBinario;
	}
	
	/**
	 * Retorna o sexo de uma pessoa.
	 */
	public long getSexo(){
		return this.sexo;
	}
	
	/**
	 * Retorna a idade de uma pessoa.
	 */
	public long getIdade(){
		return this.idade;
	}
	
	/**
	 * Retorna a renda de uma pessoa.
	 */
	public long getRenda(){
		return this.renda;
	}
	
	/**
	 * Retorna a escolaridade de uma pessoa.
	 */
	public long getEscolaridade(){
		return this.escolaridade;
	}
	
	/**
	 * Retorna o idioma de uma pessoa.
	 */
	public long getIdioma(){
		return this.idioma;
	}
	
	/**
	 * Retorna o pais de uma pessoa.
	 */
	public long getPais(){
		return this.pais;
	}
	
	/**
	 * Retorna o numero localizador de uma pessoa.
	 */
	public long getLocalizador(){
		return this.localizador;
	}
	
}
