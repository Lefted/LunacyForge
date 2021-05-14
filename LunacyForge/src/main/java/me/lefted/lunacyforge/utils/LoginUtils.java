package me.lefted.lunacyforge.utils;

import java.net.Proxy;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.api.TheAltening;
import com.thealtening.api.response.Account;
import com.thealtening.api.retriever.BasicDataRetriever;
import com.thealtening.auth.TheAlteningAuthentication;

import me.lefted.lunacyforge.implementations.ISession;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginUtils {

    // INSTANCE
    public static final LoginUtils INSTACE = new LoginUtils();

    // ATTRIBUTES
    private BasicDataRetriever basicDataRetriever = TheAltening.newBasicRetriever("");

    // METHODS
    public CompletableFuture<Account> generateAccountFromTheAltening() {
	return CompletableFuture.supplyAsync(basicDataRetriever::getAccount);
    }

    /**
     * Changes the authentication service type to TheAltening and tries to login
     * @param theAlteningToken
     * @return true if login was successful
     */
    public boolean login(String theAlteningToken) {
	TheAlteningAuthentication.theAltening().theAltening();

	final YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""),
	    Agent.MINECRAFT);

	yggdrasilUserAuthentication.setUsername(theAlteningToken);
	yggdrasilUserAuthentication.setPassword("Lunacyforge");
	try {
	    yggdrasilUserAuthentication.logIn();
	    ISession session = (ISession) Minecraft.getMinecraft();
	    session.setSession(new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId()
		.toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
	    return true;
	} catch (AuthenticationException e) {
	    e.printStackTrace();
	    return false;
	}
    }
    
    public void updateTheAlteningApiKey(String apiKey) {
	basicDataRetriever.updateKey(apiKey);
    }
}
