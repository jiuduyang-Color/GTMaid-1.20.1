package keqing.gtmaid.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import keqing.gtmaid.GMConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

public class BehaviorMineOre extends AbstractWorkAtTargetBehavior {
    private final boolean chainAllowed;

    public BehaviorMineOre(boolean chainAllowed) {
        super(2.0);
        this.chainAllowed = chainAllowed;
    }

    @Override
    protected boolean work(ServerLevel level, EntityMaid maid, BlockPos center) {
        if (this.chainAllowed && GMConfig.chainSwitch) {
            int range = GMConfig.chainRange;
            boolean minedAny = false;
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        if (this.mineOne(level, maid, center.offset(x, y, z))) {
                            minedAny = true;
                        }
                    }
                }
            }
            return minedAny;
        }
        return this.mineOne(level, maid, center);
    }

    private boolean mineOne(ServerLevel level, EntityMaid maid, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        MaterialEntry entry = ChemicalHelper.getMaterialEntry(state.getBlock());

        if (entry.isEmpty() || !TagPrefix.ORES.containsKey(entry.tagPrefix())) {
            return false;
        }
        ItemStack rawOre = ChemicalHelper.get(TagPrefix.rawOre, entry.material());
        if (rawOre.isEmpty()) {
            return false;
        }
        ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(false), rawOre.copy(), false);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        maid.getMainHandItem().hurtAndBreak(1, maid, entity -> {
        });
        maid.swing(InteractionHand.MAIN_HAND);
        return true;
    }
}
