package prr.clients;

import java.io.Serializable;

public abstract class ClientType implements Serializable {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    private Client _client;

    public ClientType(Client client) {
        _client = client;
    }

    public abstract void update();

    protected Client getClient() {
        return _client;
    }
    
}
