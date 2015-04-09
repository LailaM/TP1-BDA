import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class BancoDados {
	private Connection connectionMysql = null;
	private Connection connectionPostgreSql = null;
	private ResultSet resultset = null;
	
	public static final int TAM_BATCH = 100;//10000;

	private static final String CREATE_DATABASE_CENSO = 
			"CREATE DATABASE IF NOT EXISTS censo;";
	
    private static final String CREATE_TABLE_PESSOA = "CREATE TABLE pessoas (";
	private static String COLUMN_ID = "";
    private static final String OTHERS_COLUMNS =
    		"sexo bit(1) default NULL,"
    		+ "idade bit(7) default NULL,"
    		+ "renda bit(10) default NULL,"
    		+ "escolaridade bit(2) default NULL,"
    		+ "idioma bit(12) default NULL,"
    		+ "pais bit(8) default NULL,"
    		+ "localizador bit(24) default NULL, ";
    private static final String PRIMARY_KEY = "PRIMARY KEY (id))";
    private static final String AUTO_INCREMENT = " AUTO_INCREMENT=1";
    
    private static final String DROP_TABLE_PESSOA = 
    		"DROP TABLE IF EXISTS pessoas;";
    
    private static final String INSERT_INTO_PESSOAS = 
    		"INSERT INTO pessoas ("
    		+ "sexo, idade, renda, escolaridade, idioma, pais, localizador"
    		+ ") VALUES ("
    		+ "?, ?, ?, ?, ?, ?, ?"
    		+ ")";
    
    private static final String INSERT_INTO_PESSOAS_POSTGRE = 
    		"INSERT INTO pessoas ("
    		+ "sexo, idade, renda, escolaridade, idioma, pais, localizador"
    		+ ") VALUES ("
    		+ "cast(? as bit(1)), cast(? as bit(7)), cast(? as bit(10)), cast(? as bit(2)), cast(? as bit(12)), cast(? as bit(8)), cast(? as bit(24))"
    		+ ")";
    
    private static final String SELECT_1 = 
    		"SELECT pais, sexo, COUNT(*) "
			+ "FROM pessoas "
			+ "GROUP BY pais, sexo;";
    private static final String SELECT_2 = 
    		"SELECT pais, sexo, idade, COUNT(*) "
			+ "FROM pessoas "
			+ "GROUP BY pais, sexo , idade;";
    private static final String SELECT_3 = 
    		"SELECT pais, sexo, AVG(renda) "
			+ "FROM pessoas "
			+ "GROUP BY pais, sexo;";
    private static final String SELECT_4 = 
    		"SELECT pais, sexo, AVG(idade) "
			+ "FROM pessoas "
			+ "GROUP BY pais, sexo;";
    private static final String SELECT_5 = 
    		"SELECT pais, sexo, COUNT(*) "
			+ "FROM pessoas "
			+ "WHERE pais = 15 "
			+ "GROUP BY pais, sexo;";
    private static final String SELECT_6 = 
    		"SELECT pais, sexo, COUNT(*) "
			+ "FROM pessoas "
			+ "WHERE pais = 15 "
			+ "AND sexo = 1;";
    private static final String SELECT_7 = 
    		"SELECT pais, sexo, COUNT(*) "
			+ "FROM pessoas "
			+ "WHERE pais>=0 "
			+ "AND pais<=15 "
			+ "GROUP BY pais, sexo;";
    private static final String SELECT_8 = "";
    private static final String SELECT_9 = "";
    private static final String SELECT_10 = "";
	
	public void conectaMySql() {
		String servidorMySql = "jdbc:mysql://localhost/?rewriteBatchedStatements=true";
		String usuario = "root";
		String senha = "1234";
		String driver = "com.mysql.jdbc.Driver";
		try{
			Class.forName(driver);
			this.connectionMysql = DriverManager.getConnection(servidorMySql, usuario, senha); 
			this.connectionMysql.createStatement();
		}catch(Exception e){
			System.out.println("Erro: " + e.getMessage());
		}
		
	}

	public void conectaPostgreSql() {
		String servidorPostgreSql = "jdbc:postgresql://localhost/censo?rewriteBatchedStatements=true";
		String usuario = "postgres";
		String senha = "1234";
		String driver = "org.postgresql.Driver";
		try{
			Class.forName(driver);
			this.connectionPostgreSql = DriverManager.getConnection(servidorPostgreSql, usuario, senha); 
			this.connectionPostgreSql.createStatement();
			this.connectionPostgreSql.setAutoCommit(false);
		}catch(Exception e){
			System.out.println("Erro: " + e.getMessage());
		}
		
	}
	
	public boolean estaoConectados() {
		if(this.connectionMysql != null && this.connectionPostgreSql != null){
			return true;
		}else{
			return false;
		}
	}

	public void desconectarMySql() {
		try{
			this.connectionMysql.close();
		}catch (Exception e){
			System.out.println("Erro: " + e.getMessage());
		}
		
	}
	
	public void desconectarPostgreSql() {
		try{
			this.connectionPostgreSql.close();
		}catch (Exception e){
			System.out.println("Erro: " + e.getMessage());
		}
		
	}
	
	public void inicializarMySql() throws SQLException {
		criaBancoMySql();
		usaBancoMySql();
		deletaTabelaPessoaMySql();
		criaTabelaPessoaMySql();
	}
	
	public void inicializarPostgreSql() throws SQLException {
		//diferente do mySql, o banco precisa ser criado fora do java.
		//utilizacao do banco censo foi determinada durante a conexao.
		deletaTabelaPessoaPostgreSql();
		criaTabelaPessoaPostgreSql();
	}
	
	public void criaBancoMySql() throws SQLException{
		final PreparedStatement statement = connectionMysql.prepareStatement(CREATE_DATABASE_CENSO);
        statement.execute();
        statement.close();
	}
	
	public void usaBancoMySql() throws SQLException{
		final PreparedStatement statement = connectionMysql.prepareStatement("USE censo;");
        statement.execute();
        statement.close();
	}
	
	public void criaTabelaPessoaMySql() throws SQLException{
		COLUMN_ID = "id bigint NOT NULL auto_increment,";
		final PreparedStatement statement = connectionMysql.prepareStatement(
				CREATE_TABLE_PESSOA
				+ COLUMN_ID
				+ OTHERS_COLUMNS
				+ PRIMARY_KEY
				+ AUTO_INCREMENT);
        statement.execute();
        statement.close();
	}
	
	public void criaTabelaPessoaPostgreSql() throws SQLException{
		COLUMN_ID = "id SERIAL NOT NULL,";
		final PreparedStatement statement = connectionPostgreSql.prepareStatement(
				CREATE_TABLE_PESSOA
				+ COLUMN_ID
				+ OTHERS_COLUMNS
				+ PRIMARY_KEY);
        statement.execute();
        statement.close();
	}
	
	public void deletaTabelaPessoaMySql() throws SQLException{
		final PreparedStatement statement = connectionMysql.prepareStatement(DROP_TABLE_PESSOA);
        statement.execute();
        statement.close();
	}
	
	public void deletaTabelaPessoaPostgreSql() throws SQLException{
		final PreparedStatement statement = connectionPostgreSql.prepareStatement(DROP_TABLE_PESSOA);
        statement.execute();
        statement.close();
	}
	
	/**
	 * Insere buffer nos dois bancos: MySql e PostgreSql
	 */
	public void insereBuffer(ByteBuffer buffer) throws SQLException{
		while(buffer.hasRemaining()){
			final PreparedStatement statementMySql = connectionMysql.prepareStatement(INSERT_INTO_PESSOAS);
			final PreparedStatement statementPostgre = connectionPostgreSql.prepareStatement(INSERT_INTO_PESSOAS_POSTGRE);
	        
			for ( int i = 0; i < TAM_BATCH; i++ ) {
	        	Pessoa p = new Pessoa(buffer.getLong());
	        	
	        	statementMySql.setLong(1, p.getSexo());
	        	statementPostgre.setLong(1, p.getSexo());
	        	statementMySql.setLong(2, p.getIdade());
	        	statementPostgre.setLong(2, p.getIdade());
	        	statementMySql.setLong(3, p.getRenda());
	        	statementPostgre.setLong(3, p.getRenda());
	        	statementMySql.setLong(4, p.getEscolaridade());
	        	statementPostgre.setLong(4, p.getEscolaridade());
	        	statementMySql.setLong(5, p.getIdioma());
	        	statementPostgre.setLong(5, p.getIdioma());
	        	statementMySql.setLong(6, p.getPais());
	        	statementPostgre.setLong(6, p.getPais());
	        	statementMySql.setLong(7, p.getLocalizador());
	        	statementPostgre.setLong(7, p.getLocalizador());
	        	
	        	statementMySql.addBatch();
	        	statementPostgre.addBatch();
	        }
			statementMySql.executeBatch();
			statementPostgre.executeBatch();
	        connectionPostgreSql.commit();
	        
	        statementMySql.close();
	        statementPostgre.close();
		}
	}
	
	public void consulta1() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_1);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 1 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta2() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_2);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 2 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta3() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_3);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 3 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta4() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_4);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 4 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta5() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_5);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 5 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta6() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_6);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 6 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta7() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_7);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 7 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta8() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_8);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 8 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta9() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_9);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 9 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
	
	public void consulta10() throws SQLException{
		usaBancoMySql();
		long startTimer = System.currentTimeMillis();
		final PreparedStatement statement = connectionMysql.prepareStatement(SELECT_10);
        statement.execute();
        statement.close();
        long endTimer = System.currentTimeMillis();
		System.out.println("Tempo da consulta 10 no MySql: " + (endTimer - startTimer) + " milisegundos");
		
	}
}