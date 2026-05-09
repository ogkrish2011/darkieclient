package com.darkieclient.feature.module.impl;

import com.darkieclient.feature.module.Category;
import com.darkieclient.feature.module.Module;
import com.darkieclient.feature.setting.BooleanSetting;
import net.minecraft.client.Minecraft;

public final class PerformanceModule extends Module {
    private final BooleanSetting fastRender = new BooleanSetting("Fast Render", true);
    private final BooleanSetting smoothFps = new BooleanSetting("Smooth FPS", true);
    private final BooleanSetting clearMemory = new BooleanSetting("Clear Memory", false);

    public PerformanceModule() {
        super("Performance", "Optimizes client performance and rendering.", Category.MISC, 0);
        addSetting(fastRender);
        addSetting(smoothFps);
        addSetting(clearMemory);
    }

    @Override
    public void onClientTick() {
        if (clearMemory.isEnabled()) {
            System.gc();
            clearMemory.setEnabled(false);
        }
    }

    public boolean isFastRender() {
        return isEnabled() && fastRender.isEnabled();
    }

    public boolean isSmoothFps() {
        return isEnabled() && smoothFps.isEnabled();
    }
}
