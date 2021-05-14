package me.lefted.lunacyforge.injection.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;
import us.hemdgang.autoreward.TestMain;

@SideOnly(Side.CLIENT)
@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

    @Shadow
    private Channel channel;

    private static final Class[] ignore = { C03PacketPlayer.class, S16PacketEntityLook.class, S35PacketUpdateTileEntity.class, S12PacketEntityVelocity.class,
	    C04PacketPlayerPosition.class, S19PacketEntityHeadLook.class, S15PacketEntityRelMove.class, S17PacketEntityLookMove.class,
	    S20PacketEntityProperties.class, S0FPacketSpawnMob.class, S1CPacketEntityMetadata.class, S20PacketEntityProperties.class,
	    S1DPacketEntityEffect.class, S33PacketUpdateSign.class, S26PacketMapChunkBulk.class, S18PacketEntityTeleport.class, C05PacketPlayerLook.class,
	    S03PacketTimeUpdate.class, S19PacketEntityStatus.class, S29PacketSoundEffect.class, C0BPacketEntityAction.class, C06PacketPlayerPosLook.class,
	    S13PacketDestroyEntities.class, S04PacketEntityEquipment.class, S21PacketChunkData.class, S22PacketMultiBlockChange.class,
	    S23PacketBlockChange.class, S3CPacketUpdateScore.class, S3EPacketTeams.class, S2BPacketChangeGameState.class };
    private static final List<Class> ignoreList = Arrays.asList(ignore);

    @Inject(method = "channelRead0", at = @At("HEAD"))
    public void lunacy$onPacketRead(ChannelHandlerContext ctx, Packet packet, CallbackInfo ci) {
	if (!channel.isOpen())
	    return;

	if (TestMain.readIncomingPackets) {

	    if (TestMain.ignore && ignoreList.contains(packet.getClass()))
		return;

	    String info = String.format("<- %s", packet.getClass().getSimpleName());
	    if (TestMain.logInChat) {
		Logger.logChatMessage(info);
	    } else
		System.out.println(info);
	}
    }

    @Inject(method = "sendPacket*", at = @At("HEAD"))
    public void lunacy$onPacketSend(Packet packet, CallbackInfo ci) {

	if (TestMain.readOutgoingPackets) {

	    if (TestMain.ignore && ignoreList.contains(packet.getClass()))
		return;

	    String info = String.format("-> %s", packet.getClass().getSimpleName());
	    if (TestMain.logInChat)
		Logger.logChatMessage(info);
	    else
		System.out.println(info);
	}
    }

}
