package prr.clients;

public class GoldClientType extends ClientType {
    
    public GoldClientType(Client client) {
        super(client);
    }

    @Override
    public void update() {
        if (getClient().getBalance() < 0) {
            getClient().setClientType(new NormalClientType(getClient()));
        //FIXME replace placeholder condition "true" with the actual condition which governs the change from Gold state to Platinum state
        } else if (true) {
            getClient().setClientType(new PlatinumClientType(getClient()));
        }
        
    }

    @Override
    public String toString() {
        return "GOLD";
    }

}
