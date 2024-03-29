package prr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.ImportFileException;
import prr.exceptions.MissingFileAssociationException;
import prr.exceptions.UnavailableFileException;
import prr.exceptions.UnrecognizedEntryException;

/**
 * Manage access to network and implement load/save operations.
 */
public class NetworkManager implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

	/** The network itself. */
	private Network _network = new Network();
	/** The name of the file containing the serialized network's state. */
	private String _associatedFilename = null;

	/**
	 * Retrieves the associated network.
	 * 
	 * @return the network itself.
	 */
	public Network getNetwork() {
		return _network;
	}

	/**
	 * Reads network data from a file corresponding to the given filename and
	 * replaces the current network with the one stored in the file.
	 * 
	 * @param filename name of the file containing the serialized application's
	 *                 state
	 *                 to load.
	 * @throws UnavailableFileException if the specified file does not exist or
	 *                                  there is
	 *                                  an error while processing this file.
	 */
	public void load(String filename) throws UnavailableFileException {
		_associatedFilename = filename;
		try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			_network = (Network) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new UnavailableFileException(filename);
		}
	}

	/**
	 * Saves the serialized application's state into the file associated to the
	 * current network.
	 *
	 * @throws FileNotFoundException           if for some reason the file cannot be
	 *                                         created or opened.
	 * @throws MissingFileAssociationException if the current network does not have
	 *                                         a file.
	 * @throws IOException                     if there is some error while
	 *                                         serializing the state of the network
	 *                                         to disk.
	 */
	public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
		if (_associatedFilename == null) {
			throw new MissingFileAssociationException();
		} else {
			try (ObjectOutputStream out = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(_associatedFilename)))) {
				out.writeObject(_network);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves the serialized application's state into the specified file. The current
	 * network is
	 * associated to this file.
	 *
	 * @param filename the name of the file.
	 * @throws FileNotFoundException           if for some reason the file cannot be
	 *                                         created or opened.
	 * @throws MissingFileAssociationException if the current network does not have
	 *                                         a file.
	 * @throws IOException                     if there is some error while
	 *                                         serializing the state of the network
	 *                                         to disk.
	 */
	public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
		_associatedFilename = filename;
		save();
	}

	/**
	 * Read text input file and create domain entities.
	 * 
	 * @param filename name of the text input file.
	 * @throws ImportFileException         if for some reason the import process was
	 *                                     done incorrectly or contained
	 *                                     unrecognized fields.
	 * @throws DuplicateClientKeyException if any new client keys are found to
	 *                                     already exist within the network.
	 */
	public void importFile(String filename) throws ImportFileException {
		try {
			_network.importFile(filename);
		} catch (IOException | UnrecognizedEntryException | DuplicateClientKeyException e) {
			throw new ImportFileException(filename, e);
		}
	}

}
