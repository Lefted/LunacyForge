package me.lefted.lunacyforge.friends;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lefted.lunacyforge.LunacyForge;
import me.lefted.lunacyforge.utils.Logger;

public class FriendManager {

    // INSTANCE
    public static FriendManager instance;

    // ATTRIBUTES
    private List<String> friendList;
    private File saveFile;

    // CONSTRUCTOR
    public FriendManager() {
	friendList = new ArrayList<String>();
	saveFile = new File(LunacyForge.getPath(), "friend.csv");
	if (!saveFile.exists()) {
	    Logger.logConsole("Creating friends.csv");
	    try {
		saveFile.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	loadFromFile();
    }

    // METHODS
    private void loadFromFile() {

	try {
	    @SuppressWarnings("resource")
	    final BufferedReader reader = new BufferedReader(new FileReader(saveFile));
	    String fileString = null;

	    try {
		if (reader.ready()) {
		    fileString = reader.readLine();
		}

		if (fileString != null) {
		    if (!fileString.isEmpty()) {
			final String[] names = fileString.split(",");

			if (names.length > 0) {

			    for (String name : names) {
				addFriend(name);
			    }
			}
		    }
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

    private void saveFile() {
	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < friendList.size(); i++) {
	    final String currentName = friendList.get(i);

	    if (i == friendList.size() - 1) {
		builder.append(currentName);
	    } else {
		builder.append(currentName + ",");
	    }
	}

	try {
	    final FileWriter writer = new FileWriter(saveFile);
	    writer.write(builder.toString());
	    writer.flush();
	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public void addFriend(String name) {
	if (!friendList.contains(name)) {
	    friendList.add(name);
	    Logger.logChatMessage("§7Friend §c" + name + " §7has been added");
	    saveFile();
	} else {
	    Logger.logChatMessage("§cFriendlist already contains " + name);
	}
    }

    public void removeFriend(String name) {
	if (friendList.contains(name)) {
	    friendList.remove(name);
	    Logger.logChatMessage("§7Friend §c" + name + " §7has been removed");
	    saveFile();
	} else {
	    Logger.logChatMessage("§cFriendlist does not contain " + name);
	}
    }

    public void listFriends() {
	if (!friendList.isEmpty()) {
	    Logger.logChatMessage("---------");
	    friendList.forEach(friend -> Logger.logChatMessage("§7" + friend));
	    Logger.logChatMessage("---------");
	} else {
	    Logger.logChatMessage("§7No friends");
	}
    }

    public boolean isPlayerFriend(String name) {
	return friendList.contains(name);
    }
}
