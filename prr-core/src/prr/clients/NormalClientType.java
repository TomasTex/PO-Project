package prr.clients;

public class NormalClientType extends ClientType {
    
    public NormalClientType(Client client) {
        super(client);
    }

    @Override
    public void update() {
        if (getClient().getBalance() > 500) { 
            getClient().setClientType(new GoldClientType(getClient())); 
        }
    }

    @Override
    public String toString() {
        return "NORMAL";
    }

}
