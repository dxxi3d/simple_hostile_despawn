package dxxi3d.simple_hostile_despawn.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.DropChances;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin {

    @WrapOperation(method = "requiresCustomPersistence", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isPassenger()Z"))
    private boolean wrapPassengerPersistence(Mob mob, Operation<Boolean> original) {
        return false;
    }

    @WrapOperation(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isPersistenceRequired()Z"))
    private boolean wrapLootPersistence(Mob mob, Operation<Boolean> original) {
        if (!mob.hasCustomName()) {
            DropChances dropChances = mob.getDropChances();
            for (EquipmentSlot slot : EquipmentSlot.VALUES) {
                if (dropChances.isPreserved(slot) && !mob.getItemBySlot(slot).isEmpty()) {
                    return false;
                }
            }
            if (mob instanceof InventoryCarrier carrier && !carrier.getInventory().isEmpty()) {
                return false;
            }
        }
        return original.call(mob);
    }

    @WrapOperation(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;discard()V"))
    private void wrapDespawnDiscard(Mob mob, Operation<Void> original) {
        if (mob.isPersistenceRequired() && mob.level() instanceof ServerLevel serverLevel) {
            mob.dropPreservedEquipment(serverLevel);
            if (mob instanceof InventoryCarrier carrier) {
                for (ItemStack stack : carrier.getInventory().removeAllItems()) {
                    mob.spawnAtLocation(serverLevel, stack);
                }
            }
        }
        original.call(mob);
    }
}
