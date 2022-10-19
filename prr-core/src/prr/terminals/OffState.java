package prr.terminals;

public class OffState extends TerminalState {

    public OffState(Terminal terminal) {
        super(terminal);
    }

    @Override
    public String toString() {
        return "OFF";
    }
    
}
