package prr.exceptions;

public class UnknownTerminalStateException extends Exception {

    private static final long serialVersionUID = 202208091753L;

    private String _state;

    public UnknownTerminalStateException(String state) {
        _state = state;
    }
    
}
