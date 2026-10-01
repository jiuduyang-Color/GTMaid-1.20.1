package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BehaviorAddWater extends AbstractWorkAtTargetBehavior {
    private static final int MAX_LEVEL = 3;

    public BehaviorAddWater() {
        super(2.0);
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.WATER_CAULDRON)) {
            if (state.getValue(LayeredCauldronBlock.LEVEL) >= MAX_LEVEL) {
                return false;
            }
        } else if (!state.is(Blocks.CAULDRON)) {
            return false;
        }
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        ItemStack waterBucket = ItemsUtil.getStack(inv, stack -> stack.is(Items.WATER_BUCKET));
        if (waterBucket.isEmpty()) {
            return false;
        }
        waterBucket.shrink(1);
        ItemHandlerHelper.insertItemStacked(inv, new ItemStack(Items.BUCKET), false);
        if (state.is(Blocks.CAULDRON)) {
            level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState()
                    .setValue(LayeredCauldronBlock.LEVEL, 1), Block.UPDATE_ALL);
        } else {
            level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL,
                    state.getValue(LayeredCauldronBlock.LEVEL) + 1), Block.UPDATE_ALL);
        }
        maid.swing(InteractionHand.MAIN_HAND);
        return true;
    }
}
