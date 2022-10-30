package prr.plans;

import prr.comms.Communication;

public class BasePlan extends Plan {

    @Override
    public long calculateTextCommPrice(Communication comm) {
        long units = comm.getUnits();
        long price = 0;
        String clientType = comm.getSender().getClient().getClientType().toString();
        if (units < 50) {
            switch (clientType) {
                case "NORMAL" -> price = 10;
                case "GOLD" -> price = 10;
                case "PLATINUM" -> price = 0;
                // FIXME swap this out for an exception later
                default -> System.out.println("[DEBUG] Unknown client type received in calculateTextCommPrice!");
            }
        } else if (units < 100) {
            switch (clientType) {
                case "NORMAL" -> price = 16;
                case "GOLD" -> price = 10;
                case "PLATINUM" -> price = 4;
                // FIXME swap this out for an exception later
                default -> System.out.println("[DEBUG] Unknown client type received in calculateTextCommPrice!");
            }
        } else if (units >= 100) {
            switch (clientType) {
                case "NORMAL" -> price = 2 * units;
                case "GOLD" -> price = 2 * units;
                case "PLATINUM" -> price = 4;
                // FIXME swap this out for an exception later
                default -> System.out.println("[DEBUG] Unknown client type received in calculateTextCommPrice!");
            }
        } else { 
            // FIXME swap this out for an exception later
            System.out.println("[DEBUG] Communication units value breaks all logic"); 
        }
        return price;
    }

    @Override
    public long calculateVoiceCommPrice(Communication comm) {
        long price = 0;
        switch (comm.getSender().getClient().getClientType().toString()) {
            case "NORMAL" -> price = 20;
            case "GOLD" -> price = 10;
            case "PLATINUM" -> price = 10;
            // FIXME swap this out for an exception later
            default -> System.out.println("[DEBUG] Unknown client type received in calculateVoiceCommPrice!");
        }
        return price;
    }

    @Override
    public long calculateVideoCommPrice(Communication comm) {
        long price = 0;
        switch (comm.getSender().getClient().getClientType().toString()) {
            case "NORMAL" -> price = 30;
            case "GOLD" -> price = 20;
            case "PLATINUM" -> price = 10;
            // FIXME swap this out for an exception later
            default -> System.out.println("[DEBUG] Unknown client type received in calculateVideoCommPrice!");
        }
        return price;
    }

    @Override
    public String toString() {
        return "BASE";
    }
    
}
