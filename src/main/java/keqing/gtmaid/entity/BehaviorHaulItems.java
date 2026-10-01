package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.utils.GTTransferUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BehaviorHaulItems extends AbstractWorkAtTargetBehavior {
    public BehaviorHaulItems() {
        super(2.0);
    }

    public static boolean hasExportableItems(ServerLevel level, BlockPos pos) {
        MetaMachine machine = MetaMachine.getMachine(level, pos);
        if (machine == null) {
            return false;
        }
        for (MachineTrait trait : machine.getTraits()) {
            if (trait instanceof NotifiableItemStackHandler handler
                && handler.handlerIO == IO.OUT
                && !handler.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        MetaMachine machine = MetaMachine.getMachine(level, pos);
        if (machine == null) {
            return false;
        }
        CombinedInvWrapper maidInv = maid.getAvailableInv(false);
        boolean moved = false;
        for (MachineTrait trait : machine.getTraits()) {
            if (trait instanceof NotifiableItemStackHandler handler && handler.handlerIO == IO.OUT) {
                GTTransferUtils.moveInventoryItems(handler, maidInv);
                moved = true;
            }
        }
        return moved;
    }
}
