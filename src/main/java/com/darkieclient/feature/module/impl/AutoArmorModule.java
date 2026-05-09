package com.darkieclient.feature.module.impl;

import com.darkieclient.feature.module.Category;
import com.darkieclient.feature.module.Module;
import com.darkieclient.feature.setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class AutoArmorModule extends Module {
    private final NumberSetting delay = new NumberSetting("Delay", 100, 0, 500, 10);

    public AutoArmorModule() {
        super("AutoArmor", "Automatically equips the best armor.", Category.PLAYER, 0);
        addSetting(delay);
    }

    private long lastEquip = 0;

    @Override
    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || (mc.currentScreen != null && !(mc.currentScreen instanceof GuiInventory))) {
            return;
        }

        if (System.currentTimeMillis() - lastEquip < (long) delay.getValue()) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            if (equipArmor(i)) {
                lastEquip = System.currentTimeMillis();
                break;
            }
        }
    }

    private boolean equipArmor(int slot) {
        Minecraft mc = Minecraft.getMinecraft();
        int armorSlot = 3 - slot;
        ItemStack currentArmor = mc.thePlayer.inventory.armorItemInSlot(armorSlot);
        int bestSlot = -1;
        float bestReduction = currentArmor != null ? getReduction(currentArmor) : -1;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();
                if (armor.armorType == slot) {
                    float reduction = getReduction(stack);
                    if (reduction > bestReduction) {
                        bestReduction = reduction;
                        bestSlot = i;
                    }
                }
            }
        }

        if (bestSlot != -1) {
            if (currentArmor != null) {
                mc.playerController.windowClick(0, 5 + slot, 0, 4, mc.thePlayer);
            }
            mc.playerController.windowClick(0, bestSlot < 9 ? 36 + bestSlot : bestSlot, 0, 1, mc.thePlayer);
            return true;
        }

        return false;
    }

    private float getReduction(ItemStack stack) {
        return ((ItemArmor) stack.getItem()).damageReduceAmount;
    }
}
