package prr.terminals;

import prr.clients.Client;
import prr.exceptions.UnrecognizedEntryException;

public class FancyTerminal extends Terminal {
    
    public FancyTerminal(String key, Client client, String state) throws UnrecognizedEntryException {
        super(key, client, state);
    }

    @Override
    public String toString() {
        String base = "FANCY|" + this.getKey() + "|" + this.getClient().getKey() + "|" + this.getState().toString() + "|" + this.getPayments() + "|" + this.getDebts();
        if (this.getFriends().size() == 0) {
            return base;
        }
        return base + "|" + this.friendsToString();
    }
}
