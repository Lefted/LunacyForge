package us.hemdgang.autoreward.labyconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

public class PacketBufOld extends PacketBuf {

    public PacketBufOld(final ByteBuf buf) {
        super(buf);
    }
    
    public int refCnt() {
        return this.buf.refCnt();
    }
    
    public boolean release() {
        return this.buf.release();
    }
    
    public boolean release(final int arg0) {
        return this.buf.release(arg0);
    }
    
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }
    
    public byte[] array() {
        return this.buf.array();
    }
    
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }
    
    public int bytesBefore(final byte arg0) {
        return this.buf.bytesBefore(arg0);
    }
    
    public int bytesBefore(final int arg0, final byte arg1) {
        return this.buf.bytesBefore(arg0, arg1);
    }
    
    public int bytesBefore(final int arg0, final int arg1, final byte arg2) {
        return this.buf.bytesBefore(arg0, arg1, arg2);
    }
    
    public int capacity() {
        return this.buf.capacity();
    }
    
    public ByteBuf capacity(final int arg0) {
        return this.buf.capacity(arg0);
    }
    
    public ByteBuf clear() {
        return this.buf.clear();
    }
    
    public int compareTo(final ByteBuf arg0) {
        return this.buf.compareTo(arg0);
    }
    
    public ByteBuf copy() {
        return this.buf.copy();
    }
    
    public ByteBuf copy(final int arg0, final int arg1) {
        return this.buf.copy(arg0, arg1);
    }
    
    public ByteBuf discardReadBytes() {
        return this.buf.discardReadBytes();
    }
    
    public ByteBuf discardSomeReadBytes() {
        return this.buf.discardSomeReadBytes();
    }
    
    public ByteBuf duplicate() {
        return this.buf.duplicate();
    }
    
    public ByteBuf ensureWritable(final int arg0) {
        return this.buf.ensureWritable(arg0);
    }
    
    public int ensureWritable(final int arg0, final boolean arg1) {
        return this.buf.ensureWritable(arg0, arg1);
    }
    
    public boolean equals(final Object arg0) {
        return this.buf.equals(arg0);
    }
    
    public int forEachByte(final ByteBufProcessor arg0) {
        return this.buf.forEachByte(arg0);
    }
    
    public int forEachByte(final int arg0, final int arg1, final ByteBufProcessor arg2) {
        return this.buf.forEachByte(arg0, arg1, arg2);
    }
    
    public int forEachByteDesc(final ByteBufProcessor arg0) {
        return this.buf.forEachByteDesc(arg0);
    }
    
    public int forEachByteDesc(final int arg0, final int arg1, final ByteBufProcessor arg2) {
        return this.buf.forEachByteDesc(arg0, arg1, arg2);
    }
    
    public boolean getBoolean(final int arg0) {
        return this.buf.getBoolean(arg0);
    }
    
    public byte getByte(final int arg0) {
        return this.buf.getByte(arg0);
    }
    
    public ByteBuf getBytes(final int arg0, final ByteBuf arg1) {
        return this.buf.getBytes(arg0, arg1);
    }
    
    public ByteBuf getBytes(final int arg0, final byte[] arg1) {
        return this.buf.getBytes(arg0, arg1);
    }
    
    public ByteBuf getBytes(final int arg0, final ByteBuffer arg1) {
        return this.buf.getBytes(arg0, arg1);
    }
    
    public ByteBuf getBytes(final int arg0, final ByteBuf arg1, final int arg2) {
        return this.buf.getBytes(arg0, arg1, arg2);
    }
    
    public ByteBuf getBytes(final int arg0, final OutputStream arg1, final int arg2) throws IOException {
        return this.buf.getBytes(arg0, arg1, arg2);
    }
    
    public int getBytes(final int arg0, final GatheringByteChannel arg1, final int arg2) throws IOException {
        return this.buf.getBytes(arg0, arg1, arg2);
    }
    
    public ByteBuf getBytes(final int arg0, final ByteBuf arg1, final int arg2, final int arg3) {
        return this.buf.getBytes(arg0, arg1, arg2, arg3);
    }
    
    public ByteBuf getBytes(final int arg0, final byte[] arg1, final int arg2, final int arg3) {
        return this.buf.getBytes(arg0, arg1, arg2, arg3);
    }
    
    public char getChar(final int arg0) {
        return this.buf.getChar(arg0);
    }
    
    public double getDouble(final int arg0) {
        return this.buf.getDouble(arg0);
    }
    
    public float getFloat(final int arg0) {
        return this.buf.getFloat(arg0);
    }
    
    public int getInt(final int arg0) {
        return this.buf.getInt(arg0);
    }
    
    public long getLong(final int arg0) {
        return this.buf.getLong(arg0);
    }
    
    public int getMedium(final int arg0) {
        return this.buf.getMedium(arg0);
    }
    
    public short getShort(final int arg0) {
        return this.buf.getShort(arg0);
    }
    
    public short getUnsignedByte(final int arg0) {
        return this.buf.getUnsignedByte(arg0);
    }
    
    public long getUnsignedInt(final int arg0) {
        return this.buf.getUnsignedInt(arg0);
    }
    
    public int getUnsignedMedium(final int arg0) {
        return this.buf.getUnsignedMedium(arg0);
    }
    
    public int getUnsignedShort(final int arg0) {
        return this.buf.getUnsignedShort(arg0);
    }
    
    public boolean hasArray() {
        return this.buf.hasArray();
    }
    
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }
    
    public int hashCode() {
        return this.buf.hashCode();
    }
    
    public int indexOf(final int arg0, final int arg1, final byte arg2) {
        return this.buf.indexOf(arg0, arg1, arg2);
    }
    
    public ByteBuffer internalNioBuffer(final int arg0, final int arg1) {
        return this.buf.internalNioBuffer(arg0, arg1);
    }
    
    public boolean isDirect() {
        return this.buf.isDirect();
    }
    
    public boolean isReadable() {
        return this.buf.isReadable();
    }
    
    public boolean isReadable(final int arg0) {
        return this.buf.isReadable(arg0);
    }
    
    public boolean isWritable() {
        return this.buf.isWritable();
    }
    
    public boolean isWritable(final int arg0) {
        return this.buf.isWritable(arg0);
    }
    
    public ByteBuf markReaderIndex() {
        return this.buf.markReaderIndex();
    }
    
    public ByteBuf markWriterIndex() {
        return this.buf.markWriterIndex();
    }
    
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }
    
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }
    
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }
    
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer();
    }
    
    public ByteBuffer nioBuffer(final int arg0, final int arg1) {
        return this.buf.nioBuffer(arg0, arg1);
    }
    
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }
    
    public ByteBuffer[] nioBuffers() {
        return this.buf.nioBuffers();
    }
    
    public ByteBuffer[] nioBuffers(final int arg0, final int arg1) {
        return this.buf.nioBuffers(arg0, arg1);
    }
    
    public ByteOrder order() {
        return this.buf.order();
    }
    
    public ByteBuf order(final ByteOrder arg0) {
        return this.buf.order(arg0);
    }
    
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }
    
    public byte readByte() {
        return this.buf.readByte();
    }
    
    public ByteBuf readBytes(final int arg0) {
        return this.buf.readBytes(arg0);
    }
    
    public ByteBuf readBytes(final ByteBuf arg0) {
        return this.buf.readBytes(arg0);
    }
    
    public ByteBuf readBytes(final byte[] arg0) {
        return this.buf.readBytes(arg0);
    }
    
    public ByteBuf readBytes(final ByteBuffer arg0) {
        return this.buf.readBytes(arg0);
    }
    
    public ByteBuf readBytes(final ByteBuf arg0, final int arg1) {
        return this.buf.readBytes(arg0, arg1);
    }
    
    public ByteBuf readBytes(final OutputStream arg0, final int arg1) throws IOException {
        return this.buf.readBytes(arg0, arg1);
    }
    
    public int readBytes(final GatheringByteChannel arg0, final int arg1) throws IOException {
        return this.buf.readBytes(arg0, arg1);
    }
    
    public ByteBuf readBytes(final ByteBuf arg0, final int arg1, final int arg2) {
        return this.buf.readBytes(arg0, arg1, arg2);
    }
    
    public ByteBuf readBytes(final byte[] arg0, final int arg1, final int arg2) {
        return this.buf.readBytes(arg0, arg1, arg2);
    }
    
    public char readChar() {
        return this.buf.readChar();
    }
    
    public double readDouble() {
        return this.buf.readDouble();
    }
    
    public float readFloat() {
        return this.buf.readFloat();
    }
    
    public int readInt() {
        return this.buf.readInt();
    }
    
    public long readLong() {
        return this.buf.readLong();
    }
    
    public int readMedium() {
        return this.buf.readMedium();
    }
    
    public short readShort() {
        return this.buf.readShort();
    }
    
    public ByteBuf readSlice(final int arg0) {
        return this.buf.readSlice(arg0);
    }
    
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }
    
    public long readUnsignedInt() {
        return this.buf.readUnsignedInt();
    }
    
    public int readUnsignedMedium() {
        return this.buf.readUnsignedMedium();
    }
    
    public int readUnsignedShort() {
        return this.buf.readUnsignedShort();
    }
    
    public int readableBytes() {
        return this.buf.readableBytes();
    }
    
    public int readerIndex() {
        return this.buf.readerIndex();
    }
    
    public ByteBuf readerIndex(final int arg0) {
        return this.buf.readerIndex(arg0);
    }
    
    public ByteBuf resetReaderIndex() {
        return this.buf.resetReaderIndex();
    }
    
    public ByteBuf resetWriterIndex() {
        return this.buf.resetWriterIndex();
    }
    
    public ByteBuf retain() {
        return this.buf.retain();
    }
    
    public ByteBuf retain(final int arg0) {
        return this.buf.retain(arg0);
    }
    
    public ByteBuf setBoolean(final int arg0, final boolean arg1) {
        return this.buf.setBoolean(arg0, arg1);
    }
    
    public ByteBuf setByte(final int arg0, final int arg1) {
        return this.buf.setByte(arg0, arg1);
    }
    
    public ByteBuf setBytes(final int arg0, final ByteBuf arg1) {
        return this.buf.setBytes(arg0, arg1);
    }
    
    public ByteBuf setBytes(final int arg0, final byte[] arg1) {
        return this.buf.setBytes(arg0, arg1);
    }
    
    public ByteBuf setBytes(final int arg0, final ByteBuffer arg1) {
        return this.buf.setBytes(arg0, arg1);
    }
    
    public ByteBuf setBytes(final int arg0, final ByteBuf arg1, final int arg2) {
        return this.buf.setBytes(arg0, arg1, arg2);
    }
    
    public int setBytes(final int arg0, final InputStream arg1, final int arg2) throws IOException {
        return this.buf.setBytes(arg0, arg1, arg2);
    }
    
    public int setBytes(final int arg0, final ScatteringByteChannel arg1, final int arg2) throws IOException {
        return this.buf.setBytes(arg0, arg1, arg2);
    }
    
    public ByteBuf setBytes(final int arg0, final ByteBuf arg1, final int arg2, final int arg3) {
        return this.buf.setBytes(arg0, arg1, arg2, arg3);
    }
    
    public ByteBuf setBytes(final int arg0, final byte[] arg1, final int arg2, final int arg3) {
        return this.buf.setBytes(arg0, arg1, arg2, arg3);
    }
    
    public ByteBuf setChar(final int arg0, final int arg1) {
        return this.buf.setChar(arg0, arg1);
    }
    
    public ByteBuf setDouble(final int arg0, final double arg1) {
        return this.buf.setDouble(arg0, arg1);
    }
    
    public ByteBuf setFloat(final int arg0, final float arg1) {
        return this.buf.setFloat(arg0, arg1);
    }
    
    public ByteBuf setIndex(final int arg0, final int arg1) {
        return this.buf.setIndex(arg0, arg1);
    }
    
    public ByteBuf setInt(final int arg0, final int arg1) {
        return this.buf.setInt(arg0, arg1);
    }
    
    public ByteBuf setLong(final int arg0, final long arg1) {
        return this.buf.setLong(arg0, arg1);
    }
    
    public ByteBuf setMedium(final int arg0, final int arg1) {
        return this.buf.setMedium(arg0, arg1);
    }
    
    public ByteBuf setShort(final int arg0, final int arg1) {
        return this.buf.setShort(arg0, arg1);
    }
    
    public ByteBuf setZero(final int arg0, final int arg1) {
        return this.buf.setZero(arg0, arg1);
    }
    
    public ByteBuf skipBytes(final int arg0) {
        return this.buf.skipBytes(arg0);
    }
    
    public ByteBuf slice() {
        return this.buf.slice();
    }
    
    public ByteBuf slice(final int arg0, final int arg1) {
        return this.buf.slice(arg0, arg1);
    }
    
    public String toString() {
        return this.buf.toString();
    }
    
    public String toString(final Charset arg0) {
        return this.buf.toString(arg0);
    }
    
    public String toString(final int arg0, final int arg1, final Charset arg2) {
        return this.buf.toString(arg0, arg1, arg2);
    }
    
    public ByteBuf unwrap() {
        return this.buf.unwrap();
    }
    
    public int writableBytes() {
        return this.buf.writableBytes();
    }
    
    public ByteBuf writeBoolean(final boolean arg0) {
        return this.buf.writeBoolean(arg0);
    }
    
    public ByteBuf writeByte(final int arg0) {
        return this.buf.writeByte(arg0);
    }
    
    public ByteBuf writeBytes(final ByteBuf arg0) {
        return this.buf.writeBytes(arg0);
    }
    
    public ByteBuf writeBytes(final byte[] arg0) {
        return this.buf.writeBytes(arg0);
    }
    
    public ByteBuf writeBytes(final ByteBuffer arg0) {
        return this.buf.writeBytes(arg0);
    }
    
    public ByteBuf writeBytes(final ByteBuf arg0, final int arg1) {
        return this.buf.writeBytes(arg0, arg1);
    }
    
    public int writeBytes(final InputStream arg0, final int arg1) throws IOException {
        return this.buf.writeBytes(arg0, arg1);
    }
    
    public int writeBytes(final ScatteringByteChannel arg0, final int arg1) throws IOException {
        return this.buf.writeBytes(arg0, arg1);
    }
    
    public ByteBuf writeBytes(final ByteBuf arg0, final int arg1, final int arg2) {
        return this.buf.writeBytes(arg0, arg1, arg2);
    }
    
    public ByteBuf writeBytes(final byte[] arg0, final int arg1, final int arg2) {
        return this.buf.writeBytes(arg0, arg1, arg2);
    }
    
    public ByteBuf writeChar(final int arg0) {
        return this.buf.writeChar(arg0);
    }
    
    public ByteBuf writeDouble(final double arg0) {
        return this.buf.writeDouble(arg0);
    }
    
    public ByteBuf writeFloat(final float arg0) {
        return this.buf.writeFloat(arg0);
    }
    
    public ByteBuf writeInt(final int arg0) {
        return this.buf.writeInt(arg0);
    }
    
    public ByteBuf writeLong(final long arg0) {
        return this.buf.writeLong(arg0);
    }
    
    public ByteBuf writeMedium(final int arg0) {
        return this.buf.writeMedium(arg0);
    }
    
    public ByteBuf writeShort(final int arg0) {
        return this.buf.writeShort(arg0);
    }
    
    public ByteBuf writeZero(final int arg0) {
        return this.buf.writeZero(arg0);
    }
    
    public int writerIndex() {
        return this.buf.writerIndex();
    }
    
    public ByteBuf writerIndex(final int arg0) {
        return this.buf.writerIndex(arg0);
    }
}
