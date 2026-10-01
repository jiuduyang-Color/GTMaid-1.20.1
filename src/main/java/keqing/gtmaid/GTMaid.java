package keqing.gtmaid;

import com.mojang.logging.LogUtils;
import keqing.gtmaid.api.GMLog;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GTMaid.MODID)
public class GTMaid {
    public static final String MODID = "gtmaid";
    public static final String NAME = "GTMaid";
    public static final String VERSION = "1.0.2-1.20.1";

    public GTMaid() {
        GMLog.init(LogUtils.getLogger());

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onConfigLoading);
        modBus.addListener(this::onConfigReloading);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GMConfig.SPEC);
    }

    private void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == GMConfig.SPEC) {
            GMConfig.bake();
        }
    }

    private void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == GMConfig.SPEC) {
            GMConfig.bake();
        }
    }
}
