package prr.notifications;

import prr.clients.Client;
import prr.terminals.Terminal;

public class OffToSilentNotification extends Notification {

    public OffToSilentNotification(Client client, Terminal terminal) {
        super(client, terminal);
    }

    @Override
    public String toString() {
        return "O2S|" + _terminal.getKey();
    }
    
}