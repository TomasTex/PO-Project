package prr.clients;

import prr.exceptions.UnknownTerminalStateException;
import prr.terminals.Terminal;
import prr.terminals.TerminalState;

public interface TerminalObserver {
    
    public void update(Terminal terminal, TerminalState oldState, TerminalState newState) throws UnknownTerminalStateException;
}
