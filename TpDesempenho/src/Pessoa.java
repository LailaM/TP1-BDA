import java.util.Random;

public class Pessoa {
	long sexo;
	long idade;
	long renda;
	long escolaridade;
	long idioma;
	long pais;
	long localizador;
	
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
	
	//Arrumar
	public Pessoa(long pessoaBinario){
		if(pessoaBinario < 0){
			this.sexo = 0;
		}
		else{
			this.sexo = 1;
		}
		this.pais = (pessoaBinario >> 55) & 255L; // mascara com 8 1s
		this.idade = (pessoaBinario >> 48) & 127L; // mascara com 7 1s
		this.renda = (pessoaBinario >> 38) & 1023L; // mascara com 10 1s
		this.escolaridade = (pessoaBinario >> 36) & 3L; // mascara com 2 1s
		this.idioma = (pessoaBinario >> 24) & 4095L; // mascara com 12 1s
		this.localizador = pessoaBinario & 16777215L; // mascara com 24 1s
		System.out.println(16777215L);
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
}
