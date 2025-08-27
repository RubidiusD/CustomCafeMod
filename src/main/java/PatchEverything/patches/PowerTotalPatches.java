package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.RegenPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

@SpirePatches2({
        @SpirePatch2(clz= PoisonPower.class, method= "updateDescription", paramtypez={}),
        @SpirePatch2(clz= RegenPower.class, method= "updateDescription", paramtypez={})
})
public class PowerTotalPatches {
    public static void Postfix(AbstractPower __instance) {
        if (__instance.owner.isPlayer || ! (__instance instanceof RegenPower)) {
            __instance.description = __instance.description + languagePack.getUIString("PatchEverything:PowerTotalPatches").TEXT[0] + (__instance.amount * (__instance.amount + 1)) / 2 + ".";
        }
    }
}
