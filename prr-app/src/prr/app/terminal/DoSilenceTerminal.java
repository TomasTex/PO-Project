package prr.app.terminal;

import prr.Network;
import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.TerminalAlreadySilentException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Silence the terminal.
 */
class DoSilenceTerminal extends TerminalCommand {

	DoSilenceTerminal(Network context, Terminal terminal) {
		super(Label.MUTE_TERMINAL, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
        try {
			_receiver.turnOnSilently();
		} catch (prr.exceptions.IllegalTerminalStateChangeException e) {
			try {
				_receiver.turnSilent();
			} catch (IllegalTerminalStateChangeException e1) {
				// Fail silently.
			} catch (TerminalAlreadySilentException e1) {
				_display.popup(Message.alreadySilent());
			}
		}
	}
}
