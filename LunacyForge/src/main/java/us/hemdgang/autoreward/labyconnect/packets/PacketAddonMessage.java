package us.hemdgang.autoreward.labyconnect.packets;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketAddonMessage extends Packet{

    private String key;
    private byte[] data;
    
    public PacketAddonMessage(final String key, final byte[] data) {
        this.key = key;
        this.data = data;
    }
    
    public PacketAddonMessage(final String key, final String json) {
        this.key = key;
        this.data = this.toBytes(json);
    }
    
    public PacketAddonMessage() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.key = buf.readString();
        final byte[] data = new byte[buf.readInt()];
        buf.readBytes(data);
        this.data = data;
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.key);
        buf.writeInt(this.data.length);
        buf.writeBytes(this.data);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getJson() {
        try {
            final StringBuilder outStr = new StringBuilder();
            if (this.data == null || this.data.length == 0) {
                return "";
            }
            if (this.isCompressed(this.data)) {
                final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(this.data));
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    outStr.append(line);
                }
            }
            else {
                outStr.append(Arrays.toString(this.data));
            }
            return outStr.toString();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private byte[] toBytes(final String in) {
        final byte[] str = in.getBytes(StandardCharsets.UTF_8);
        try {
            if (str == null || str.length == 0) {
                return new byte[0];
            }
            final ByteArrayOutputStream obj = new ByteArrayOutputStream();
            final GZIPOutputStream gzip = new GZIPOutputStream(obj);
            gzip.write(str);
            gzip.flush();
            gzip.close();
            return obj.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private boolean isCompressed(final byte[] compressed) {
        return compressed[0] == 31 && compressed[1] == -117;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public byte[] getData() {
        return this.data;
    }
}
