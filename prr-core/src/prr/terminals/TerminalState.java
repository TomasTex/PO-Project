package prr.terminals;

import java.io.Serializable;

import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.NullCommunicationException;
import prr.exceptions.TerminalAlreadyIdleException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadySilentException;

public abstract class TerminalState implements Serializable {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    private Terminal _terminal;

    private TerminalState _lastState;

    public TerminalState(Terminal terminal, TerminalState lastState) {
        _lastState = lastState;
        _terminal = terminal; 
    }

    protected Terminal getTerminal() {
        return _terminal;
    }

    protected String getLastStateAsString() {
        return _lastState.toString();
    }

    public TerminalState getLastState() {
        return _lastState;
    }

    public String getTerminalState() {
        return getClass().getName();
    }

    public abstract boolean canStartCommunication();
    public abstract boolean canEndCurrentCommunication() throws NullCommunicationException;

    public abstract void onNormalEnable() throws IllegalTerminalStateChangeException;
    public abstract void onSilentEnable() throws IllegalTerminalStateChangeException;
    public abstract void onDisable() throws IllegalTerminalStateChangeException, TerminalAlreadyOffException;
    public abstract void onIdle() throws IllegalTerminalStateChangeException, TerminalAlreadyIdleException;
    public abstract void onSilence() throws IllegalTerminalStateChangeException, TerminalAlreadySilentException;
    public abstract void onCommunicationStart() throws IllegalTerminalStateChangeException;
    public abstract void onCommunicationEnd() throws IllegalTerminalStateChangeException;
    
}
