package me.lefted.lunacyforge.implementations;

import net.minecraft.util.Timer;

public interface ITimer {

    Timer getAimAssistTimer();
    
    Timer getBowAimbotTimer();
    
    Timer getMinecraftTimer();

    int getRightClickDelayTimer();
    
    void setRightClickDelayTimer(int rightClickDelayTimer);
}
