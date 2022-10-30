package prr.clients;

public class PlatinumClientType extends ClientType {

    public PlatinumClientType(Client client) {
        super(client);
    }

    @Override
    public void update() {
        if(getClient().getBalance() < 0) {
            getClient().setClientType(new NormalClientType(getClient()));
        //FIXME replace placeholder condition "true" with the actual condition which governs the change from Platinum state to Gold state
        } else if (true) {
            getClient().setClientType(new GoldClientType(getClient()));
        }
    }

    @Override
    public String toString() {
        return "PLATINUM";
    }
    
}
