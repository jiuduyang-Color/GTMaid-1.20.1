package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BehaviorPlantTree extends AbstractWorkAtTargetBehavior {
    public BehaviorPlantTree() {
        super(2.0);
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.isAir()) {
            return false;
        }
        if (!level.getBlockState(pos.below()).is(BlockTags.DIRT)) {
            return false;
        }
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        ItemStack sapling = ItemsUtil.getStack(inv, stack -> stack.is(ItemTags.SAPLINGS));
        if (sapling.isEmpty()) {
            return false;
        }
        if (maid.placeItemBlock(pos, sapling)) {
            maid.swing(InteractionHand.MAIN_HAND);
            return true;
        }
        return false;
    }
}
