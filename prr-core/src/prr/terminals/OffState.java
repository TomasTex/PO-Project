package prr.terminals;

public class OffState extends TerminalState {

    public OffState(Terminal terminal) {
        super(terminal);
    }

    @Override
    public void onNormalEnable() {
        setLastState(this);
        getTerminal().setState(new IdleState(getTerminal()));
    }

    @Override
    public void onSilentEnable() {
        setLastState(this);
        getTerminal().setState(new SilentState(getTerminal()));
    }

    @Override
    public void onDisable() {
        // Doesn't do anything. Terminal is already off.
    }

    @Override
    public void onIdle() {
        // Doesn't do anything. Terminal is not on.
    }

    @Override
    public void onSilence() {
        // Doesn't do anything. Terminal is not on.
    }

    @Override
    public void onCommunicationStart() {
        // Doesn't do anything. Terminal is not on.
    }

    @Override
    public void onCommunicationEnd() {
        // Doesn't do anything. Terminal is not on.
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
