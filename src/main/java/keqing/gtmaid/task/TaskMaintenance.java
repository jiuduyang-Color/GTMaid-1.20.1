package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.gregtechceu.gtceu.common.data.GTItems;
import keqing.gtmaid.entity.BehaviorMaintenance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TaskMaintenance extends AbstractGTMaidTask {
    public TaskMaintenance() {
        super("maintenance", new ItemStack(GTItems.DUCT_TAPE.get()), machineSound(), 0.5f,
                "Tape up GregTech maintenance hatches");
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                0.6f,
                4,
                entity -> !ItemsUtil.getStack(entity.getAvailableInv(false),
                        stack -> stack.is(GTItems.DUCT_TAPE.get())).isEmpty(),
                (entity, pos) -> {
                    if (!(entity.level() instanceof ServerLevel serverLevel)) {
                        return false;
                    }
                    return BehaviorMaintenance.needsTaping(serverLevel, pos);
                });
        BehaviorMaintenance workTask = new BehaviorMaintenance();
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
