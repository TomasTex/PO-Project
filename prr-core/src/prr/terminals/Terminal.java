package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import prr.Network;
import prr.clients.Client;
import prr.clients.TerminalObserver;
import prr.comms.Communication;
import prr.comms.TextCommunication;
import prr.comms.VideoCommunication;
import prr.comms.VoiceCommunication;
import prr.exceptions.CommunicationAlreadyPaidException;
import prr.exceptions.CommunicationStillOngoingException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.InvalidFriendKeyException;
import prr.exceptions.InvalidSenderException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.NoSuchFriendException;
import prr.exceptions.NullCommunicationException;
import prr.exceptions.TerminalAlreadyFriendException;
import prr.exceptions.TerminalAlreadyIdleException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnknownTerminalStateException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.UnsupportedCommunicationAtDestinationException;
import prr.exceptions.UnsupportedCommunicationAtOriginException;

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
        private List<TerminalObserver> _observers = new ArrayList<>();
        private List<TerminalObserver> _observersToRemove = new ArrayList<>();
        private Communication _currentCommunication;

        public Terminal(String key, Client client, String state, Network network) throws UnrecognizedEntryException {
                _key = key;
                _client = client;
                _state = getStateFromString(state);
                _currentCommunication = null;
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

        public void setState(TerminalState state) throws IllegalTerminalStateChangeException {
                if (_currentCommunication != null && _currentCommunication.getStatus().equals("ONGOING")) {
                        throw new IllegalTerminalStateChangeException(getState(), new BusyState(this, getState()));
                }
                if (state.getLastState() != null) notifyObservers(state.getLastState(), state);
                _state = state;
        }

        public void turnOnNormally() throws IllegalTerminalStateChangeException {
                getState().onNormalEnable();
        }

        public void turnOnSilently() throws IllegalTerminalStateChangeException {
                getState().onSilentEnable();
        }

        public void turnSilent() throws IllegalTerminalStateChangeException, TerminalAlreadySilentException {
                getState().onSilence();
        }

        public void turnIdle() throws IllegalTerminalStateChangeException, TerminalAlreadyIdleException {
                getState().onIdle();
        }

        public void turnOff() throws IllegalTerminalStateChangeException, TerminalAlreadyOffException {
                getState().onDisable();
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

        public Communication getCurrentCommunication() throws NullCommunicationException {
                if (_currentCommunication != null) {
                        return _currentCommunication;
                }
                throw new NullCommunicationException();
        }

        @Override
        public int compareTo(Terminal other) {
                return _key.compareTo(other.getKey());
        }

        protected TerminalState getStateFromString(String code) throws UnrecognizedEntryException {
                TerminalState state = null;
                TerminalState dummyLastState = new OffState(this, null);
                switch (code) {
                        case "ON" -> state = new IdleState(this, dummyLastState);
                        case "OFF" -> state = new OffState(this, dummyLastState);
                        case "SILENCE" -> state = new SilentState(this , dummyLastState);
                        default -> throw new UnrecognizedEntryException(code);
                }
                return state;
        }

        protected String friendsToString() {
                StringBuilder friendsBuilder = new StringBuilder();
                for(String friendKey : _friends.keySet()) {
                        friendsBuilder.append(friendKey);
                        friendsBuilder.append(",");
                }
                if (!friendsBuilder.isEmpty() && friendsBuilder.lastIndexOf(",") == friendsBuilder.length()-1) friendsBuilder.deleteCharAt(friendsBuilder.length()-1);
                return friendsBuilder.toString();
        }

        public void addFriend(Terminal friend) throws InvalidFriendKeyException, TerminalAlreadyFriendException {
                if (getKey().equals(friend.getKey())) throw new InvalidFriendKeyException(friend.getKey());
                if (isFriend(friend)) throw new TerminalAlreadyFriendException();
                _friends.put(friend.getKey(), friend);
        }

        public void removeFriend(Terminal friend) throws InvalidFriendKeyException, NoSuchFriendException {
                if (getKey().equals(friend.getKey())) throw new InvalidFriendKeyException(friend.getKey());
                if (!isFriend(friend)) throw new NoSuchFriendException();
                _friends.remove(friend.getKey(), friend);
        }

        public boolean isFriend(Terminal target) {
                for (Terminal friend : getFriends().values()) {
                        if (friend.equals(target)) return true;
                }       
                return false;
        }

        public void addObserver(TerminalObserver observer) {
                if(!isDuplicateObserver(observer)) _observers.add(observer);
        }

        public boolean isDuplicateObserver(TerminalObserver observer) {
                for (TerminalObserver obs : _observers) if (obs.equals(observer)) return true;
                return false;
        }

        public void notifyObservers(TerminalState oldState, TerminalState newState) {
                for (TerminalObserver observer : _observers)
                        try {
                                observer.update(this, oldState, newState);
                        } catch (UnknownTerminalStateException e) {
                                // For now we'll catch the exception here. Might want to push it up to the app though.
                                e.printStackTrace();
                        }
                removeObserversAsScheduled();
        }

        public void scheduleObserverRemoval(TerminalObserver observer) {
                _observersToRemove.add(observer);
        }

        public void removeObserversAsScheduled() {
                for (TerminalObserver observer : _observersToRemove) _observers.remove(observer);
                _observersToRemove.clear();
        }

        public abstract boolean canDoTextCommunication();
        public abstract boolean canDoVoiceCommunication();
        public abstract boolean canDoVideoCommunication();

        /**
         * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive communication) and
         *          it was the originator of this communication.
         * @throws NullCommunicationException if there is no current communication.
         **/
        public boolean canEndCurrentCommunication() {
                if (_currentCommunication == null) return false;

                boolean value;
                try {
                        value = _currentCommunication.getSender().equals(this) && getState().canEndCurrentCommunication();
                } catch (NullCommunicationException e) {
                        // I don't know what to do with this.
                        value = false;
                        e.printStackTrace();
                }
                return value;
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
                return getState().canStartCommunication();
        }

        public void sendTextCommunication(Network network, String receiverKey, String message) throws InvalidTerminalKeyException, UnknownTerminalKeyException, DuplicateTerminalKeyException, IllegalTerminalStateChangeException {
                // Validating arguments and terminal state
                network.verifyTerminalKey(receiverKey);
                if (receiverKey.equals(getKey())) { throw new DuplicateTerminalKeyException(receiverKey); }
                Terminal receiver = network.fetchTerminalByKey(receiverKey);

                // Instantiating new text communication object and ending it
                Communication textComm = new TextCommunication(network, this, receiver, message);
                network.insertCommunication(textComm);
                textComm.end();
                _debts += textComm.getPrice();
        }

        public void startInteractiveCommunication(Network network, String receiverKey, String type) throws InvalidTerminalKeyException, UnknownTerminalKeyException, DuplicateTerminalKeyException, IllegalTerminalStateChangeException {
                // Validating arguments and terminal state
                network.verifyTerminalKey(receiverKey);
                if (receiverKey.equals(getKey())) { throw new DuplicateTerminalKeyException(receiverKey); }
                Terminal receiver = network.fetchTerminalByKey(receiverKey);

                getState().onCommunicationStart();
                receiver.getState().onCommunicationStart();

                // Instantiating new interactive communication object
                Communication intComm = null;
                switch (type) {
                        case "VOICE" -> intComm = new VoiceCommunication(network, this, receiver, -1);
                        case "VIDEO" -> intComm = new VideoCommunication(network, this, receiver, -1);
                        default -> System.out.println("[DEBUG] Unknown interactive communication type detected in Terminal.java!");
                }
                _currentCommunication = intComm;
                network.insertCommunication(intComm);
        }
 
        public long endCurrentCommunication(long duration) throws IllegalTerminalStateChangeException {
                //Ending current communication
                _currentCommunication.setDuration(duration);
                _currentCommunication.end();

                // Validating terminal state 
                getState().onCommunicationEnd();
                _currentCommunication.getReceiver().getState().onCommunicationEnd();

                long price = _currentCommunication.getPrice();
                String type = _currentCommunication.toString().split("\\|")[0];
                _debts += price;
                _currentCommunication = null; 
                getClient().registerCommunicationEnd(type);
                return price;               
        }

        /*
        public void attemptConnection(Terminal receiver, String commType) throws UnsupportedCommunicationAtOriginException, UnsupportedCommunicationAtDestinationException, DestinationIsSilentException, DestinationIsBusyException, DestinationIsOffException {
                switch (commType) {
                        // Kind of unnecessary, but it's nice to have just for the sake of maybe having future terminal types that can't do text communications.
                        case "TEXT" -> {
                                if (!this.canDoTextCommunication()) { throw new UnsupportedCommunicationAtOriginException(); }
                                if (!receiver.canDoTextCommunication()) { throw new UnsupportedCommunicationAtDestinationException();}
                        }
                        case "VOICE" -> {
                                if (!this.canDoVoiceCommunication()) { throw new UnsupportedCommunicationAtOriginException(); }
                                if (!receiver.canDoVoiceCommunication()) { throw new UnsupportedCommunicationAtDestinationException();}
                        }
                        case "VIDEO" -> {
                                if (!this.canDoVideoCommunication()) { throw new UnsupportedCommunicationAtOriginException(); }
                                if (!receiver.canDoVideoCommunication()) { throw new UnsupportedCommunicationAtDestinationException();}
                        }
                        default -> System.out.println("[DEBUG] Unknown communication type detected in Terminal.java!");
                }
                if (!receiver.canStartCommunication()) {
                        switch (receiver.getState().toString()) {
                                case "OFF" -> {
                                        getClient().registerFailedCommunication(receiver);
                                        throw new DestinationIsOffException();
                                }
                                case "BUSY" -> {
                                        getClient().registerFailedCommunication(receiver);
                                        throw new DestinationIsBusyException();
                                }
                                case "SILENT" -> {
                                        getClient().registerFailedCommunication(receiver);
                                        throw new DestinationIsSilentException();
                                }
                                default -> System.out.println("[DEBUG] Unknown terminal state detected in Terminal.java!");
                        }
                }
        }
        */

        public void attemptTextCommunication(Terminal receiver) throws UnsupportedCommunicationAtOriginException, UnsupportedCommunicationAtDestinationException, DestinationIsOffException {
                // Will never happen during tests. However, it might prove useful in case the program is
                // ever expanded to fit communication types which cannot do text communications.
                if (!this.canDoTextCommunication()) { throw new UnsupportedCommunicationAtOriginException(); }
                if (!receiver.canDoTextCommunication()) { throw new UnsupportedCommunicationAtDestinationException();}

                if(receiver.getState().toString().equals("OFF")) {
                        getClient().registerFailedCommunication(receiver);
                        throw new DestinationIsOffException();
                }
        }

        public void attemptInteractiveCommunication(Terminal receiver, String commType) throws UnsupportedCommunicationAtOriginException, UnsupportedCommunicationAtDestinationException, DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException {
                switch (commType) {
                        case "VOICE" -> {
                                if (!this.canDoVoiceCommunication()) { throw new UnsupportedCommunicationAtOriginException(); }
                                if (!receiver.canDoVoiceCommunication()) { throw new UnsupportedCommunicationAtDestinationException();}
                        }
                        case "VIDEO" -> {
                                if (!this.canDoVideoCommunication()) { throw new UnsupportedCommunicationAtOriginException(); }
                                if (!receiver.canDoVideoCommunication()) { throw new UnsupportedCommunicationAtDestinationException();}
                        }
                        default -> System.out.println("[DEBUG] Unknown comm type detected in attemptInteractiveCommunication() in Terminal.java!");
                }
                switch (receiver.getState().toString()) {
                        case "OFF" -> {
                                getClient().registerFailedCommunication(receiver);
                                throw new DestinationIsOffException();
                        }
                        case "BUSY" -> {
                                getClient().registerFailedCommunication(receiver);
                                throw new DestinationIsBusyException();
                        }
                        case "SILENCE" -> {
                                getClient().registerFailedCommunication(receiver);
                                throw new DestinationIsSilentException();
                        }
                        case "IDLE" -> {
                                // This is what is supposed to happen.
                                break;
                        }
                        default -> System.out.println("[DEBUG] Unknown terminal state (" + receiver.getState().toString() + ") detected in attemptInteractiveCommunication() in Terminal.java!");
                }
        }

        public void payCommunication(Communication comm) throws CommunicationAlreadyPaidException, CommunicationStillOngoingException, InvalidSenderException {
                if (!comm.getSender().equals(this)) throw new InvalidSenderException();
                if (comm.getStatus().equals("ONGOING")) throw new CommunicationStillOngoingException();
                if (comm.isPaid()) throw new CommunicationAlreadyPaidException();
                long price = comm.getPrice();
                _payments += price;
                _debts -= price;
                comm.pay();
        }
        
        @Override
        public boolean equals(Object o) {
                if (o instanceof Terminal) {
                        Terminal other = (Terminal) o;
                        return _key.equals(other.getKey());
                }       return false;
        }
}
