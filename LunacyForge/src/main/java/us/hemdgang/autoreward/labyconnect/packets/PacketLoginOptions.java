package us.hemdgang.autoreward.labyconnect.packets;

import java.util.TimeZone;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketLoginOptions extends Packet {

    private boolean showServer;
    // private UserStatus status;
    private TimeZone timeZone;

    public PacketLoginOptions(final boolean showServer, final TimeZone timeZone) {
	this.showServer = showServer;
	this.timeZone = timeZone;
    }

    public PacketLoginOptions() {
    }

    @Override
    public void read(final PacketBuf buf) {
	this.showServer = buf.readBoolean();
	buf.readUserStatus();
	this.timeZone = TimeZone.getTimeZone(buf.readString());
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeBoolean(this.showServer);
	buf.writeUserStatus(0); // 0 for online
	buf.writeString(this.timeZone.getID());
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }

    public Options getOptions() {
	return new Options(this.showServer, this.timeZone);
    }

    public static class Options {
	private final boolean showServer;
	// private final UserStatus onlineStatus;
	private final TimeZone timeZone;

	public Options(final boolean showServer, final TimeZone timeZone) {
	    this.showServer = showServer;
	    this.timeZone = timeZone;
	    // this.onlineStatus = onlineStatus;
	}

	public boolean isShowServer() {
	    return this.showServer;
	}

	// public UserStatus getOnlineStatus() {
	// return this.onlineStatus;
	// }

	public TimeZone getTimeZone() {
	    return this.timeZone;
	}
    }
}
