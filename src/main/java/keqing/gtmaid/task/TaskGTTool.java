package keqing.gtmaid.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import keqing.gtmaid.entity.BehaviorGTToolWork;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class TaskGTTool extends AbstractGTMaidTask {
    private final GTToolType toolType;
    private final List<BehaviorGTToolWork.Transform> transforms;

    public TaskGTTool(String path, ItemStack icon, GTToolType toolType,
                      List<BehaviorGTToolWork.Transform> transforms, String actionSummary) {
        super(path, icon, machineSound(), 0.5f, actionSummary);
        this.toolType = toolType;
        this.transforms = transforms;
    }

    public static TaskGTTool hammer() {
        return new TaskGTTool("hammer", new ItemStack(Items.ANVIL), GTToolType.HARD_HAMMER,
                Lists.newArrayList(
                        new BehaviorGTToolWork.Transform(TagPrefix.rawOre, TagPrefix.crushed, 1, 1),
                        new BehaviorGTToolWork.Transform(TagPrefix.crushed, TagPrefix.dustImpure, 1, 1),
                        new BehaviorGTToolWork.Transform(TagPrefix.ingot, TagPrefix.plate, 3, 2),
                        new BehaviorGTToolWork.Transform(TagPrefix.rod, TagPrefix.rodLong, 2, 1)),
                "Shape ingots and rods with a hammer");
    }

    public static TaskGTTool file() {
        return new TaskGTTool("file", new ItemStack(Items.IRON_INGOT), GTToolType.FILE,
                Lists.newArrayList(
                        new BehaviorGTToolWork.Transform(TagPrefix.ingot, TagPrefix.rod, 1, 1),
                        new BehaviorGTToolWork.Transform(TagPrefix.bolt, TagPrefix.screw, 1, 1)),
                "Process items with a file");
    }

    public static TaskGTTool saw() {
        return new TaskGTTool("saw", new ItemStack(Items.IRON_AXE), GTToolType.SAW,
                Lists.newArrayList(
                        new BehaviorGTToolWork.Transform(TagPrefix.rodLong, TagPrefix.rod, 1, 2),
                        new BehaviorGTToolWork.Transform(TagPrefix.rod, TagPrefix.bolt, 1, 2)),
                "Process items with a saw");
    }

    public static TaskGTTool cutter() {
        return new TaskGTTool("cutter", new ItemStack(Items.SHEARS), GTToolType.WIRE_CUTTER,
                Lists.newArrayList(
                        new BehaviorGTToolWork.Transform(TagPrefix.plate, TagPrefix.wireGtSingle, 1, 2),
                        new BehaviorGTToolWork.Transform(TagPrefix.screw, TagPrefix.bolt, 1, 1),
                        new BehaviorGTToolWork.Transform(TagPrefix.rod, TagPrefix.bolt, 1, 4)),
                "Process items with wire cutters");
    }

    public static TaskGTTool mortar() {
        return new TaskGTTool("mortar", new ItemStack(Items.BOWL), GTToolType.MORTAR,
                Lists.newArrayList(
                        new BehaviorGTToolWork.Transform(TagPrefix.ingot, TagPrefix.dust, 1, 1)),
                "Grind items with a mortar");
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return Lists.newArrayList(Pair.of(6, new BehaviorGTToolWork(this.toolType, this.transforms)));
    }
}
