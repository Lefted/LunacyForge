package me.lefted.lunacyforge.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.events.TickEvent;
import me.lefted.lunacyforge.events.UpdateEvent;
import me.lefted.lunacyforge.implementations.IRightClickDelayTimer;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import java.util.function.Predicate;

/* Also see: MixinMovementInputFromOptions.java, MixinItemStack.java */
@ModuleInfo(description = "Sneaks/Unsneaks at the end of a block to build bridges faster\nMade for Bedwars", tags = { "Ninjabridge", "Eaglen" })
public class FastBridge extends Module {

    // VALUES
    @ContainerInfo(hoverText = "Adjusts the rightclick timer")
    @SliderInfo(description = "Speed", min = 0, max = 4, step = 1D, numberType = ContainerSlider.NumberType.INTEGER)
    private Value<Integer> rightClickDelayValue = new Value("rightClickDelay", Integer.valueOf(3));

    @ContainerInfo(hoverText = "Useful for onestacks")
    @CheckboxInfo(description = "Sneak when jumping")
    private Value<Boolean> useOnestackValue = new Value("useOnestack", Boolean.valueOf(false));

    @ContainerInfo(hoverText = "If enabled, you can fall off when you're not holding blocks", groupID = 0)
    @CheckboxInfo(description = "Only sneak when holding blocks")
    private Value<Boolean> useBlockSneakValue = new Value<Boolean>("useBlockSneak", Boolean.valueOf(false), new String[] { "safeSneakTime" },
	newValue -> newValue);

    @ContainerInfo(hoverText = "Time in ticks you keep sneaking in case your blocks go out", groupID = 0)
    @SliderInfo(min = 0, max = 100, step = 5D, description = "Safetime", numberType = NumberType.INTEGER)
    private Value<Integer> safeSneakTimeValue = new Value("safeSneakTime", Integer.valueOf(20));

    // ATTRIBUTES
    public int safeSneakDelay;
    private boolean sneak = false;
    private boolean inAir;

    // CONSTRUCTOR
    public FastBridge() {
	super("FastBridge", Category.MOVEMENT);
    }

    // METHODS
    @EventTarget
    public void onUpdate(UpdateEvent event) {
	if (this.isEnabled()) {
	    if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
		this.inAir = true;
	    }
	    if (this.mc.thePlayer.onGround) {
		this.inAir = false;
	    }
	    if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
		this.sneak = true;
	    } else if (this.mc.thePlayer != null && this.mc.theWorld != null && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()) {

		final ItemStack stack = this.mc.thePlayer.getCurrentEquippedItem();
		final BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);

		// bridging
		if (this.mc.thePlayer.onGround) {
		    if (stack != null || (!(Boolean) this.useBlockSneakValue.getObject())) {
			if (mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {

			    if (stack != null) {
				if (stack.getItem() != null) {
				    if (stack.getItem() instanceof ItemBlock) {
					this.safeSneakDelay = 0;
				    }
				}
			    }

			    if (this.mc.objectMouseOver != null) {
				if (this.mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
				    // the block the player is aiming at
				    final BlockPos block = this.mc.objectMouseOver.getBlockPos();

				    // if the the above block is in a certain distance
				    final double diffX = (block.getX() - mc.thePlayer.posX);
				    final double diffZ = (block.getZ() - mc.thePlayer.posZ);
				    final double diffY = (block.getY() - mc.thePlayer.posY - 1D);

				    /*
				     * x max:0.3 min: -1.3
				     * z max:0.3 min: -1.3
				     * y must be -2
				     */
				    if (diffY == -2.0D && diffX < 0.3D && diffX > -1.3D && diffZ < 0.3D && diffZ > -1.3D) {
					// if delay is set grater than 0
					if (!this.sneak) {
					    this.onSneak();
					}
					// if delay is set to 0
					if ((Integer) this.rightClickDelayValue.getObject() == 0) {
					    ((IRightClickDelayTimer) this.mc).setRightClickDelayTimer(0);
					}
				    }
				}
			    }
			    this.sneak = true;

			} else {
			    this.sneak = false;
			}
		    } else {
			this.sneak = false;
		    }
		}

		// one stack and co
		if (this.inAir && (Boolean) this.useOnestackValue.getObject()) {
		    this.sneak = true;
		    if (this.mc.objectMouseOver != null) {
			if (this.mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
			    // the block the player is aiming at
			    final BlockPos block = this.mc.objectMouseOver.getBlockPos();

			    // if the the above block is in a certain distance
			    final double diffX = (block.getX() - mc.thePlayer.posX);
			    final double diffZ = (block.getZ() - mc.thePlayer.posZ);
			    final double diffY = (block.getY() - mc.thePlayer.posY - 1D);

			    /*
			     * x max:0.3 min: -1.3
			     * z max:0.3 min: -1.3
			     * y max -2.0 min: -0.4
			     */
			    // above diffY == -2.0D && diffX < 0.3D && diffX > -1.3D && diffZ < 0.3D && diffZ > -1.3D
			    if (diffY <= -2.0D && diffY >= -4.0D && diffX < 1D && diffX > -2.0D && diffZ < 1D && diffZ > -2.0D) {
				((IRightClickDelayTimer) this.mc).setRightClickDelayTimer(0);
			    }
			}
		    }

		}

	    } else {
		this.sneak = false;
	    }
	    // safe sneaking if all blocks were placed
	    if (this.safeSneakDelay != 0 && ((Boolean) this.useBlockSneakValue.getObject())) {
		this.sneak = true;
	    }
	}
    }

    public void allBlocksUsed() {
	this.safeSneakDelay = (Integer) this.safeSneakTimeValue.getObject();
    }

    private void onSneak() {
	final int delay = (Integer) this.rightClickDelayValue.getObject();

	if (delay > 0) {

	    // works if delay is grater 1
	    ((IRightClickDelayTimer) this.mc).setRightClickDelayTimer(delay + 2);
	}
    }

    public boolean isSneaking() {
	return this.sneak;
    }

    @EventTarget
    public void onTick(TickEvent e) {
	if (this.safeSneakDelay != 0) {
	    this.safeSneakDelay--;
	}
    }

    @Override
    public void onEnable() {
	EventManager.register(this);
    }

    @Override
    public void onDisable() {
	this.sneak = false;
	this.safeSneakDelay = 0;
	EventManager.unregister(this);
    }

}
