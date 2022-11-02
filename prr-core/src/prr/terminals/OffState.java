package prr.terminals;

import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.TerminalAlreadyOffException;

public class OffState extends TerminalState {

    public OffState(Terminal terminal, TerminalState lastState) {
        super(terminal, lastState);
    }

    @Override
    public void onNormalEnable() throws IllegalTerminalStateChangeException {
        getTerminal().setState(new IdleState(getTerminal(), this));
    }

    @Override
    public void onSilentEnable() throws IllegalTerminalStateChangeException {
        getTerminal().setState(new SilentState(getTerminal(), this));
    }

    @Override
    public void onDisable() throws TerminalAlreadyOffException {
        // Doesn't do anything. Terminal is already off.
        throw new TerminalAlreadyOffException();
    }

    @Override
    public void onIdle() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal is not on.
        throw new IllegalTerminalStateChangeException(this, new IdleState(getTerminal(), this));
    }

    @Override
    public void onSilence() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal is not on.
        throw new IllegalTerminalStateChangeException(this, new SilentState(getTerminal(), this));
    }

    @Override
    public void onCommunicationStart() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal is not on.
        throw new IllegalTerminalStateChangeException(this, new BusyState(getTerminal(), this));
    }

    @Override
    public void onCommunicationEnd() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal is not on.
        throw new IllegalTerminalStateChangeException(this, new IdleState(getTerminal(), this));
    }

    @Override
    public boolean canStartCommunication() {
        return false;
    }

    @Override
    public boolean canEndCurrentCommunication() {
        return false;
    }

    @Override
    public String toString() {
        return "OFF";
    }
    
}
