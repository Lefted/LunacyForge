package me.lefted.lunacyforge.modules;

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

    // ATTRIBUTES
    private static List<String> friendList;
    private File saveFile;

    public FriendManager() {
	this.friendList = new ArrayList<String>();
	this.saveFile = new File(LunacyForge.getPath(), "friends.csv");
	if (!this.saveFile.exists()) {
	    Logger.logConsole("Creating friends.csv");
	    try {
		this.saveFile.createNewFile();
	    } catch (Exception e) {
		Logger.logErrConsole("Error creating friends.csv:");
		e.printStackTrace();
	    }
	}

	this.loadFriendsFromFile();
    }

    public void addFriend(String name) {
	if (!friendList.contains(name)) {
	    friendList.add(name);
	    Logger.logChatMessage("§7Friend §c" + name + " §7has been added");
	    this.saveFile();
	} else {
	    Logger.logChatMessage("§cFriendlist already contains " + name);
	}
    }

    public void removeFriend(String name) {
	if (friendList.contains(name)) {
	    friendList.remove(name);
	    Logger.logChatMessage("§7Friend §c" + name + " §7has been removed");
	    this.saveFile();
	} else {
	    Logger.logChatMessage("§cFriendlist does not contain " + name);
	}
    }

    // ACCESS
    public static boolean isPlayerFriendly(String name) {
	return FriendManager.friendList.contains(name);
    }

    public void loadFriendsFromFile() {
	try {
	    @SuppressWarnings("resource")
	    final BufferedReader reader = new BufferedReader(new FileReader(this.saveFile));
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
				this.addFriend(name);
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
	    final FileWriter writer = new FileWriter(this.saveFile);
	    writer.write(builder.toString());
	    writer.flush();
	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
