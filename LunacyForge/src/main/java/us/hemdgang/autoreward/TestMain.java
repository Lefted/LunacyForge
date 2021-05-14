package us.hemdgang.autoreward;

import java.util.UUID;

import me.lefted.lunacyforge.utils.Logger;
import us.hemdgang.autoreward.labyconnect.ClientConnection;

public class TestMain {

    // public static String UUID = "89ebbce855f54f5989f2f16d9923c2d9";
    public static UUID uuid;
    public static String password;
    public static String email;
    public static String motd;
    public static String username;

    
    public static boolean readIncomingPackets = false;
    public static boolean readOutgoingPackets = false;
    public static boolean logInChat = false;
    public static boolean ignore = true;

    public static void entry(String[] args) throws InterruptedException {
	uuid = UUID.fromString(System.getenv("UUID"));
	password = System.getenv("PASSWORD");
	email = System.getenv("EMAIL");
	motd = System.getenv("MOTD");
	username = System.getenv("USERNAME");
	
	final String info = String.format("email %s username %s uuid %s motd %s", email, username, uuid, motd);
	System.out.println(info);
	Logger.logChatMessage(info);

	
	 final ClientConnection connection = new ClientConnection();
	
	 connection.connect();
	
	
//	 Thread.sleep(5000L);
//	 connection.requestPin(System.out::println);
	
	 while (true)
	 ;
    }
}
