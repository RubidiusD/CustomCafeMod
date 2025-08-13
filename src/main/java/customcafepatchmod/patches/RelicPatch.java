package customcafepatchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import customcafepatchmod.screens.PowerCardScreenBase;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicRelic;

@SpirePatch2(
        clz= PowerelicRelic.class,
        method= "<class>",
        paramtypez={AbstractRoom.class}
)
public class RelicPatch {
    public static void OnEnterRoom(PowerelicRelic __instance) {
        if (__instance.capturedCard == null) {
            PowerCardScreenBase.generateScreen(__instance).open();
        }
    }
}
