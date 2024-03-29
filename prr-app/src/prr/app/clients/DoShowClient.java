package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		String _clientKey = stringField("clientKey");
		try {
			_display.popup(_receiver.fetchClientByKey(_clientKey));
			for (Object notification : _receiver.fetchClientByKey(_clientKey).getNotificationList()) {
				_display.popup(notification);
			}
			_receiver.fetchClientByKey(_clientKey).clearNotifications();
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new prr.app.exceptions.UnknownClientKeyException(_clientKey);
		}
	}
}
