package prr.app.terminal;

import prr.Network;
import prr.exceptions.InvalidFriendKeyException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.NoSuchFriendException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		addStringField("friendKey", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
        String _friendKey = stringField("friendKey");
		Terminal _friend = null;
		try {
			_network.verifyTerminalKey(_friendKey);
			_friend = _network.fetchTerminalByKey(_friendKey);
			_receiver.removeFriend(_friend);
			
		} catch (prr.exceptions.InvalidTerminalKeyException e) {
			throw new prr.app.exceptions.InvalidTerminalKeyException(_friendKey);
		} catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new prr.app.exceptions.UnknownTerminalKeyException(_friendKey);
		} catch (prr.exceptions.InvalidFriendKeyException e) {
			// Fail silently.
		} catch (NoSuchFriendException e) {
			// Fail silently.
		}
	}
}
