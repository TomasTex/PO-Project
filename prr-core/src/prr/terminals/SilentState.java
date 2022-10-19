package prr.terminals;

public class SilentState extends TerminalState {

    public SilentState(Terminal terminal) {
        super(terminal);
    }

    @Override
    public String toString() {
        return "SILENCE";
    }
    
}
