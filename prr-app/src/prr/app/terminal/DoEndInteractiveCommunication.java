package prr.app.terminal;

import prr.Network;
import prr.exceptions.IllegalTerminalStateChangeException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

	DoEndInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.END_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canEndCurrentCommunication());
		addStringField("duration", Prompt.duration());
	}

	@Override
	protected final void execute() throws CommandException {
        long _duration = Long.parseLong(stringField("duration"));
		long _price = 0;

		try {
			_price = _receiver.endCurrentCommunication(_duration);
			_display.popup(Message.communicationCost(_price));
		} catch (IllegalTerminalStateChangeException e) {
			System.out.println("[DEBUG] Illegal state change from " + e.getOldState() + " to " + e.getNewState() + "!");
			e.printStackTrace();
		}
	}
}
