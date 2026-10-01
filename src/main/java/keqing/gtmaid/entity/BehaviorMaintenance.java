package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.common.data.GTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BehaviorMaintenance extends AbstractWorkAtTargetBehavior {
    public BehaviorMaintenance() {
        super(2.0);
    }

    public static boolean needsTaping(ServerLevel level, BlockPos pos) {
        IMaintenanceMachine machine = findMaintenanceMachine(level, pos);
        return machine != null && !machine.isTaped();
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        IMaintenanceMachine machine = findMaintenanceMachine(level, pos);
        if (machine == null || machine.isTaped()) {
            return false;
        }
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        ItemStack tape = ItemsUtil.getStack(inv, stack -> stack.is(GTItems.DUCT_TAPE.get()));
        if (tape.isEmpty()) {
            return false;
        }
        tape.shrink(1);
        machine.setTaped(true);
        maid.swing(InteractionHand.MAIN_HAND);
        return true;
    }

    private static IMaintenanceMachine findMaintenanceMachine(ServerLevel level, BlockPos pos) {

        MetaMachine metaMachine = MetaMachine.getMachine(level, pos);
        if (metaMachine instanceof IMaintenanceMachine machine) {
            return machine;
        }
        for (Direction side : Direction.values()) {
            IMaintenanceMachine machine = GTCapabilityHelper.getMaintenanceMachine(level, pos, side);
            if (machine != null) {
                return machine;
            }
        }
        return null;
    }
}
