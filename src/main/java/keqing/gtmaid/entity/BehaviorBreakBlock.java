package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import keqing.gtmaid.GMConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class BehaviorBreakBlock extends AbstractWorkAtTargetBehavior {
    private final Predicate<BlockState> targetState;
    private final boolean chainAllowed;

    public BehaviorBreakBlock(Predicate<BlockState> targetState, double closeEnoughDist, boolean chainAllowed) {
        super(closeEnoughDist);
        this.targetState = targetState;
        this.chainAllowed = chainAllowed;
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos center) {
        if (this.chainAllowed && GMConfig.chainSwitch) {
            return this.breakChain(level, maid, center);
        }
        return this.breakOne(level, maid, center);
    }

    private boolean breakOne(ServerLevel level, EntityMaid maid, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || !this.targetState.test(state)) {
            return false;
        }
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        maid.dropResourcesToMaidInv(state, level, pos, blockEntity, maid, maid.getMainHandItem());
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        maid.getMainHandItem().hurtAndBreak(1, maid, entity -> {
        });
        maid.swing(InteractionHand.MAIN_HAND);
        return true;
    }

    private boolean breakChain(ServerLevel level, EntityMaid maid, BlockPos center) {
        BlockState centerState = level.getBlockState(center);
        if (centerState.isAir() || !this.targetState.test(centerState)) {
            return false;
        }
        int range = GMConfig.chainRange;
        boolean brokeAny = false;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    if (this.breakOne(level, maid, center.offset(x, y, z))) {
                        brokeAny = true;
                    }
                }
            }
        }
        return brokeAny || this.breakOne(level, maid, center);
    }
}
