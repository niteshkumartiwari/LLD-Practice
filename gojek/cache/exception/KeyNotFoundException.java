package gojek.cache.exception;

public class KeyNotFoundException extends Exception{
    public KeyNotFoundException(String key) {
        super("Not found key: "+ key);
    }
}
