package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorEnergize;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class TaskGenerator extends AbstractGTMaidTask {
    public TaskGenerator() {
        super("generator", new ItemStack(Items.REDSTONE_BLOCK), machineSound(), 0.5f,
                "Charge GregTech machines with the maid's experience");
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                0.6f,
                4,
                entity -> true,
                (entity, pos) -> {
                    if (!(entity.level() instanceof ServerLevel serverLevel)) {
                        return false;
                    }
                    return BehaviorEnergize.needsEnergy(serverLevel, pos);
                });
        BehaviorEnergize workTask = new BehaviorEnergize();
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
