package prr.comms;

import prr.Network;
import prr.terminals.Terminal;

public class VideoCommunication extends Communication {

    private long _duration;

    public VideoCommunication(Network network, Terminal sender, Terminal receiver, long duration) {
        super(network, sender, receiver);
        _duration = duration;
    }

    public long getDuration() {
        return _duration;
    }

    @Override
    public void setDuration(long duration) {
        _duration = duration;
    }

    @Override
    public void setUnits() {
        _units = _duration;
    }

    @Override
    public void calculatePrice() {
        _price = getSender().getClient().getPlan().calculateVideoCommPrice(this);
    }

    @Override
    public String toString() {
        return "VIDEO|" + getID() + "|" + getSender().getKey() + "|" + getReceiver().getKey() + "|" + getUnits() + "|" + getPrice() + "|" + getStatus();
    }
    
}
