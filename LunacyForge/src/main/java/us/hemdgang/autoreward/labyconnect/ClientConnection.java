package us.hemdgang.autoreward.labyconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Proxy;
import java.nio.channels.UnresolvedAddressException;
import java.security.PublicKey;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.crypto.SecretKey;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import us.hemdgang.autoreward.TestMain;
import us.hemdgang.autoreward.labyconnect.packets.PacketAddonMessage;
import us.hemdgang.autoreward.labyconnect.packets.PacketDisconnect;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptingDecoder;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptingEncoder;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptionRequest;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptionResponse;
import us.hemdgang.autoreward.labyconnect.packets.PacketHelloPing;
import us.hemdgang.autoreward.labyconnect.packets.PacketHelloPong;
import us.hemdgang.autoreward.labyconnect.packets.PacketKick;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginData;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginOptions;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginVersion;
import us.hemdgang.autoreward.labyconnect.packets.PacketServerMessage;

public class ClientConnection extends PacketHandler {

    private NioEventLoopGroup nioEventLoopGroup;
    private ExecutorService executorService;
    private NioSocketChannel nioSocketChannel;
    private Bootstrap bootstrap;

    private Consumer<String> pinResponseConsumer;

    public ClientConnection() {
	this.nioEventLoopGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("Chat"));
	this.executorService = Executors.newFixedThreadPool(2, new DefaultThreadFactory("Helper"));
    }

    public void connect() {
	String defaultIp = "mod.labymod.net";
	int defaultPort = 30336;
	final String customPort = System.getProperty("customChatPort");
	if (customPort != null) {
	    defaultPort = Integer.parseInt(customPort);
	}
	// if (this.customIp != null) {
	// defaultIp = this.customIp;
	// }
	// if (this.customPort != -1) {
	// defaultPort = this.customPort;
	// }
	this.connect(defaultIp, defaultPort);
    }

    public void connect(final String ip, final int port) {
	if (this.nioSocketChannel != null && this.nioSocketChannel.isOpen()) {
	    this.nioSocketChannel.close();
	    this.nioSocketChannel = null;
	}
	// this.labyConnect.setForcedLogout(false);
	// this.labyConnect.getChatlogManager().loadChatlogs(LabyMod.getInstance().getPlayerUUID());
	// this.updateConnectionState(EnumConnectionState.HELLO);
	(this.bootstrap = new Bootstrap()).group((EventLoopGroup) this.nioEventLoopGroup);
	this.bootstrap.option(ChannelOption.TCP_NODELAY, true);
	this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
	this.bootstrap.channel((Class) NioSocketChannel.class);
	this.bootstrap.handler((ChannelHandler) new ClientChannelInitializer(this));
	this.executorService.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    ClientConnection.this.bootstrap.connect(ip, port).syncUninterruptibly();
		    ClientConnection.this.sendPacket(new PacketHelloPing(System.currentTimeMillis()));
		} catch (UnresolvedAddressException error) {
		    // ClientConnection.this.updateConnectionState(EnumConnectionState.OFFLINE);
		    String lastKickmessage = ((error.getMessage() == null) ? "Unknown error" : error.getMessage());
		    System.out.println(lastKickmessage);
		    // Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "UnresolvedAddressException: " + error.getMessage());
		    error.printStackTrace();
		} catch (Throwable throwable) {
		    // ClientConnection.this.updateConnectionState(EnumConnectionState.OFFLINE);
		    String lastKickMessage = ((throwable.getMessage() == null) ? "Unknown error" : throwable.getMessage());
		    System.out.println(lastKickMessage);
		    // Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Throwable: " + throwable.getMessage());
		    throwable.printStackTrace();
		    // if (ClientConnection.this.lastKickMessage.contains("no further information") || throwable.getMessage() == null) {
		    // ClientConnection.this.lastKickMessage = LanguageManager.translate("chat_not_reachable");
		    // }
		}
	    }
	});
    }

    public void setNioSocketChannel(NioSocketChannel nioSocketChannel) {
	this.nioSocketChannel = nioSocketChannel;
    }

    public void sendPacket(final Packet packet) {
	this.sendPacket(packet, null);
    }

    public void sendPacket(final Packet packet, final Consumer<NioSocketChannel> consumer) {
	if (this.nioSocketChannel == null) {
	    return;
	}
	// if (!this.nioSocketChannel.isOpen() || !this.nioSocketChannel.isWritable() || this.currentConnectionState == EnumConnectionState.OFFLINE) {
	if (!this.nioSocketChannel.isOpen() || !this.nioSocketChannel.isWritable()) {
	    return;
	}
	// Protocol.getProtocol().getPacketId(packet);

	System.out.println(String.format("Sending packet %s", packet.getClass().getSimpleName()));

	if (this.nioSocketChannel.eventLoop().inEventLoop()) {
	    this.nioSocketChannel.writeAndFlush((Object) packet).addListeners(new GenericFutureListener[] {
		    (GenericFutureListener) ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE });
	    if (consumer != null) {
		consumer.accept(this.nioSocketChannel);
	    }
	} else {
	    this.nioSocketChannel.eventLoop().execute((Runnable) new Runnable() {
		@Override
		public void run() {
		    ClientConnection.this.nioSocketChannel.writeAndFlush((Object) packet).addListeners(new GenericFutureListener[] {
			    (GenericFutureListener) ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE });
		    if (consumer != null) {
			consumer.accept(ClientConnection.this.nioSocketChannel);
		    }
		}
	    });
	}
    }

    @Override
    public void handle(PacketHelloPing p0) {
	System.out.println("recieved packet hello ping");
    }

    @Override
    public void handle(PacketHelloPong p0) {
	System.out.println("recieved packet hello pong");
	System.out.println("sending login packet (data, options, version");

	UUID uuid = Minecraft.getMinecraft().getSession().getProfile().getId();
	String playerName = Minecraft.getMinecraft().getSession().getUsername();
//	UUID uuid = TestMain.uuid;
//	String playerName = TestMain.email;
	String motd = TestMain.motd;

	this.sendPacket(new PacketLoginData(uuid, playerName, motd));
	this.sendPacket(new PacketLoginOptions(false, TimeZone.getDefault()));
	final String aboutMcVersion = "1.8.9";
	this.sendPacket(new PacketLoginVersion(24, aboutMcVersion + "_" + "3.8.25"));

    }

    @Override
    public void handle(PacketNotAllowed p0) {
	p0.handle();
    }

    @Override
    public void handle(PacketServerMessage p0) {
	System.out.println(String.format("recieved packet server message %s", p0.getMessage()));
    }

    @Override
    public void handle(final PacketDisconnect packet) {
	System.out.println("recieved packet disconnect");
	this.disconnect(true);

	if (packet.getReason() != null) {
	    System.out.println(String.format("disconnect message: %s", packet.getReason()));
	}
    }

    public void disconnect(final boolean kicked) {
	// if (this.currentConnectionState == EnumConnectionState.OFFLINE) {
	// return;
	// }
	// this.updateConnectionState(EnumConnectionState.OFFLINE);
	// LabyMod.getInstance().getUserManager().getFamiliarManager().clear();
	this.executorService.execute(new Runnable() {
	    @Override
	    public void run() {
		// ClientConnection.this.labyConnect.getChatlogManager().saveChatlogs(LabyMod.getInstance().getPlayerUUID());
		if (ClientConnection.this.nioSocketChannel != null && !kicked) {
		    ClientConnection.this.nioSocketChannel.writeAndFlush((Object) new PacketDisconnect("Logout")).addListener(
			(GenericFutureListener) new ChannelFutureListener() {

			    public void operationComplete(final ChannelFuture arg0) throws Exception {
				if (ClientConnection.this.nioSocketChannel != null) {
				    ClientConnection.this.nioSocketChannel.close();
				}
			    }
			});
		}
	    }
	});
    }

    public void requestPin(final Consumer<String> consumer) {
	this.pinResponseConsumer = consumer;
	System.out.println("Sending packet addon message with auth_pin");
	this.sendPacket(new PacketAddonMessage("auth_pin", new JsonObject().toString()));
    }

    @Override
    public void handle(PacketAddonMessage packet) {
	System.out.println("recieved packet addon message");

	// LabyMod.getInstance().getEventManager().callAddonMessage(packet);
	final String key = packet.getKey();
	if (key.equals("UPDATE")) {
	    // LabyMod.getInstance().getUpdater().setForceUpdate(true);
	}
	if (key.equals("UPDATE-BACKUP")) {
	    // LabyMod.getInstance().getUpdater().setBackupMethod(true);
	    // LabyMod.getInstance().getUpdater().setForceUpdate(true);
	}
	if (key.equals("auth_pin")) {
	    System.out.println("type auth_pin");

	    final JsonObject jsonObject = (JsonObject) new JsonParser().parse(packet.getJson());
	    if (jsonObject.has("pin")) {
		final String pin = jsonObject.get("pin").getAsString();
		if (this.pinResponseConsumer != null) {
		    this.pinResponseConsumer.accept(pin);
		}
	    }
	}
	// if (key.equals("server_message")) {
	// final JsonObject jsonObject = (JsonObject)new JsonParser().parse(packet.getJson());
	// LabyMod.getInstance().displayMessageInChat(ModColor.createColors(jsonObject.get("message").getAsString()));
	// }
    }

    @Override
    public void handle(PacketLoginData packetLoginData) {
	System.out.println("recieved packet login data");
    }

    @Override
    public void handle(PacketLoginOptions packetLoginOptions) {
	System.out.println("recieved packet login options");
    }

    @Override
    public void handle(PacketLoginVersion packetLoginVersion) {
	System.out.println("recieved packet login version");
    }

    @Override
    public void handle(final PacketEncryptionRequest encryptionRequest) {
	System.out.println("recieved packet encryption request");
	final SecretKey secretKey = CryptManager.createNewSharedKey();
	final PublicKey publicKey = CryptManager.decodePublicKey(encryptionRequest.getPublicKey());
	System.out.println(String.format("server id %s", encryptionRequest.getServerId()));
	final String serverId = encryptionRequest.getServerId();
	final String token = "";
	final String hash = new BigInteger(CryptManager.getServerIdHash(serverId, publicKey, secretKey)).toString(16);
	System.out.println(String.format("hash %s", hash));
	// Minecraft.getMinecraft().getSession().getGameProfile().getId()
	// final UUID uuid = ave.A().L().e().getId();
	// TODO implement minecraft authentication
	// if (uuid == null) {
	// // this.lastKickMessage = LanguageManager.translate("chat_invalid_session");
	// // Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, this.lastKickMessage);
	// System.out.println("Invalid session");
	// this.disconnect(false);
	// return;
	// }
	try {
	    // TODO create session service and server
	    // TODO get game profile

	    // authenticate
//	    final YggdrasilRequester requester = new YggdrasilRequester();
//	    final String email = TestMain.email;
//	    final String password = TestMain.password;
//	    // final YggdrasilAuthenticateRes response = requester.authenticate(YggdrasilAgent.getMinecraftAgent(), username, password);
//	    // final String accessToken = response.getAccessToken();
//	    YggdrasilUserAuthentication mojangAuthentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
//		.createUserAuthentication(Agent.MINECRAFT);
//	    mojangAuthentication.setUsername(email);
//	    mojangAuthentication.setPassword(password);
//	    mojangAuthentication.logIn();

//	    final String accessToken = mojangAuthentication.getAuthenticatedToken();

//	    System.out.println(String.format("got back accessToken:%s", accessToken));
	    final MinecraftSessionService minecraftSessionService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString())
		.createMinecraftSessionService();

	    // final GameProfile profile = new GameProfile(TestMain.uuid, "");
//	    final GameProfile profile = mojangAuthentication.getSelectedProfile();
	    
	    final GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
	    
	    if (profile.getId() == null) {
		System.out.println("UUID null");
		return;
	    }
	    
	    
	    final String accessToken = Minecraft.getMinecraft().getSession().getToken();
	    
	    minecraftSessionService.joinServer(profile, accessToken, hash);
	    GameProfile hasJoinedServer = minecraftSessionService.hasJoinedServer(profile, hash);
	    
//	    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//	    System.out.println("Waiting");
//	    try {
//		reader.readLine();
//	    } catch (IOException e1) {
//		e1.printStackTrace();
//	    }

	    if (hasJoinedServer == null) {
		System.out.println("has joined server = null");
	    } else {
		System.out.println(String.format("id %s name %s", hasJoinedServer.getId(), hasJoinedServer.getName()));
	    }
	    try {
		Thread.sleep(2000L);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    // minecraftSessionService.joinServer(ave.A().L().e(), ave.A().L().d(), hash);
	    this.sendPacket(new PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.getVerifyToken()), new Consumer<NioSocketChannel>() {
		@Override
		public void accept(final NioSocketChannel channel) {
		    ClientConnection.this.enableEncryption(secretKey);
		}
	    });
	    return;
	} catch (AuthenticationUnavailableException e1) {
	    // this.lastKickMessage = LanguageManager.translate("chat_authentication_unavaileable");
	} catch (InvalidCredentialsException e2) {
	    // this.lastKickMessage = LanguageManager.translate("chat_invalid_session");
	} catch (AuthenticationException e3) {
	    // this.lastKickMessage = LanguageManager.translate("chat_login_failed");
	    System.out.println("Authentication Exception");
	    e3.printStackTrace();
	}

	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, this.lastKickMessage);
	// System.out.println("Failed to enable encryption");
	// this.disconnect(false);
    }

    public void enableEncryption(final SecretKey key) {
	this.nioSocketChannel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler) new PacketEncryptingDecoder(CryptManager.createNetCipherInstance(2,
	    key)));
	this.nioSocketChannel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler) new PacketEncryptingEncoder(CryptManager.createNetCipherInstance(1,
	    key)));
	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Enabled LabyConnect encryption!");
	System.out.println("Enabled LabyConnect encryption");
    }

    @Override
    public void handle(PacketEncryptionResponse packetEncryptionResponse) {
	System.out.println("recieved packet encryption response packet");
    }

    @Override
    public void handle(PacketKick packet) {
	this.disconnect(true);
	String kickMessage = "unknown";
	if (packet.getReason() != null)
	    kickMessage = packet.getReason();

	System.out.println(String.format("recieved kick packet!! reason: %s", kickMessage));
    }
}