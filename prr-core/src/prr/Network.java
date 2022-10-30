package prr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import prr.clients.Client;
import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.InvalidFriendKeyException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;
import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.terminals.Terminal;

/**
 * Class Network implements a network.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

	/** A map containing all known client keys as keys and all corresponding client objects as values. */
	private Map<String, Client> _clients = new TreeMap<>();
	/** A map containing all known terminal keys as keys and all corresponding terminal objects as values. */
	private Map<String, Terminal> _terminals = new TreeMap<>();
	/** An integer detailing the unique identifier of the next initializable communication object. */
	private int nextCommID = 1;
	
	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename name of the text input file
	 * @throws UnrecognizedEntryException if some entry is not correct.
	 * @throws IOException if there is an IO error while processing the text file.
	 * @throws DuplicateClientKeyException if any new client keys are found to already exist within the network.
	 */
	void importFile(String filename) throws UnrecognizedEntryException, IOException, DuplicateClientKeyException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\|");
				try {
					registerFields(fields);
				} catch (DuplicateTerminalKeyException | UnrecognizedEntryException | UnknownClientKeyException | UnknownTerminalKeyException | InvalidFriendKeyException | InvalidTerminalKeyException e) {
					// This should not happen.
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			throw new UnrecognizedEntryException(filename);
		}
    }

	/**
	 * Receives a client key string and attempts to retrieve the corresponding client object
	 * by searching the current network's map of clients.
	 * 
	 * @param key unique key of the client we want to retrieve.
	 * @return the client whose key was passed to the function.
	 * @throws UnknownClientKeyException if the key does not match with any known client.
	 */

	public Client fetchClientByKey(String key) throws UnknownClientKeyException {
		Client client = _clients.get(key);
		if (client == null) {
			throw new UnknownClientKeyException(key);
		}
		return client;
	}

	/**
	 * Receives a terminal key string and attempts to retrieve the corresponding terminal object
	 * by searching the current network's map of terminals.
	 *  
	 * @param key unique key of the terminal we want to retrieve.
	 * @return the terminal whose key was passed to the function.
	 * @throws UnknownTerminalKeyException if the key does not match with any known terminal.
	 */
	public Terminal fetchTerminalByKey(String key) throws UnknownTerminalKeyException {
		Terminal terminal = _terminals.get(key);
		if (terminal == null) {
			throw new UnknownTerminalKeyException(key);
		}
		return terminal;
	}

	/**
	 * Returns a list containing all known terminals.
	 * 
	 * @return a list of all known terminals, sorted by their key value.
	 */
	public List<Terminal> getAllTerminals() {
		List<Terminal> terminalList = new ArrayList<Terminal>();
		terminalList.addAll(_terminals.values());
		Collections.sort(terminalList); // Is this necessary?
		return terminalList;
	}

	/**
	 * Returns a list containing all known clients.
	 * 
	 * @return a list of all known clients, sorted by their key value.
	 */
	public List<Client> getAllClients() {
		List<Client> clientList = new ArrayList<Client>();
		clientList.addAll(_clients.values());
		Collections.sort(clientList); //Is this necessary?
		return clientList;
	}

	/**
	 * Returns a list containing all known unused terminals.
	 * 
	 * @return a list of all known unused terminals, sorted by their key value.
	 */
	public List<Terminal> getUnusedTerminals() {
		List<Terminal> terminalList = new ArrayList<Terminal>();
		for (Terminal terminal : _terminals.values()) {
			if(terminal.isUnused()) { terminalList.add(terminal); }
		}
		Collections.sort(terminalList);
		return terminalList;
	}

	/**
	 * Sorts the domain entity creation process into the right method based on the first
	 * string field read. 
	 * 
	 * @param fields a string array containing all fields needed to create a new domain entity.
	 * @throws UnrecognizedEntryException if an unrecognized field is read.
	 * @throws DuplicateClientKeyException if a new and supposedly unique client key is found to already exist within the network.
	 * @throws UnknownClientKeyException if a requested client key is found to not match any known clients within the network.
	 * @throws DuplicateTerminalKeyException if a new and supposedly unique terminal key is found to already exist within the network.
	 * @throws UnknownTerminalKeyException if a requested terminal key is found to not match any known terminals within the network.
	 * @throws InvalidFriendKeyException if an attempt to register a terminal as a friend of itself is made.
	 * @throws InvalidTerminalKeyException if a given terminal key is either not made up solely of numbers or is not 6 characters long.
	 */
	public void registerFields(String[] fields) throws UnrecognizedEntryException, DuplicateClientKeyException, UnknownClientKeyException, DuplicateTerminalKeyException, UnknownTerminalKeyException, InvalidFriendKeyException, InvalidTerminalKeyException {
		switch (fields[0]) {
			case "CLIENT" -> registerClient(fields);
			case "FANCY", "BASIC" -> registerTerminal(fields);
			case "FRIENDS" -> registerFriends(fields);
			default -> throw new UnrecognizedEntryException(fields[0]);
		}
	}

	/**
	 * Creates a new client object from a set of valid string fields.
	 * 
	 * @param fields a string array containing all fields needed to create a new client object.
	 * @throws DuplicateClientKeyException if the new client's key is found to already exist within the network.
	 */
	public void registerClient(String[] fields) throws DuplicateClientKeyException {
		if(_clients.containsKey(fields[1])) {
			throw new DuplicateClientKeyException(fields[1]);
		}
		Client client = new Client(fields[1], fields[2], Integer.valueOf(fields[3]));
		_clients.put(client.getKey(), client);
	}

	/**
	 * Creates a new terminal object from a set of valid string fields.
	 * 
	 * @param fields a string array containing all fields needed to create a new terminal object.
	 * @throws UnrecognizedEntryException if an unrecognized field is read.
	 * @throws UnknownClientKeyException if a requested client key is found to not match any known clients within the network.
	 * @throws DuplicateTerminalKeyException if the new terminal's key is found to already exist within the network.
	 * @throws InvalidTerminalKeyException if the new terminal's key is either not made up solely of numbers or is not 6 characters long.
	 */
	public void registerTerminal(String[] fields) throws UnrecognizedEntryException, UnknownClientKeyException, DuplicateTerminalKeyException, InvalidTerminalKeyException {
		verifyTerminalKey(fields[1]);
		if(_terminals.containsKey(fields[1])) {
			throw new DuplicateTerminalKeyException(fields[1]);
		}
		Terminal terminal = null;
		Client holder = fetchClientByKey(fields[2]);
		switch(fields[0]) {
			case "BASIC" -> terminal = new BasicTerminal(fields[1], holder, fields[3], this);
			case "FANCY" -> terminal = new FancyTerminal(fields[1], holder, fields[3], this);
			default -> throw new UnrecognizedEntryException(fields[0]);
		}
		_terminals.put(terminal.getKey(), terminal);
		holder.addTerminal(terminal);
	}

	/**
	 * Registers a set of terminals as friends of a "main" terminal.
	 * 
	 * @param fields a string array containing the key of the "main" terminal and the keys of all friend terminals.
	 * @throws UnknownTerminalKeyException if a requested terminal key is found to not match any known terminals within the network.
	 * @throws InvalidFriendKeyException if an attempt to register a terminal as a friend of itself is made.
	 */
	public void registerFriends(String[] fields) throws UnknownTerminalKeyException, InvalidFriendKeyException {
		Terminal main = fetchTerminalByKey(fields[1]);
		String[] friendKeys = fields[2].split(",");
		for (String key : friendKeys) {
			if (key.equals(main.getKey())) { 
				throw new InvalidFriendKeyException(key); 
			}
			main.addFriend(fetchTerminalByKey(key));
		}
		
	}

	/**
	 * Checks whether a given string (key) is in the right format to be passed as
	 * a terminal key. A terminal key must be entirely made up of numbers and
	 * must be 6 characters long.
	 * 
	 * @param key the unverified terminal key.
	 * @throws InvalidTerminalKeyException if the key string is not in the right format to be passed as a terminal key.
	 */
	public void verifyTerminalKey(String key) throws InvalidTerminalKeyException {
		if (key.length() != 6) {
			throw new InvalidTerminalKeyException(key);
		}
		try {
			Integer.parseInt(key);
		} catch (NumberFormatException e) {
			throw new InvalidTerminalKeyException(key);
		}
	}

	/**
	 * Returns the appropriate unique identifier for a communication object.
	 * 
	 * @return the appropriate unique idenfifier.
	 */
	public int getCommID() {
		return nextCommID++;
	}

	/**
	 * Returns the total balance of every recognized entity in the network.
	 * (Note: Since the Client class's getBalance() iterates over all of its
	 * object's assigned terminals, iterating over all known clients in this
	 * method is enough to guarantee that all entities in the network are
	 * being taken into consideration.)
	 * 
	 * @return the total balance of the network, in credits.
	 */
	public long getGlobalBalance() {
		long balance = 0;
		for (Client client : getAllClients()) {
			balance += client.getBalance();
		}
		return balance;
	}
	
}

