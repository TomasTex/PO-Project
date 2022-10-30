package prr.terminals;

public class IdleState extends TerminalState {

    public IdleState(Terminal terminal) {
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
        getTerminal().setState(new OffState(getTerminal()));
    }

    @Override
    public void onIdle() {
        // Doesn't do anything. Terminal is already idle.
    }

    @Override
    public void onSilence() {
        setLastState(this);
        getTerminal().setState(new SilentState(getTerminal()));
    }

    @Override
    public void onCommunicationStart() {
        setLastState(this);
        getTerminal().setState(new BusyState(getTerminal()));
    }

    @Override
    public void onCommunicationEnd() {
        // Doesn't do anything. Idle terminals do not have ongoing communications.
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
