package prr.terminals;

public class IdleState extends TerminalState {

    public IdleState(Terminal terminal) {
        super(terminal);
    }

    @Override
    public String toString() {
        return "IDLE";
    }
    
}
