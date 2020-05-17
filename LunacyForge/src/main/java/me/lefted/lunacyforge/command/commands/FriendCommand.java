package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.friends.FriendManager;

public class FriendCommand extends Command {

    // CONSTRUCTOR
    public FriendCommand() {
	super("friend");
    }

    // METHODS
    @Override
    public void execute(String[] strings) {
	if (strings.length > 2) {
	    if (strings[1].equalsIgnoreCase("add")) {
		FriendManager.instance.addFriend(strings[2]);
	    } else if (strings[1].equalsIgnoreCase("remove") || strings[1].equalsIgnoreCase("delete")) {
		FriendManager.instance.removeFriend(strings[2]);
	    }
	} else if (strings.length == 2) {
	    if (strings[1].equalsIgnoreCase("list")) {
		FriendManager.instance.listFriends();
	    }
	}
    }

}
