package prr.exceptions;

import prr.terminals.TerminalState;

public class IllegalTerminalStateChangeException extends Exception {

    private static final long serialVersionUID = 202208091753L;

    private TerminalState _oldState;
    private TerminalState _newState;

    public IllegalTerminalStateChangeException(TerminalState oldState, TerminalState newState) {
        _oldState = oldState;
        _newState = newState;
    }

    public TerminalState getOldState() {
        return _oldState;
    }

    public TerminalState getNewState() {
        return _newState;
    }
    
}
