package prr.notifications;

import prr.clients.Client;
import prr.terminals.Terminal;

public class SilentToIdleNotification extends Notification {

    public SilentToIdleNotification(Client client, Terminal terminal) {
        super(client, terminal);
    }

    @Override
    public String toString() {
        return "S2I|" + _terminal.getKey();
    }
    
}
