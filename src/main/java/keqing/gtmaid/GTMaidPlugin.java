package keqing.gtmaid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import keqing.gtmaid.task.TaskAddWater;
import keqing.gtmaid.task.TaskBreakBlock;
import keqing.gtmaid.task.TaskBrokeTree;
import keqing.gtmaid.task.TaskDrillOre;
import keqing.gtmaid.task.TaskGTTool;
import keqing.gtmaid.task.TaskGenerator;
import keqing.gtmaid.task.TaskMaintenance;
import keqing.gtmaid.task.TaskPickFluid;
import keqing.gtmaid.task.TaskPickItems;
import keqing.gtmaid.task.TaskPlantTree;
import keqing.gtmaid.task.TaskWashOre;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

@LittleMaidExtension
public class GTMaidPlugin implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {

        manager.add(new TaskMaintenance());
        manager.add(new TaskBrokeTree());
        manager.add(new TaskBreakBlock("sand", Blocks.SAND, true, 0.4f, "Dig sand in the work area"));
        manager.add(new TaskBreakBlock("gravel", Blocks.GRAVEL, true, 0.4f, "Dig gravel in the work area"));
        manager.add(new TaskBreakBlock("dirt", Blocks.DIRT, true, 0.4f, "Dig dirt in the work area"));
        manager.add(new TaskBreakBlock("clay", Blocks.CLAY, true, 0.4f, "Dig clay in the work area"));
        manager.add(new TaskDrillOre());
        manager.add(new TaskWashOre());

        manager.add(new TaskPickFluid("pickwater", new ItemStack(Items.WATER_BUCKET),
                Fluids.WATER, Items.WATER_BUCKET, "Fill buckets with water"));
        manager.add(new TaskPickFluid("picklava", new ItemStack(Items.LAVA_BUCKET),
                Fluids.LAVA, Items.LAVA_BUCKET, "Fill buckets with lava"));
        manager.add(new TaskAddWater());

        manager.add(new TaskPickItems());
        manager.add(new TaskGenerator());

        manager.add(new TaskPlantTree());

        manager.add(TaskGTTool.hammer());
        manager.add(TaskGTTool.file());
        manager.add(TaskGTTool.mortar());
        manager.add(TaskGTTool.cutter());
        manager.add(TaskGTTool.saw());
    }
}
