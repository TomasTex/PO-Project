package prr.terminals;

public class BusyState extends TerminalState {

    public BusyState(Terminal terminal) {
        super(terminal);
    }

    @Override
    public String toString() {
        return "BUSY";
    }
    
}
