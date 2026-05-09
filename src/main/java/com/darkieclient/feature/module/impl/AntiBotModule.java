package com.darkieclient.feature.module.impl;

import com.darkieclient.DarkieClient;
import com.darkieclient.feature.module.Category;
import com.darkieclient.feature.module.Module;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public final class AntiBotModule extends Module {
    public AntiBotModule() {
        super("AntiBot", "Filters NPCs from players.", Category.CLIENT, Keyboard.KEY_NONE);
    }

    public static boolean shouldIgnore(EntityPlayer player) {
        DarkieClient client = DarkieClient.getInstance();
        if (client == null) {
            return false;
        }

        AntiBotModule antiBot = client.getModuleManager().getModule(AntiBotModule.class);
        return antiBot != null && antiBot.isEnabled() && antiBot.isBot(player);
    }

    public boolean isBot(EntityPlayer player) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (player == null || minecraft.thePlayer == null || minecraft.theWorld == null) {
            return false;
        }

        if (player == minecraft.thePlayer) {
            return false;
        }

        return !isInTabList(minecraft, player);
    }

    private boolean isInTabList(Minecraft minecraft, EntityPlayer player) {
        Collection<NetworkPlayerInfo> playerInfoMap = minecraft.getNetHandler() == null
            ? null
            : minecraft.getNetHandler().getPlayerInfoMap();
        if (playerInfoMap == null || playerInfoMap.isEmpty()) {
            return true;
        }

        for (NetworkPlayerInfo playerInfo : playerInfoMap) {
            if (playerInfo == null || playerInfo.getGameProfile() == null) {
                continue;
            }

            if (player.getUniqueID().equals(playerInfo.getGameProfile().getId())) {
                return true;
            }
        }

        return false;
    }
}
