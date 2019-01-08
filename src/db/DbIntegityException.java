package db;

public class DbIntegityException extends RuntimeException{
    
    public DbIntegityException(String mensagem){
        super(mensagem);
    }
    
}
