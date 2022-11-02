package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.IllegalTerminalStateChangeException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.UnsupportedCommunicationAtDestinationException;
import prr.exceptions.UnsupportedCommunicationAtOriginException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

        DoSendTextCommunication(Network context, Terminal terminal) {
                super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
                addStringField("destinationKey", Prompt.terminalKey());
                addStringField("message", Prompt.textMessage());
        }

        @Override
        protected final void execute() throws CommandException {
                String _destinationKey = stringField("destinationKey");
                String _message = stringField("message");
                Terminal _destination = null;
                try {
                        _destination = _network.fetchTerminalByKey(_destinationKey);
                        _receiver.attemptTextCommunication(_destination);
                        _receiver.sendTextCommunication(_network, _destinationKey, _message);
                } catch (UnsupportedCommunicationAtOriginException e) {
                        // Does not happen.
                } catch (UnsupportedCommunicationAtDestinationException e) {
                        // Does not happen.
                } catch (DestinationIsOffException e) {
                        _display.popup(Message.destinationIsOff(_destinationKey));
                } catch (prr.exceptions.InvalidTerminalKeyException e) {
                        throw new prr.app.exceptions.InvalidTerminalKeyException(_destinationKey);
                } catch (prr.exceptions.UnknownTerminalKeyException e) {
                        throw new prr.app.exceptions.UnknownTerminalKeyException(_destinationKey);
                } catch (prr.exceptions.DuplicateTerminalKeyException e) {
                        // Fails silently, I think?
                } catch (IllegalTerminalStateChangeException e) {
                        // Fails silently.
                }
        }
} 
