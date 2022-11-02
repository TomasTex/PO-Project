package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("friendKey", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {

		String _friendKey = stringField("friendKey");
		Terminal _friend = null;
		try {
			_network.verifyTerminalKey(_friendKey);
			_friend = _network.fetchTerminalByKey(_friendKey);
			_receiver.addFriend(_friend);
		} catch (prr.exceptions.InvalidTerminalKeyException e) {
			throw new prr.app.exceptions.InvalidTerminalKeyException(_friendKey);
		} catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new prr.app.exceptions.UnknownTerminalKeyException(_friendKey);
		} catch (prr.exceptions.InvalidFriendKeyException e) {
			// Fail silently.
		} catch (prr.exceptions.TerminalAlreadyFriendException e) {
			// Fail silently.
		}

	}
}
