package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
                //FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
        String _clientKey = Form.requestString(Prompt.key());
		String _clientName = Form.requestString(Prompt.name());
		String _clientTaxID = Form.requestString(Prompt.taxId());

		String[] fields = {"CLIENT", _clientKey, _clientName, _clientTaxID};
		try {
			_receiver.registerClient(fields);
		} catch (prr.exceptions.DuplicateClientKeyException e) {
			throw new prr.app.exceptions.DuplicateClientKeyException(_clientKey);
		}
	}

}
