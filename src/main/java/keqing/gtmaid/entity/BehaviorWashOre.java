package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BehaviorWashOre extends AbstractWorkAtTargetBehavior {
    public BehaviorWashOre() {
        super(2.0);
    }

    public static boolean hasWater(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0;
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.WATER_CAULDRON)) {
            return false;
        }
        int waterLevel = state.getValue(LayeredCauldronBlock.LEVEL);
        if (waterLevel <= 0) {
            return false;
        }
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            MaterialEntry entry = ChemicalHelper.getMaterialEntry(stack.getItem());
            if (entry.isEmpty() || entry.tagPrefix() == null) {
                continue;
            }
            TagPrefix output = washedPrefix(entry.tagPrefix());
            if (output == null) {
                continue;
            }
            int amount = stack.getCount();
            ItemStack production = ChemicalHelper.get(output, entry.material(), amount);
            if (production.isEmpty()) {
                continue;
            }
            inv.extractItem(slot, amount, false);
            inv.insertItem(slot, production, false);
            level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, waterLevel - 1), Block.UPDATE_ALL);
            maid.swing(InteractionHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    private static TagPrefix washedPrefix(TagPrefix input) {
        if (input == TagPrefix.crushed) {
            return TagPrefix.crushedPurified;
        }
        if (input == TagPrefix.dustImpure || input == TagPrefix.dustPure) {
            return TagPrefix.dust;
        }
        return null;
    }
}
