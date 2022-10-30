package prr.terminals;

public class SilentState extends TerminalState {

    public SilentState(Terminal terminal) {
        super(terminal);
    }

    @Override
    public void onNormalEnable() {
        // Doesn't do anything. Terminal is already on.
    }

    @Override
    public void onSilentEnable() {
        // Doesn't do anything. Terminal is already on.
    }

    @Override
    public void onDisable() {
        setLastState(this);
        getTerminal().setState(new OffState(getTerminal()));
    }

    @Override
    public void onIdle() {
        setLastState(this);
        getTerminal().setState(new IdleState(getTerminal()));
    }

    @Override
    public void onSilence() {
        // Doesn't do anything. Terminal is already silent.
    }

    @Override
    public void onCommunicationStart() {
        setLastState(this);
        getTerminal().setState(new BusyState(getTerminal()));
    }

    @Override
    public void onCommunicationEnd() {
        // Doesn't do anything. Silent terminals do not have ongoing communications.   
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
        return "SILENCE";
    }
    
}
