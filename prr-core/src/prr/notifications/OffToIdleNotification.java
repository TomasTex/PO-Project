package prr.notifications;

import prr.clients.Client;
import prr.terminals.Terminal;

public class OffToIdleNotification extends Notification {

    public OffToIdleNotification(Client client, Terminal terminal) {
        super(client, terminal);
    }

    @Override
    public String toString() {
        return "O2I|" + _terminal.getKey();
    }
    
}
