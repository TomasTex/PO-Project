package prr.clients;

public class PlatinumClientType extends ClientType {

    public PlatinumClientType(Client client) {
        super(client);
    }

    @Override
    public void update() {
        if(getClient().getBalance() < 0) {
            getClient().setClientType(new NormalClientType(getClient()));
        } else if (getClient().getConsecutiveTextCommsWithoutNegativeBalance() == 2) {
            getClient().resetConsecutiveCommsWithoutNegativeBalance();
            getClient().setClientType(new GoldClientType(getClient()));
        }
    }

    @Override
    public String toString() {
        return "PLATINUM";
    }
    
}
