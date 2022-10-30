package prr.terminals;

public class BusyState extends TerminalState {

    public BusyState(Terminal terminal) {
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
        // Doesn't do anything. Terminal cannot be turned off while a communication is still ongoing.
        
    }

    @Override
    public void onIdle() {
        // Doesn't do anything. Terminal cannot switch states until the current communication ends.
        
    }

    @Override
    public void onSilence() {
        // Doesn't do anything. Terminal cannot switch states until the current communication ends.
        
    }

    @Override
    public void onCommunicationStart() {
        // Doesn't do anything. Terminal cannot process more than one communication at once.
        
    }

    @Override
    public void onCommunicationEnd() {
        setLastState(this);
        switch (getLastStateAsString()) {
            case "IDLE" -> getTerminal().setState(new IdleState(getTerminal()));
            case "SILENT" -> getTerminal().setState(new SilentState(getTerminal()));
            default -> System.out.println("[DEBUG] Unknown last state in BusyState.java!");
        }
        
    }

    @Override
    public boolean canStartCommunication() {
        return false;
    }

    @Override
    public boolean canEndCurrentCommunication() {
        if (getTerminal().getCurrentCommunication().getSender().equals(getTerminal())) {
            return true;
        } return false;
    }

    @Override
    public String toString() {
        return "BUSY";
    }
    
}
