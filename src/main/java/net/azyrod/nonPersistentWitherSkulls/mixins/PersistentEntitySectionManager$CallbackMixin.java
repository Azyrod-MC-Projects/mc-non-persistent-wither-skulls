package net.azyrod.nonPersistentWitherSkulls.mixins;

import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.entity.PersistentEntitySectionManager$Callback")
public class PersistentEntitySectionManager$CallbackMixin {
    @Final
    @Shadow
    private EntityAccess entity;

    @Inject(method = "updateStatus(Lnet/minecraft/world/level/entity/Visibility;Lnet/minecraft/world/level/entity/Visibility;)V", at = @At("RETURN"))
    private void updateLoadStatus(Visibility oldStatus, Visibility newStatus, CallbackInfo ci) {
        if (entity instanceof WitherSkull skull && skull.level() instanceof ServerLevel world) {
            if (!world.isPositionEntityTicking(skull.blockPosition()) && !skull.getDeltaMovement().equals(Vec3.ZERO)) {
                // If the skull reached an unloaded chunk, and it has motion: discard it
                skull.discard();
            }
        }
    }
}