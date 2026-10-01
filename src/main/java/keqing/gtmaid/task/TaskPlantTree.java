package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorPlantTree;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TaskPlantTree extends AbstractGTMaidTask {
    public TaskPlantTree() {
        super("tree", new ItemStack(Items.OAK_SAPLING), farmSound(), 0.5f, "Plant saplings");
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                0.6f,
                4,
                entity -> !ItemsUtil.getStack(entity.getAvailableInv(false),
                        stack -> stack.is(ItemTags.SAPLINGS)).isEmpty(),
                (entity, pos) -> {
                    BlockState state = entity.level().getBlockState(pos);
                    if (!state.isAir()) {
                        return false;
                    }
                    return entity.level().getBlockState(pos.below()).is(BlockTags.DIRT);
                });
        BehaviorPlantTree workTask = new BehaviorPlantTree();
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
