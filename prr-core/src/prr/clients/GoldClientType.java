package prr.clients;

public class GoldClientType extends ClientType {
    
    public GoldClientType(Client client) {
        super(client);
    }

    @Override
    public void update() {
        if (getClient().getBalance() < 0) {
            getClient().setClientType(new NormalClientType(getClient()));
        } else if (getClient().getConsecutiveVideoCommsWithoutNegativeBalance() == 5) {
            getClient().resetConsecutiveCommsWithoutNegativeBalance();
            getClient().setClientType(new PlatinumClientType(getClient()));
        }
        
    }

    @Override
    public String toString() {
        return "GOLD";
    }

}
