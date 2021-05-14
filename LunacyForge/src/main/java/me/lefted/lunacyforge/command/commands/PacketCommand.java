package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.utils.Logger;
import us.hemdgang.autoreward.TestMain;

public class PacketCommand extends Command {

    public PacketCommand() {
	super("packet");
    }

    @Override
    public void execute(String[] strings) {
	if (strings.length < 2)
	    return;

	final String mode = strings[1];

	switch (mode) {
	case "in":
	    TestMain.readIncomingPackets = !TestMain.readIncomingPackets;
	    Logger.logChatMessage(String.format("Set packet sniffing for incoming packets to %s", TestMain.readIncomingPackets));
	    break;
	case "out":
	    TestMain.readOutgoingPackets = !TestMain.readOutgoingPackets;
	    Logger.logChatMessage(String.format("Set packet sniffing for outging packets to %s", TestMain.readOutgoingPackets));
	    break;
	case "chat":
	    TestMain.logInChat = !TestMain.logInChat;
	    Logger.logChatMessage(String.format("Set chat logging to %s", TestMain.logInChat));
	    break;
	case "ignore":
	    TestMain.ignore = !TestMain.ignore;
	    Logger.logChatMessage(String.format("Set ignore to %s", TestMain.ignore));
	    break;
	default:
	    Logger.logChatMessage("usage .packet <in/out/chat>");
	    break;
	}
    }

}
