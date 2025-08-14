package customcafepatchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import customcafepatchmod.screens.PowerCardScreenBase;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicRelic;

@SpirePatch2(
        clz= PowerelicRelic.class,
        method= "atPreBattle",
        paramtypez={}
)
public class RelicPatch {
    @SpirePrefixPatch
    public static void Prefix(PowerelicRelic __instance) {
        System.out.println("Checking if null --------------------------------------------------");
        if (__instance.capturedCard == null) {
            System.out.println("null ---------------------------------------------- :)");
            PowerCardScreenBase.generateScreen(__instance).open();
        }
    }
}
