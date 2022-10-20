package prr.exceptions;

public class InvalidFriendKeyException extends Exception {

    private static final long serialVersionUID = 202208091753L;

    private String _key;

    public InvalidFriendKeyException(String key) {
        _key = key;
    }
    
}
