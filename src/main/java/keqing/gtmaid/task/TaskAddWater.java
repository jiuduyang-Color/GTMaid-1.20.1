package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorAddWater;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TaskAddWater extends AbstractGTMaidTask {
    public TaskAddWater() {
        super("addwater", new ItemStack(Items.WATER_BUCKET), machineSound(), 0.4f,
                "Fill cauldrons with water");
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                0.6f,
                4,
                entity -> !ItemsUtil.getStack(entity.getAvailableInv(false),
                        stack -> stack.is(Items.WATER_BUCKET)).isEmpty(),
                (entity, pos) -> {
                    BlockState state = entity.level().getBlockState(pos);
                    if (state.is(Blocks.WATER_CAULDRON)) {
                        return state.getValue(LayeredCauldronBlock.LEVEL) < 3;
                    }
                    return state.is(Blocks.CAULDRON);
                });
        BehaviorAddWater workTask = new BehaviorAddWater();
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
