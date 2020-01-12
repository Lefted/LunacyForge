package me.lefted.lunacyforge.command;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@SideOnly(Side.CLIENT)
public abstract class Command {

    private String[] names;

    public Command(String... names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    public abstract void execute(String[] strings);
}