package me.lefted.lunacyforge.command.commands;

import me.lefted.lunacyforge.command.Command;
import us.hemdgang.autoreward.TestMain;
import us.hemdgang.autoreward.labyconnect.ClientConnection;

public class AutoRewardCommand extends Command{
    
    public AutoRewardCommand() {
	super("autoreward");
    }

    @Override
    public void execute(String[] strings) {
	new Thread(() -> {
	    try {
		TestMain.entry(null);
	    } catch (InterruptedException e1) {
		e1.printStackTrace();
	    }
	}).start();;
    }

}
