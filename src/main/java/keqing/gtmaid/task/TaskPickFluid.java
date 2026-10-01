package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorPickFluid;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class TaskPickFluid extends AbstractGTMaidTask {
    private final Fluid fluid;
    private final Item filledBucket;

    public TaskPickFluid(String path, ItemStack icon, Fluid fluid, Item filledBucket, String actionSummary) {
        super(path, icon, machineSound(), 0.4f, actionSummary);
        this.fluid = fluid;
        this.filledBucket = filledBucket;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                0.6f,
                4,
                entity -> !ItemsUtil.getStack(entity.getAvailableInv(false),
                        stack -> stack.is(Items.BUCKET)).isEmpty(),
                (entity, pos) -> {
                    BlockState state = entity.level().getBlockState(pos);
                    return state.getFluidState().is(this.fluid) && state.getFluidState().isSource();
                });
        BehaviorPickFluid workTask = new BehaviorPickFluid(this.fluid, this.filledBucket);
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
