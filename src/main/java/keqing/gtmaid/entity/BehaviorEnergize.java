package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;

public class BehaviorEnergize extends AbstractWorkAtTargetBehavior {
    private static final long MAX_EU_PER_WORK = 32L;

    public BehaviorEnergize() {
        super(2.0);
    }

    public static boolean needsEnergy(ServerLevel level, BlockPos pos) {
        for (Direction side : Direction.values()) {
            IEnergyContainer container = GTCapabilityHelper.getEnergyContainer(level, pos, side);
            if (container != null
                && container.inputsEnergy(side)
                && container.getEnergyStored() < container.getEnergyCapacity()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        for (Direction side : Direction.values()) {
            IEnergyContainer container = GTCapabilityHelper.getEnergyContainer(level, pos, side);
            if (container == null || !container.inputsEnergy(side)) {
                continue;
            }
            if (container.getEnergyStored() >= container.getEnergyCapacity()) {
                continue;
            }
            long voltage = Math.max(1L, container.getInputVoltage());
            long amperage = Math.max(1L, getEnergyAmount(maid) / voltage);
            long accepted = container.acceptEnergyFromNetwork(side, voltage, amperage);
            if (accepted > 0) {
                maid.swing(InteractionHand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }

    private static long getEnergyAmount(EntityMaid maid) {
        int experience = getMaidExperience(maid);
        return Math.min(Math.max(experience * 2L, 1L), MAX_EU_PER_WORK);
    }

    private static int getMaidExperience(EntityMaid maid) {
        CompoundTag tag = new CompoundTag();
        maid.saveWithoutId(tag);
        return tag.getInt(EntityMaid.EXPERIENCE_TAG);
    }
}
