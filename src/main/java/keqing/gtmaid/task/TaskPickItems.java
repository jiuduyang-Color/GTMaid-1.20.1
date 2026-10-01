package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorHaulItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class TaskPickItems extends AbstractGTMaidTask {
    public TaskPickItems() {
        super("pickitem", new ItemStack(Items.HOPPER), machineSound(), 0.5f,
                "Haul items produced by GregTech machines");
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
                    return BehaviorHaulItems.hasExportableItems(serverLevel, pos);
                });
        BehaviorHaulItems workTask = new BehaviorHaulItems();
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
