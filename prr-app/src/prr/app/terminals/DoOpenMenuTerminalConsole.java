package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add mode import if needed

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

	private String _terminalKey;

	DoOpenMenuTerminalConsole(Network receiver) {
		super(Label.OPEN_MENU_TERMINAL, receiver);
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_terminalKey = Form.requestString(Prompt.terminalKey());
			(new prr.app.terminal.Menu(_receiver, _receiver.fetchTerminalByKey(_terminalKey))).open();
		} catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(_terminalKey);
		}
				//// I THINK I DID THIS BUT IM NOT SURE
                //FIXME implement command
                // create an instance of prr.app.terminal.Menu with the
                // selected Terminal
				//// PLS CONFIRM
	}
}
