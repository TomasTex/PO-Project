package prr.app.terminal;

import prr.Network;
import prr.comms.Communication;
import prr.exceptions.CommunicationAlreadyPaidException;
import prr.exceptions.CommunicationStillOngoingException;
import prr.exceptions.InvalidSenderException;
import prr.exceptions.UnknownCommunicationKeyException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
// Add more imports if needed

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

	DoPerformPayment(Network context, Terminal terminal) {
		super(Label.PERFORM_PAYMENT, context, terminal);
		addStringField("commKey", Prompt.commKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			int _commKey = Integer.parseInt(stringField("commKey"));
			Communication _comm = _network.fetchCommunicationByKey(_commKey);
			_receiver.payCommunication(_comm);
		} catch (NumberFormatException | UnknownCommunicationKeyException | CommunicationAlreadyPaidException | CommunicationStillOngoingException | InvalidSenderException e) {
			_display.popup(Message.invalidCommunication());
		}
	}
}
