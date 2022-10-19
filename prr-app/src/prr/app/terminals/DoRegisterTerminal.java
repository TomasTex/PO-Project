package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
		//FIXME add command fields
	}

	@Override
	protected final void execute() throws CommandException {
        String _terminalKey = Form.requestString(Prompt.terminalKey());
		String _terminalType = Form.requestString(Prompt.terminalType());
		while (!_terminalType.equals("BASIC") && !_terminalType.equals("FANCY")) {
			_terminalType = Form.requestString(Prompt.terminalType());
		}
		String _clientKey = Form.requestString(Prompt.clientKey());

		String[] fields = {_terminalType, _terminalKey, _clientKey, "ON"};


		try {
			// TODO figure out when a given _terminalKey is INVALID and throw the appropriate exception
			_receiver.registerTerminal(fields);
		} catch (UnrecognizedEntryException e) {
			// I'm not entirely sure how to treat this one.
			e.printStackTrace();
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new prr.app.exceptions.UnknownClientKeyException(_clientKey);
		} catch (prr.exceptions.DuplicateTerminalKeyException e) {
			throw new prr.app.exceptions.DuplicateTerminalKeyException(_terminalKey);
		}


	}
}