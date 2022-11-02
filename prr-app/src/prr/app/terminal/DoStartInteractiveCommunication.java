package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.UnsupportedCommunicationAtDestinationException;
import prr.exceptions.UnsupportedCommunicationAtOriginException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
		addStringField("destinationKey", Prompt.terminalKey());
		addOptionField("commType", Prompt.commType(), "VOICE", "VIDEO");
	}

	@Override
	protected final void execute() throws CommandException {
		String _destinationKey = stringField("destinationKey");
		String _commType = optionField("commType");
		Terminal _destination = null;
        try {
			_destination = _network.fetchTerminalByKey(_destinationKey);
			_receiver.attemptInteractiveCommunication(_destination, _commType);
			_receiver.startInteractiveCommunication(_network, _destinationKey, _commType);
		} catch (UnsupportedCommunicationAtOriginException e) {
			_display.popup(Message.unsupportedAtOrigin(_destinationKey, _commType));
		} catch (UnsupportedCommunicationAtDestinationException e) {
			_display.popup(Message.unsupportedAtDestination(_destinationKey, _commType));
		} catch (DestinationIsSilentException e) {
			_display.popup(Message.destinationIsSilent(_destinationKey));
		} catch (DestinationIsBusyException e) {
			_display.popup(Message.destinationIsBusy(_destinationKey));
		} catch (DestinationIsOffException e) {
			_display.popup(Message.destinationIsOff(_destinationKey));
		} catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new prr.app.exceptions.UnknownTerminalKeyException(_destinationKey);
		} catch (InvalidTerminalKeyException e) {
			// I'm assuming this won't happen. Fails silently.
		} catch (DuplicateTerminalKeyException e) {
			// I'm assuming this won't happen. Fails silently.
		} catch (IllegalTerminalStateChangeException e) {
			// I'm assuming this won't happen. Fails silently.
		}
	}
}
