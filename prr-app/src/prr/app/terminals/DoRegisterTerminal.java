package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
		addStringField("terminalKey", Prompt.terminalKey());
		addOptionField("terminalType", Prompt.terminalType(), "BASIC", "FANCY");
		addStringField("clientKey", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {

		String _terminalKey = stringField("terminalKey");
		String _terminalType = optionField("terminalType");
		String _clientKey = stringField("clientKey");

		try {
			_receiver.verifyTerminalKey(_terminalKey);
		} catch (prr.exceptions.InvalidTerminalKeyException e1) {
			throw new prr.app.exceptions.InvalidTerminalKeyException(_terminalKey);
		}

		String[] fields = {_terminalType, _terminalKey, _clientKey, "ON" };

		try {
			_receiver.registerTerminal(fields);
		} catch (UnrecognizedEntryException e) {
			// I'm not entirely sure how to treat this one.
			e.printStackTrace();
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new prr.app.exceptions.UnknownClientKeyException(_clientKey);
		} catch (prr.exceptions.DuplicateTerminalKeyException e) {
			throw new prr.app.exceptions.DuplicateTerminalKeyException(_terminalKey);
		} catch (prr.exceptions.InvalidTerminalKeyException e) {
			throw new prr.app.exceptions.InvalidTerminalKeyException(_terminalKey);
		}
	}
}