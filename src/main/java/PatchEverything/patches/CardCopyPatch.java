package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch2(
        clz= AbstractCard.class,
        method= "makeStatEquivalentCopy",
        paramtypez={}
)
public class CardCopyPatch {
    @SpirePostfixPatch
    public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance) {
        __result.exhaust = __instance.exhaust;
        __result.exhaustOnUseOnce = __instance.exhaustOnUseOnce;
        __result.exhaustOnFire = __instance.exhaustOnFire;
        __result.retain = __instance.retain;

        return __result;
    }
}
