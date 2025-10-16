package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatches2({
    @SpirePatch2(cls= "tisCardPack.cards.colorless.WeakPoint", method= SpirePatch.CONSTRUCTOR, paramtypez = {}, requiredModId = "tisCardPack"),
    @SpirePatch2(cls= "tisCardPack.cards.colorless.WeakPoint", method= "upgrade", paramtypez = {}, requiredModId = "tisCardPack")
})
public class WeakPointsSaysExhaust {
    @SpireInsertPatch(rloc= 4) public static void Insert(AbstractCard __instance) {
        if (Loader.isModLoaded("ExhaustPoof")) {
            __instance.rawDescription += " NL Poof.";
        } else {
            __instance.rawDescription += " NL Exhaust.";
        }
        __instance.initializeDescription();
    }
}
