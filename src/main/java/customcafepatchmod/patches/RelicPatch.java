package customcafepatchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import customcafepatchmod.PowerCardScreenBase;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicRelic;

@SpirePatch2(
        clz= PowerelicRelic.class,
        method= "onEnterRoom"
)
public class RelicPatch {
    @SpirePrefixPatch
    public static void Prefix(PowerelicRelic __instance) {
        if (__instance.capturedCard == null) {
            PowerCardScreenBase.generateScreen(__instance).open();
        }
    }
}
