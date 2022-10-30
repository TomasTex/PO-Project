package prr.comms;

import prr.Network;
import prr.terminals.Terminal;

public class TextCommunication extends Communication {

    private String _message;

    public TextCommunication(Network network, Terminal sender, Terminal receiver, String message) {
        super(network, sender, receiver);
        _message = message;
    }

    public long getMessageLength() {
        return _message.length();
    }

    @Override
    public void setUnits() {
        _units = _message.length();
    }

    @Override
    public void calculatePrice() {
       _price = getSender().getClient().getPlan().calculateTextCommPrice(this); 
    }
    
}
