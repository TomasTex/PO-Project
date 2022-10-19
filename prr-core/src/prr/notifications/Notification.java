package prr.notifications;

import java.io.Serializable;

import prr.clients.Client;
import prr.terminals.Terminal;

public abstract class Notification implements Serializable {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    protected Client _client;
    protected Terminal _terminal;

    public Notification(Client client, Terminal terminal) {
        _client = client;
        _terminal = terminal;
    }

    public String getNotificationType() {
        return getClass().getName();
    }
    
}