package customcafepatchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch2(cls= "BuxomMod.actions.OmegaRecallAction", method= "update", paramtypez = {}, requiredModId = "BuxomMod")
public class OmegaRecallFix {
    @SpireInsertPatch(rloc=6)
    public static void Insert(CardGroup ___nonStatusCardsDrawPile) {
        if (!___nonStatusCardsDrawPile.isEmpty()) {
            ___nonStatusCardsDrawPile.removeTopCard();
        }
    }
}
