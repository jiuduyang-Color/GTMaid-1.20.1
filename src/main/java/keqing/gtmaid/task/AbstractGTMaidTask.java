package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public abstract class AbstractGTMaidTask implements IMaidTask {
    private final ResourceLocation uid;
    private final ItemStack icon;
    private final SoundEvent ambientSound;
    private final float soundVolume;
    private final String actionSummary;

    protected AbstractGTMaidTask(String path, ItemStack icon, @Nullable SoundEvent ambientSound,
                                float soundVolume, String actionSummary) {
        this.uid = new ResourceLocation("gtmaid", path);
        this.icon = icon;
        this.ambientSound = ambientSound;
        this.soundVolume = soundVolume;
        this.actionSummary = actionSummary;
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        if (ambientSound == null) {
            return null;
        }
        return SoundUtil.environmentSound(maid, ambientSound, soundVolume);
    }

    protected static SoundEvent breakSound() {
        return InitSounds.MAID_BREAK.get();
    }

    protected static SoundEvent farmSound() {
        return InitSounds.MAID_FARM.get();
    }

    protected static SoundEvent machineSound() {
        return InitSounds.MAID_FURNACE.get();
    }

    @Override
    public String getMaidActionSummary() {
        return actionSummary;
    }
}
