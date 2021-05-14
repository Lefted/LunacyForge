package us.hemdgang.autoreward.labyconnect;

public class PacketNotAllowed extends Packet{

    private String reason;
    private long until;
    
    public PacketNotAllowed(final String reason, final long until) {
        this.reason = reason;
        this.until = until;
    }
    
    public PacketNotAllowed() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.reason = buf.readString();
        this.until = buf.readLong();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.reason);
        buf.writeLong(this.until);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
        this.handle();
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public long getUntil() {
        return this.until;
    }
    
    public void handle() {
	System.out.println(String.format("Recieved Not Allowed Packet reason: %s", this.reason));
	
        if (this.until <= 0L) {
//            ave.A().a((Runnable)new Runnable() {
//                @Override
//                public void run() {
//                    LabyMod.getInstance().connectToServer(null);
//                    final String message = (PacketNotAllowed.this.reason == null || PacketNotAllowed.this.reason.isEmpty()) ? LanguageManager.translate("chat_unknown_kick_reason") : PacketNotAllowed.this.reason;
//                    final axu prevScreen = (axu)LabyMod.getInstance().getGuiOpenListener().getGuiMultiplayer();
//                    LabyModCore.getMinecraft().openGuiDisconnected((axu)((prevScreen == null) ? new aya() : prevScreen), "disconnect.lost", ModColor.createColors(message));
//                }
//            });
        }
//        else {
//            ave.A().a((Runnable)new Runnable() {
//                @Override
//                public void run() {
//                    LabyMod.getInstance().connectToServer(null);
//                }
//            });
//            ave.A().a((axu)new GuiNotAllowed(this.reason));
//        }
    }
}
