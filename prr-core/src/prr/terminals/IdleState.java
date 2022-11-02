package prr.terminals;

import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.TerminalAlreadyIdleException;

public class IdleState extends TerminalState {

    public IdleState(Terminal terminal, TerminalState lastState) {
        super(terminal, lastState);
    }

    @Override
    public void onNormalEnable() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal is already on.
        throw new IllegalTerminalStateChangeException(this, new IdleState(getTerminal(), this));
    }

    @Override
    public void onSilentEnable() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal is already on.
        throw new IllegalTerminalStateChangeException(this, new SilentState(getTerminal(), this));
    }

    @Override
    public void onDisable() throws IllegalTerminalStateChangeException {
        getTerminal().setState(new OffState(getTerminal(), this));
    }

    @Override
    public void onIdle() throws TerminalAlreadyIdleException {
        // Doesn't do anything. Terminal is already idle.
        throw new TerminalAlreadyIdleException();
    }

    @Override
    public void onSilence() throws IllegalTerminalStateChangeException {
        getTerminal().setState(new SilentState(getTerminal(), this));
    }

    @Override
    public void onCommunicationStart() throws IllegalTerminalStateChangeException {
        getTerminal().setState(new BusyState(getTerminal(), this));
    }

    @Override
    public void onCommunicationEnd() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Idle terminals do not have ongoing communications.
        throw new IllegalTerminalStateChangeException(this, new IdleState(getTerminal(), this));
    }

    @Override
    public boolean canStartCommunication() {
        return true;
    }

    @Override
    public boolean canEndCurrentCommunication() {
        return false;
    }
    
    @Override
    public String toString() {
        return "IDLE";
    }
}
