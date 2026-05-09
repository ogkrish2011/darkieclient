package com.darkieclient.feature.module.impl;

import com.darkieclient.feature.module.Category;
import com.darkieclient.feature.module.Module;
import net.minecraft.client.Minecraft;

public final class FullBrightModule extends Module {
    private float oldGamma;

    public FullBrightModule() {
        super("FullBright", "Makes everything bright.", Category.RENDER, 0);
    }

    @Override
    protected void onEnable() {
        oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0F;
    }

    @Override
    protected void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = oldGamma;
    }
}
