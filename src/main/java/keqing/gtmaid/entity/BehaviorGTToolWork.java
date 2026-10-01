package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class BehaviorGTToolWork extends Behavior<EntityMaid> {

    public record Transform(TagPrefix input, TagPrefix output, int inputCount, int outputCount) {
    }

    private static final int WORK_COOLDOWN_TICKS = 20;

    private final GTToolType toolType;
    private final List<Transform> transforms;
    private long lastWorkTick = 0L;

    public BehaviorGTToolWork(GTToolType toolType, List<Transform> transforms) {
        super(ImmutableMap.of());
        this.toolType = toolType;
        this.transforms = transforms;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        if (maid.tickCount - this.lastWorkTick < WORK_COOLDOWN_TICKS) {
            return false;
        }
        ItemStack held = maid.getMainHandItem();
        return ToolHelper.isTool(held, this.toolType);
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        this.lastWorkTick = maid.tickCount;
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            TagPrefix prefix = null;
            Material material = null;

            MaterialEntry entry = ChemicalHelper.getMaterialEntry(stack.getItem());
            if (!entry.isEmpty() && !entry.tagPrefix().isEmpty()) {
                prefix = entry.tagPrefix();
                material = entry.material();
            } else {
                MaterialStack stackEntry = ChemicalHelper.getMaterialStack(stack);
                TagPrefix byItem = ChemicalHelper.getPrefix(stack.getItem());
                if (stackEntry != null && byItem != null && !byItem.isEmpty()) {
                    prefix = byItem;
                    material = stackEntry.material();
                }
            }
            if (prefix == null || material == null) {
                continue;
            }
            for (Transform transform : this.transforms) {
                if (prefix != transform.input() || stack.getCount() < transform.inputCount()) {
                    continue;
                }
                ItemStack production = ChemicalHelper.get(transform.output(), material,
                        transform.outputCount());
                if (production.isEmpty()) {
                    continue;
                }
                inv.extractItem(slot, transform.inputCount(), false);
                ItemHandlerHelper.insertItemStacked(inv, production, false);
                maid.getMainHandItem().hurtAndBreak(1, maid, entity -> {
                });
                maid.swing(InteractionHand.MAIN_HAND);
                return;
            }
        }
    }
}
