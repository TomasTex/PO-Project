package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
        addStringField("clientKey", Prompt.key());
		addStringField("clientName", Prompt.name());
		addStringField("clientTaxID", Prompt.taxId());
	}

	@Override
	protected final void execute() throws CommandException {

		String[] fields = {"CLIENT", stringField("clientKey"), stringField("clientName"), stringField("clientTaxID")};
		try {
			_receiver.registerClient(fields);
		} catch (prr.exceptions.DuplicateClientKeyException e) {
			throw new prr.app.exceptions.DuplicateClientKeyException(stringField("clientKey"));
		}
	}

}
