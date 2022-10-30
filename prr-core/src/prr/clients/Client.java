package prr.clients;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import prr.notifications.DefaultDeliveryMethod;
import prr.notifications.Notification;
import prr.notifications.NotificationDeliveryMethod;
import prr.plans.BasePlan;
import prr.plans.Plan;
import prr.terminals.Terminal;

public class Client implements Serializable, Comparable<Client> {

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
    private List<String> _failedCommReceiverIDs = new ArrayList<>();

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
        updatePaymentsAndDebts();
        return _payments;
    }

    public long getDebts() {
        updatePaymentsAndDebts();
        return _debts;
    }

    public long getBalance() {
        updatePaymentsAndDebts();
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

    public void update() {
        _clientType.update();
    }

    public void addFailedConnectionAttempt(Terminal receiver) {
        _failedCommReceiverIDs.add(receiver.getKey());
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
