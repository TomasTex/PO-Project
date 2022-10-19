package prr.terminals;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import prr.clients.Client;
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

        public Terminal(String key, Client client, String state) throws UnrecognizedEntryException {
                _key = key;
                _client = client;
                _state = getStateFromString(state);
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

        // FIXME define attributes
        // FIXME define contructor(s)
        // FIXME define methods

        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication.
         **/
        public boolean canEndCurrentCommunication() {
                if (this.getState().getTerminalState() == "BusyState") {
                        return true;
                } return false;
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
                String state = this.getState().getTerminalState();
                if (!state.equals("OffState")&& !state.equals("BusyState")) {
                        return true;
                } return false;
        }       
}
