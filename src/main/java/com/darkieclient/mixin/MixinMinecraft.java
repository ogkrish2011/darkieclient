package com.darkieclient.mixin;

import com.darkieclient.combat.ClientRotationHelper;
import com.darkieclient.event.PrePlayerInteractEvent;
import com.darkieclient.event.RunTickStartEvent;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "runTick", at = @At("HEAD"))
    private void DarkieClient$onRunTickStart(CallbackInfo callbackInfo) {
        ClientRotationHelper.get().onRunTickStart();
        MinecraftForge.EVENT_BUS.post(new RunTickStartEvent());
        
        // Performance optimization: Cap FPS in menus
        com.darkieclient.feature.module.impl.PerformanceModule perf = com.darkieclient.DarkieClient.getInstance().getModuleManager().getModule(com.darkieclient.feature.module.impl.PerformanceModule.class);
        if (perf != null && perf.isEnabled() && perf.isSmoothFps() && net.minecraft.client.Minecraft.getMinecraft().currentScreen != null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;getMouseOver(F)V", shift = At.Shift.BEFORE))
    private void DarkieClient$beforeGetMouseOver(CallbackInfo callbackInfo) {
        ClientRotationHelper.get().updateServerRotations();
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/settings/GameSettings;chatVisibility:Lnet/minecraft/entity/player/EntityPlayer$EnumChatVisibility;"))
    private void DarkieClient$beforePlayerInteract(CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post(new PrePlayerInteractEvent());
    }
}
