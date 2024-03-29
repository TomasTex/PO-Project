package prr.app.terminal;

import prr.Network;
import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Turn off the terminal.
 */
class DoTurnOffTerminal extends TerminalCommand {

	DoTurnOffTerminal(Network context, Terminal terminal) {
		super(Label.POWER_OFF, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
        try {
			_receiver.turnOff();
		} catch (IllegalTerminalStateChangeException e) {
			// Fail silently.
		} catch (TerminalAlreadyOffException e) {
			_display.popup(Message.alreadyOff());
		}
	}
}
