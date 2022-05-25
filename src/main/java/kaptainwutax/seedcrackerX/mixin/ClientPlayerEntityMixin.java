package kaptainwutax.seedcrackerX.mixin;

import com.mojang.brigadier.StringReader;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        SeedCracker.get().getDataStorage().tick();
    }

    @Inject(method = "sendCommand(Ljava/lang/String;Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void onSendCommand(String command, Text preview, CallbackInfo ci) {
        StringReader reader = new StringReader(command);
        if (ClientCommands.isClientSideCommand(command.split(Pattern.quote(" ")))) {
            ClientCommands.executeCommand(reader);
            ci.cancel();
        }
    }

}
