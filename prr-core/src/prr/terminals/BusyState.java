package prr.terminals;

import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.NullCommunicationException;

public class BusyState extends TerminalState {

    public BusyState(Terminal terminal, TerminalState lastState) {
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
        // Doesn't do anything. Terminal cannot be turned off while a communication is still ongoing.
        throw new IllegalTerminalStateChangeException(this, new OffState(getTerminal(), this));
    }

    @Override
    public void onIdle() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal cannot switch states until the current communication ends.
        throw new IllegalTerminalStateChangeException(this, new IdleState(getTerminal(), this));
    }

    @Override
    public void onSilence() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal cannot switch states until the current communication ends.
        throw new IllegalTerminalStateChangeException(this, new SilentState(getTerminal(), this));   
    }

    @Override
    public void onCommunicationStart() throws IllegalTerminalStateChangeException {
        // Doesn't do anything. Terminal cannot process more than one communication at once.
        throw new IllegalTerminalStateChangeException(this, new BusyState(getTerminal(), this));
    }

    @Override
    public void onCommunicationEnd() throws IllegalTerminalStateChangeException {
        switch (getLastStateAsString()) {
            case "IDLE" -> getTerminal().setState(new IdleState(getTerminal(), this));
            case "SILENCE" -> getTerminal().setState(new SilentState(getTerminal(), this));
            default -> System.out.println("[DEBUG] Unknown last state (" + getLastStateAsString() + ") in BusyState.java!");
        }
        
    }

    @Override
    public boolean canStartCommunication() {
        return false;
    }

    @Override
    public boolean canEndCurrentCommunication() throws NullCommunicationException {
        if (getTerminal().getCurrentCommunication().getSender().equals(getTerminal())) {
            return true;
        } return false;
    }

    @Override
    public String toString() {
        return "BUSY";
    }
    
}
