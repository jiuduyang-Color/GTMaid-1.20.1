package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BehaviorPickFluid extends AbstractWorkAtTargetBehavior {
    private final Fluid fluid;
    private final Item filledBucket;

    public BehaviorPickFluid(Fluid fluid, Item filledBucket) {
        super(2.0);
        this.fluid = fluid;
        this.filledBucket = filledBucket;
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.getFluidState().is(this.fluid) || !state.getFluidState().isSource()) {
            return false;
        }
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        ItemStack bucket = ItemsUtil.getStack(inv, stack -> stack.is(Items.BUCKET));
        if (bucket.isEmpty()) {
            return false;
        }
        bucket.shrink(1);
        ItemHandlerHelper.insertItemStacked(inv, new ItemStack(this.filledBucket), false);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        maid.swing(InteractionHand.MAIN_HAND);
        return true;
    }
}
