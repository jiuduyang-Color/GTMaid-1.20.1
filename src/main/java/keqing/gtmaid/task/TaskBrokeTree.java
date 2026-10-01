package keqing.gtmaid.task;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class TaskBrokeTree extends TaskBreakBlock {
    public TaskBrokeTree() {
        super("log", new ItemStack(Blocks.OAK_LOG), state -> state.is(BlockTags.LOGS), false, 0.4f,
                "Chop down trees and collect logs");
    }
}
