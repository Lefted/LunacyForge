package me.lefted.lunacyforge.command.commands;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import me.lefted.lunacyforge.command.Command;
import me.lefted.lunacyforge.implementations.ISession;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginCommand extends Command {

    public LoginCommand() {
	super("login");
    }

    @Override
    public void execute(String[] strings) {
	// if (strings.length < 3)
	// return;
	logIn();
    }

    public static void logIn() {
	final String email = System.getenv("EMAIL");
	final String password = System.getenv("PASSWORD");

	// login
	final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(
	    Agent.MINECRAFT);
	auth.setUsername(email);
	auth.setPassword(password);

	try {
	    auth.logIn();

	    final Session session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(),
		"mojang");
	    ((ISession) Minecraft.getMinecraft()).setSession(session);
	    Logger.logChatMessage("logged in successfully");
	} catch (AuthenticationException e) {
	    Logger.logChatMessage("could not login");
	    e.printStackTrace();
	}
    }

}
