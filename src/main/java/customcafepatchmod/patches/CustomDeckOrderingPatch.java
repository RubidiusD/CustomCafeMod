package customcafepatchmod.patches;

import CustomStart.CustomRunMods.CustomDeckScreenBase;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

@SpirePatch2(clz= CustomDeckScreenBase.CustomDeckScreen.class, method= "open", paramtypez = {})
public class CustomDeckOrderingPatch {
    @SpireInsertPatch(rloc= 5)
    public static void Insert(ArrayList<AbstractCard.CardColor> ___colorList) {
        ___colorList.remove(AbstractCard.CardColor.BLUE);
        ___colorList.add(AbstractCard.CardColor.BLUE);
    }
}
