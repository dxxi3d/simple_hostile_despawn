package dxxi3d.simple_hostile_despawn.neoforge;

import dxxi3d.simple_hostile_despawn.ModLogger;
import dxxi3d.simple_hostile_despawn.SharedConstants;
import net.neoforged.fml.common.Mod;

@Mod(SharedConstants.MOD_ID)
public final class MainNeoForge {

    public MainNeoForge() {
        ModLogger.sendInitialized();
    }
}
