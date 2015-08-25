package org.walterweight.sunburn;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.walterweight.sunburn.proxies.Common;


@Mod(modid = SunBurn.MODID, name=SunBurn.NAME, version = SunBurn.VERSION)
public class SunBurn
{
    public static final String NAME = "SunBurn";
    public static final String MODID = "sunburn";
    public static final String VERSION = "0.1.1";
    public PlayerEventHandler playerEventHandler = new PlayerEventHandler();
    public static int chanceOfCatchingFirePerTick;
    public static float sunLightRequiredToCatchFire;
    public static int amountOfDamage;
    public static boolean helmetProtectsAgainstSun;
    public static boolean helmetRequiresFireProtection;
    public static int chanceOfHelmetTakingDamagePerTick;

    @Instance("sunburn")
    public static SunBurn instance;

    @SidedProxy(clientSide="org.walterweight.sunburn.proxies.Client", serverSide="org.walterweighjt.suburn.proxies.Common")
    public static Common proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        chanceOfCatchingFirePerTick = config.getInt("chanceOfCatchingFirePerTick", Configuration.CATEGORY_GENERAL,
                1, 0, 100,
                "The likelihood of catching fire per tick as a percentage");
        sunLightRequiredToCatchFire = config.getFloat("sunLightRequiredToCatchFire", Configuration.CATEGORY_GENERAL,
                0.5F, 0F, 1F,
                "Sun light level required before player catches fire");
        amountOfDamage = config.getInt("amountOfDamage", Configuration.CATEGORY_GENERAL,
                8, 0, 100,
                "The amount of damage (in hearts) that is inflicted on a player when they start to burn. Note in vanilla, a player has 20 hearts when in full health");
        helmetProtectsAgainstSun = config.getBoolean("helmetProtectsAgainstSun", Configuration.CATEGORY_GENERAL,
                true, "If true, a helmet will protect the player from the sun");
        helmetRequiresFireProtection = config.getBoolean("helmetRequiresFireProtection", Configuration.CATEGORY_GENERAL,
                true, "If true, a helmet must have the fire protection enchantment to be effective");
        chanceOfHelmetTakingDamagePerTick = config.getInt("chanceOfHelmetTakingDamagePerTick", Configuration.CATEGORY_GENERAL,
                25, 0, 100,
                "The likelihood of a helmet taking damage per tick as a percentage");
        config.save();
    }


    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
    }

}
