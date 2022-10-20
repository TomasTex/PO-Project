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

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

	private Map<String, Client> _clients = new TreeMap<>();
	private Map<String, Terminal> _terminals = new TreeMap<>();
	
	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename name of the text input file
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws IOException if there is an IO error while processing the text file
	 * @throws DuplicateClientKeyException
	 */
	void importFile(String filename) throws UnrecognizedEntryException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\|");
				try {
					registerFields(fields);
				} catch (DuplicateClientKeyException | DuplicateTerminalKeyException | UnrecognizedEntryException | UnknownClientKeyException | UnknownTerminalKeyException | InvalidFriendKeyException | InvalidTerminalKeyException e) {
					// This should not happen.
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			throw new UnrecognizedEntryException(filename);
		}
    }

	public Client fetchClientByKey(String key) throws UnknownClientKeyException {
		Client client = _clients.get(key);
		if (client == null) {
			throw new UnknownClientKeyException(key);
		}
		return client;
	}

	public Terminal fetchTerminalByKey(String key) throws UnknownTerminalKeyException {
		Terminal terminal = _terminals.get(key);
		if (terminal == null) {
			throw new UnknownTerminalKeyException(key);
		}
		return terminal;
	}

	public List<Terminal> getAllTerminals() {
		List<Terminal> terminalList = new ArrayList<Terminal>();
		terminalList.addAll(_terminals.values());
		Collections.sort(terminalList); // Is this necessary?
		return terminalList;
	}

	public List<Client> getAllClients() {
		List<Client> clientList = new ArrayList<Client>();
		clientList.addAll(_clients.values());
		Collections.sort(clientList);
		return clientList;
	}

	public List<Terminal> getUnusedTerminals() {
		List<Terminal> terminalList = new ArrayList<Terminal>();
		for (Terminal terminal : _terminals.values()) {
			if(terminal.isUnused()) { terminalList.add(terminal); }
		}
		Collections.sort(terminalList);
		return terminalList;
	}

	public void registerFields(String[] fields) throws UnrecognizedEntryException, DuplicateClientKeyException, UnknownClientKeyException, DuplicateTerminalKeyException, UnknownTerminalKeyException, InvalidFriendKeyException, InvalidTerminalKeyException {
		switch (fields[0]) {
			case "CLIENT" -> registerClient(fields);
			case "FANCY", "BASIC" -> registerTerminal(fields);
			case "FRIENDS" -> registerFriends(fields);
			default -> throw new UnrecognizedEntryException(fields[0]);
		}
	}

	public void registerClient(String[] fields) throws DuplicateClientKeyException {
		if(_clients.containsKey(fields[1])) {
			throw new DuplicateClientKeyException(fields[1]);
		}
		Client client = new Client(fields[1], fields[2], Integer.valueOf(fields[3]));
		_clients.put(client.getKey(), client);
	}

	public void registerTerminal(String[] fields) throws UnrecognizedEntryException, UnknownClientKeyException, DuplicateTerminalKeyException, InvalidTerminalKeyException {
		verifyTerminalKey(fields[1]);
		if(_terminals.containsKey(fields[1])) {
			throw new DuplicateTerminalKeyException(fields[1]);
		}
		Terminal terminal = null;
		Client holder = fetchClientByKey(fields[2]);
		switch(fields[0]) {
			case "BASIC" -> terminal = new BasicTerminal(fields[1], holder, fields[3]);
			case "FANCY" -> terminal = new FancyTerminal(fields[1], holder, fields[3]);
			default -> throw new UnrecognizedEntryException(fields[0]);
		}
		_terminals.put(terminal.getKey(), terminal);
		holder.addTerminal(terminal);
	}

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
	/*
	public boolean isValidTerminalKey(String key) {
		if (key.length() != 6) { return false; }
		try {
			Integer.parseInt(key);
		} catch (NumberFormatException e) { return false; }
		return true;
	}
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
	
}

