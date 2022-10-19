package prr.clients;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import prr.notifications.Notification;
import prr.plans.Plan;
import prr.terminals.Terminal;

public class Client implements Serializable, Comparable<Client> {

    private String _key;
    private String _name;
    private int _taxID;
    private ClientType _clientType;
    private List<Notification> _notifications = new ArrayList<>();
    private Map<String, Terminal> _terminals = new TreeMap<>();
    private long _payments = 0;
    private long _debts = 0;
    private Plan _plan;
    private String _notificationReceptionMethod;
    private boolean _acceptsNotifications;

    public Client(String key, String name, int taxID) {
        _key = key;
        _name = name;
        _taxID = taxID;
        _clientType = new NormalClientType();
        _payments = 0;
        _debts = 0;
        _plan = null;
        _notificationReceptionMethod = "POMBO";
        _acceptsNotifications = true;

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

    public long getPayments() {
        return _payments;
    }

    public long getDebts() {
        return _debts;
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

    @Override
    public int compareTo(Client other) {
        return _key.compareTo(other.getKey());
    }

    @Override
    public String toString() {
        return "CLIENT|" + this.getKey() + "|" + this.getName() + "|" + this.getTaxID() + "|" + 
            this.getClientType().toString() + "|" + this.acceptsNotificationsToString() + "|" + 
                    this.getTerminalMapSize() + "|" + this.getPayments() + "|" + getDebts(); 
    }
}
