package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show unused terminals (without communications).
 */
class DoShowUnusedTerminals extends Command<Network> {

	DoShowUnusedTerminals(Network receiver) {
		super(Label.SHOW_UNUSED_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		// Há provavelmente uma forma melhor de se implementar isto, mas estou com
		// medo de andar a import objetos da core para ao app portanto fica assim
		for (Object terminal : _receiver.getUnusedTerminals()) _display.popup(terminal);
	}
}
