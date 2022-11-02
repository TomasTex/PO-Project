package prr.app.clients;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

	DoShowAllClients(Network receiver) {
		super(Label.SHOW_ALL_CLIENTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
        // Há provavelmente uma forma melhor de se implementar isto, mas estou com
		// medo de andar a import objetos da core para ao app portanto fica assim
		for (Object client : _receiver.getAllClients()) _display.popup(client);
	}
}
