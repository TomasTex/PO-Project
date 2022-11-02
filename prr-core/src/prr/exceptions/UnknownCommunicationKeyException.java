package prr.exceptions;

public class UnknownCommunicationKeyException extends Exception {

    private static final long serialVersionUID = 202208091753L;

    private int _key;

    public UnknownCommunicationKeyException(int key) {
        _key = key;
    }
    
}