package dxxi3d.simple_hostile_despawn.forge;

import dxxi3d.simple_hostile_despawn.ModLogger;
import dxxi3d.simple_hostile_despawn.SharedConstants;
import net.minecraftforge.fml.common.Mod;

@Mod(SharedConstants.MOD_ID)
public final class MainForge {

    public MainForge() {
        ModLogger.sendInitialized();
    }
}
