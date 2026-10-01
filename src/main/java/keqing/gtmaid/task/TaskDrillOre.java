package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import keqing.gtmaid.entity.BehaviorMineOre;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolActions;

import java.util.List;

public class TaskDrillOre extends AbstractGTMaidTask {
    public TaskDrillOre() {
        super("ore", new ItemStack(Items.GOLDEN_PICKAXE), breakSound(), 0.1f,
                "Mine GregTech ores and produce raw ore");
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                0.6f,
                4,
                entity -> entity.getMainHandItem().canPerformAction(ToolActions.PICKAXE_DIG),
                (entity, pos) -> {
                    var entry = ChemicalHelper.getMaterialEntry(entity.level().getBlockState(pos).getBlock());
                    return !entry.isEmpty() && TagPrefix.ORES.containsKey(entry.tagPrefix());
                });
        BehaviorMineOre workTask = new BehaviorMineOre(true);
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }
}
