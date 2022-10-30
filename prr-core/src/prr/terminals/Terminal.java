package prr.terminals;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import prr.Network;
import prr.clients.Client;
import prr.comms.Communication;
import prr.comms.TextCommunication;
import prr.comms.VideoCommunication;
import prr.comms.VoiceCommunication;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable, Comparable<Terminal> /* FIXME maybe addd more interfaces */{

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

        private String _key;
        private Client _client;
        private Map<String, Terminal> _friends = new TreeMap<>();
        private TerminalState _state;
        private long _payments = 0;
        private long _debts = 0;
        private Map<Integer, Communication> _comms = new TreeMap<>();
        private Communication _currentCommunication;

        /* TEMPORARY SOLUTION TO COMM INIT PROBLEM */
        private Network _network;

        public Terminal(String key, Client client, String state, Network network) throws UnrecognizedEntryException {
                _key = key;
                _client = client;
                _state = getStateFromString(state);
                //Probably unneccessary.
                _state.setLastState(new OffState(this));
                _network = network;
        }

        public String getKey() {
                return _key;
        }

        public Client getClient() {
                return _client;
        }

        public TerminalState getState() {
                return _state;
        }

        public void setState(TerminalState state) {
                _state = state;
        }

        public long getPayments() {
                return _payments;
        }

        public long getDebts() {
                return _debts;
        }

        public boolean isUnused() {
                return _payments == 0 && _debts == 0;
        }

        public Map<String,Terminal> getFriends() {
                return _friends;
        }

        public Communication getCurrentCommunication() {
                return _currentCommunication;
        }

        @Override
        public int compareTo(Terminal other) {
                return _key.compareTo(other.getKey());
        }

        protected TerminalState getStateFromString(String code) throws UnrecognizedEntryException {
                TerminalState state = null;
                switch (code) {
                        case "ON" -> state = new IdleState(this);
                        case "OFF" -> state = new OffState(this);
                        case "SILENCE" -> state = new SilentState(this);
                        default -> throw new UnrecognizedEntryException(code);
                }
                return state;
        }

        protected String friendsToString() {
                String friendsAsString = "";
                for(String friendKey : _friends.keySet()) {
                        if (friendsAsString != "") {
                                friendsAsString = friendsAsString + friendKey;
                        }
                        else {
                                friendsAsString = friendsAsString + "," + friendKey;
                        }
                }
                return friendsAsString;
        }

        public void addFriend(Terminal friend) {
                _friends.put(friend.getKey(), friend);
        }

        public abstract boolean canDoTextCommunication();
        public abstract boolean canDoVoiceCommunication();
        public abstract boolean canDoVideoCommunication();

        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication.
         **/
        public boolean canEndCurrentCommunication() {
                return getState().canEndCurrentCommunication();
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
                return getState().canStartCommunication();
        }

        public void startTextCommunication(String receiverKey, String message) throws InvalidTerminalKeyException, UnknownTerminalKeyException {
                _network.verifyTerminalKey(receiverKey);
                Terminal receiver = _network.fetchTerminalByKey(receiverKey);

                Communication textComm = new TextCommunication(_network, this, receiver, message);
                _currentCommunication = textComm;
                _comms.put(textComm.getID(), textComm);
                // ADD EXCEPTION IF ACCESSED BY BAD TERMINAL STATES
                getState().onCommunicationStart();
        }

        public void startInteractiveCommunication(String receiverKey, long duration, String type) throws InvalidTerminalKeyException, UnknownTerminalKeyException {
                _network.verifyTerminalKey(receiverKey);
                Terminal receiver = _network.fetchTerminalByKey(receiverKey);
                Communication intComm = null;
                switch (type) {
                        case "VOICE" -> intComm = new VoiceCommunication(_network, this, receiver, duration);
                        case "VIDEO" -> intComm = new VideoCommunication(_network, this, receiver, duration);
                        default -> System.out.println("[DEBUG] Unknown interactive communication type detected in Terminal.java!");
                }
                _currentCommunication = intComm;
                _comms.put(intComm.getID(), intComm);
                // ADD EXCEPTION IF ACCESSED BY BAD TERMINAL STATES
                getState().onCommunicationStart();
        }
 
        public void endCurrentCommunication() {
                _currentCommunication.end();
                // _currentCommunication = null;
                // ADD EXCEPTION IF ACCESSED BY BAD TERMINAL STATES
                getState().onCommunicationEnd();

        }

        public void attemptConnection(Terminal receiver) {
                if(!receiver.canStartCommunication()) {
                        getClient().addFailedConnectionAttempt(receiver);
                }
        }
        
        @Override
        public boolean equals(Object o) {
                if (o instanceof Terminal) {
                        Terminal other = (Terminal) o;
                        return _key.equals(other.getKey());
                }
                return false;
        }
}
