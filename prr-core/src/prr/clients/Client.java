package prr.clients;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import prr.exceptions.InvalidClientAttributeModificationException;
import prr.exceptions.UnknownTerminalStateException;
import prr.notifications.BusyToIdleNotification;
import prr.notifications.DefaultDeliveryMethod;
import prr.notifications.Notification;
import prr.notifications.NotificationDeliveryMethod;
import prr.notifications.OffToIdleNotification;
import prr.notifications.OffToSilentNotification;
import prr.notifications.SilentToIdleNotification;
import prr.plans.BasePlan;
import prr.plans.Plan;
import prr.terminals.Terminal;
import prr.terminals.TerminalState;

public class Client implements TerminalObserver, Serializable, Comparable<Client> {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    private String _key;
    private String _name;
    private int _taxID;
    private ClientType _clientType;
    private List<Notification> _notifications = new ArrayList<>();
    private Map<String, Terminal> _terminals = new TreeMap<>();
    private long _payments = 0;
    private long _debts = 0;
    private Plan _plan;
    private NotificationDeliveryMethod _notificationDeliveryMethod;
    private boolean _acceptsNotifications;
    private int _consecutiveVideoCommsWithoutNegativeBalance;
    private int _consecutiveTextCommsWithoutNegativeBalance;

    private static transient Collator _collator = Collator.getInstance(Locale.getDefault());

    public Client(String key, String name, int taxID) {
        _key = key;
        _name = name;
        _taxID = taxID;
        _clientType = new NormalClientType(this);
        _payments = 0;
        _debts = 0;
        _plan = new BasePlan();
        _notificationDeliveryMethod = new DefaultDeliveryMethod();
        _acceptsNotifications = true;
        _consecutiveVideoCommsWithoutNegativeBalance = 0;
        _consecutiveTextCommsWithoutNegativeBalance = 0;
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public int getTaxID() {
        return _taxID;
    }

    public ClientType getClientType() {
        return _clientType;
    }

    public List<Notification> getNotificationList() {
        return _notifications;
    }

    public void updatePaymentsAndDebts() {
        long payments = 0;
        long debts = 0;
        for (Terminal terminal : _terminals.values()) {
            payments += terminal.getPayments();
            debts += terminal.getDebts();
        }
        _payments = payments;
        _debts = debts;
    }

    public long getPayments() {
        return _payments;
    }

    public long getDebts() {
        return _debts;
    }

    public long getBalance() {
        return _payments - _debts;
    }

    public Plan getPlan() {
        return _plan;
    }

    public NotificationDeliveryMethod getDeliveryMethod() {
        return _notificationDeliveryMethod;
    }

    public void setDeliveryMethod(NotificationDeliveryMethod method) {
        _notificationDeliveryMethod = method;
    }

    public int getConsecutiveVideoCommsWithoutNegativeBalance() {
        return _consecutiveVideoCommsWithoutNegativeBalance;
    }

    public int getConsecutiveTextCommsWithoutNegativeBalance() {
        return _consecutiveTextCommsWithoutNegativeBalance;
    }

    public void resetConsecutiveCommsWithoutNegativeBalance() {
        _consecutiveVideoCommsWithoutNegativeBalance = 0;
        _consecutiveTextCommsWithoutNegativeBalance = 0;
    }

    public void addTerminal(Terminal terminal) {
        _terminals.put(terminal.getKey(), terminal);
    }

    public boolean acceptsNotifications() {
        return _acceptsNotifications;
    }

    public int getTerminalMapSize() {
        return _terminals.size();
    }

    public String acceptsNotificationsToString() {
        if (_acceptsNotifications) {
            return "YES";
        }   return "NO";
    }

    public void clearNotifications() {
        _notifications.clear();
    }

    public void setClientType(ClientType clientType) {
        _clientType = clientType;
    }

    public void updateClientType() {
        _clientType.update();
    }

    public void enableClientNotifications() throws InvalidClientAttributeModificationException {
        if (!_acceptsNotifications) { _acceptsNotifications = true; }
        else { throw new InvalidClientAttributeModificationException(); }
    }

    public void disableClientNotifications() throws InvalidClientAttributeModificationException {
        if (_acceptsNotifications) { _acceptsNotifications = false; }
        else { throw new InvalidClientAttributeModificationException(); }
    }

    public void registerFailedCommunication(Terminal receiver) {
        if (_acceptsNotifications) receiver.addObserver(this);
    }

    public void registerCommunicationEnd(String commType) {
        updatePaymentsAndDebts();
        updateClientType();
        if (getBalance() >= 0) {
            switch (commType) {
                case "TEXT" -> _consecutiveTextCommsWithoutNegativeBalance++;
                case "VIDEO" -> _consecutiveVideoCommsWithoutNegativeBalance++;
            }
        } else { resetConsecutiveCommsWithoutNegativeBalance(); }
    }

    @Override
    public int compareTo(Client other) {
        //return _key.compareTo(other.getKey());
        return _collator.compare(_key, other.getKey());
    }

    @Override
    public String toString() {
        return "CLIENT|" + this.getKey() + "|" + this.getName() + "|" + this.getTaxID() + "|" + 
            this.getClientType().toString() + "|" + this.acceptsNotificationsToString() + "|" + 
                    this.getTerminalMapSize() + "|" + this.getPayments() + "|" + getDebts(); 
    }

    @Override
    public void update(Terminal terminal, TerminalState oldState, TerminalState newState) throws UnknownTerminalStateException {
        Notification notification = null;
        //System.out.println("is this even triggered? " + oldState + " -> " + newState);
        switch (oldState.toString()) {
            case "OFF" -> {
                switch (newState.toString()) {
                    case "SILENCE" -> {
                        notification = new OffToSilentNotification(this, terminal);
                        if (_acceptsNotifications) _notifications.add(notification);
                        terminal.scheduleObserverRemoval(this);
                    }
                    case "IDLE" -> {
                        notification = new OffToIdleNotification(this, terminal);
                        if (_acceptsNotifications) _notifications.add(notification);
                        terminal.scheduleObserverRemoval(this);
                    }
                    //There is no other option here. If this switch block somehow ends in the default option, something is wrong
                    default -> {
                        terminal.scheduleObserverRemoval(this);
                        throw new UnknownTerminalStateException(newState.toString());
                    }
                }
            }
            case "BUSY" -> {
                if(newState.toString().equals("IDLE")) { 
                    notification = new BusyToIdleNotification(this, terminal); 
                    if (_acceptsNotifications) _notifications.add(notification);
                    terminal.scheduleObserverRemoval(this); 
                }
            }
            case "SILENCE" -> {
                if(newState.toString().equals("IDLE")) { 
                    notification = new SilentToIdleNotification(this, terminal);
                    if (_acceptsNotifications) _notifications.add(notification);
                    terminal.scheduleObserverRemoval(this); 
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Client) {
            Client other = (Client) o;
            return getKey().equals(other.getKey());
        }   return false;
    }
}
