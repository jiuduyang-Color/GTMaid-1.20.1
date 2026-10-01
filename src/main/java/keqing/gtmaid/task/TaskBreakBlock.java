package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import keqing.gtmaid.entity.BehaviorBreakBlock;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;

import java.util.List;
import java.util.function.Predicate;

public class TaskBreakBlock extends AbstractGTMaidTask {
    protected final Predicate<BlockState> targetState;
    protected final boolean shovelTool;
    protected final float moveSpeed;

    public TaskBreakBlock(String path, ItemStack icon, Predicate<BlockState> targetState,
                          boolean shovelTool, float soundVolume, String actionSummary) {
        super(path, icon, farmSound(), soundVolume, actionSummary);
        this.targetState = targetState;
        this.shovelTool = shovelTool;
        this.moveSpeed = 0.6f;
    }

    public TaskBreakBlock(String path, Block block, boolean shovelTool, float soundVolume, String actionSummary) {
        this(path, new ItemStack(block), state -> state.getBlock() == block, shovelTool, soundVolume, actionSummary);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidMoveToPredicateBlockTask moveTask = new MaidMoveToPredicateBlockTask(
                this.moveSpeed,
                4,
                entity -> hasCorrectTool(entity, this.shovelTool),
                (entity, pos) -> this.targetState.test(entity.level().getBlockState(pos)));
        BehaviorBreakBlock workTask = new BehaviorBreakBlock(this.targetState, 2.0, true);
        return Lists.newArrayList(Pair.of(5, moveTask), Pair.of(6, workTask));
    }

    protected static boolean hasCorrectTool(EntityMaid maid, boolean shovelTool) {
        ItemStack stack = maid.getMainHandItem();
        if (stack.isEmpty()) {
            return false;
        }
        return shovelTool
                ? stack.canPerformAction(ToolActions.SHOVEL_DIG)
                : stack.canPerformAction(ToolActions.AXE_DIG);
    }
}
