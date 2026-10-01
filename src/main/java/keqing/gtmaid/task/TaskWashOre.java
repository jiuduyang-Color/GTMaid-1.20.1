package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorWashOre;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class TaskWashOre extends AbstractGTMaidTask {
    public TaskWashOre() {
        super("washore", new ItemStack(Items.WATER_BUCKET), machineSound(), 0.5f,
                "Wash crushed ore in a water cauldron");
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
                    return BehaviorWashOre.hasWater(serverLevel, pos);
                });
        BehaviorWashOre workTask = new BehaviorWashOre();
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
