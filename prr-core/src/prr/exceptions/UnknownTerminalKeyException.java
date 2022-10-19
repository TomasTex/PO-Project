package prr.exceptions;

public class UnknownTerminalKeyException extends Exception {

    private static final long serialVersionUID = 202208091753L;

    private String _key;

    public UnknownTerminalKeyException(String key) {
        _key = key;
    }
    
}
