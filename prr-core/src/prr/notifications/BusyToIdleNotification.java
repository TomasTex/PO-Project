package prr.notifications;

import prr.clients.Client;
import prr.terminals.Terminal;

public class BusyToIdleNotification extends Notification {

    public BusyToIdleNotification(Client client, Terminal terminal) {
        super(client, terminal);
    }

    @Override
    public String toString() {
        return "B2I|" + _terminal.getKey();
    }
    
}
