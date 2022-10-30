package prr.terminals;

import java.io.Serializable;

public abstract class TerminalState implements Serializable {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    private Terminal _terminal;

    private TerminalState _lastState;

    public TerminalState(Terminal terminal) { _terminal = terminal; }

    protected Terminal getTerminal() {
        return _terminal;
    }

    protected String getLastStateAsString() {
        return _lastState.toString();
    }

    protected void setLastState(TerminalState lastState) {
        _lastState = lastState;
    }

    public String getTerminalState() {
        return getClass().getName();
    }

    public abstract boolean canStartCommunication();
    public abstract boolean canEndCurrentCommunication();

    public abstract void onNormalEnable();
    public abstract void onSilentEnable();
    public abstract void onDisable();
    public abstract void onIdle();
    public abstract void onSilence();
    public abstract void onCommunicationStart();
    public abstract void onCommunicationEnd();
    
}
