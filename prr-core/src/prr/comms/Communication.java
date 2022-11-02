package prr.comms;

import java.io.Serializable;

import prr.Network;
import prr.terminals.Terminal;

public abstract class Communication implements Serializable, Comparable<Communication> {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    private int _id;
    private Network _network;
    private Terminal _sender;
    private Terminal _receiver;
    private boolean _isOngoing;
    protected long _units;
    protected long _price;
    private boolean _isPaid;

    public Communication(Network network, Terminal sender, Terminal receiver) {
        _network = network;
        _id = _network.getCommID();
        _sender = sender;
        _receiver = receiver;
        _isOngoing = true;
        _units = 0;
        _price = 0;
        _isPaid = false;
    }

    public Terminal getSender() {
        return _sender;
    }

    public Terminal getReceiver() {
        return _receiver;
    }

    public int getID() {
        return _id;
    }

    public long getUnits() {
        return _units;
    }

    public long getPrice() {
        return _price;
    }

    public boolean isPaid() {
        return _isPaid;
    }

    public String getStatus() {
        if (_isOngoing) {
            return "ONGOING";
        }   return "FINISHED";
    }

    public void end() {
        _isOngoing = false;
        // Always call calculatePrice() after setUnits(). The calculatePrice() method uses the getUnits() method in this class,
        // which will always return 0 unless the _units attribute is correctly defined (as per the setUnits() method).
        setUnits();
        calculatePrice();
    }

    public void pay() {
        _isPaid = true;
    }

    public abstract void setUnits();
    public abstract void calculatePrice();
    public void setDuration(long duration) {}

    @Override
    public boolean equals(Object o) {
        if(o instanceof Communication) {
            Communication other = (Communication) o;
            return getID() == other.getID();
        }   return false;
    }

    @Override
    public int compareTo(Communication other) {
        return _id - other.getID();
    }
    
}
