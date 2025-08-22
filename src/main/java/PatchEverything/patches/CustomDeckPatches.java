package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

import static PatchEverything.util.EverythingPatchConfig.includeStatusCards;

public class CustomDeckPatches {
    @SpirePatch2(cls= "CustomStart.CustomRunMods.CustomDeckScreenBase$CustomDeckScreen", method= "open", paramtypez = {}, requiredModId = "CustomStart")
    public static class OrderingPatch {
        @SpireInsertPatch(rloc= 5)
        public static void Insert(ArrayList<AbstractCard.CardColor> ___colorList) {
            ___colorList.remove(AbstractCard.CardColor.BLUE);
            ___colorList.add(AbstractCard.CardColor.BLUE);
        }
    }

    public static void CleanseCardList(ArrayList<AbstractCard> cardList) {
        cardList.removeIf((abstractCard) -> abstractCard.color == AbstractCard.CardColor.CURSE || (!includeStatusCards && abstractCard.type == AbstractCard.CardType.STATUS));
    }

    @SpirePatch2(cls= "CustomStart.CustomRunMods.CustomDeckScreenBase$CustomDeckScreen", method= "InitCardList", paramtypez = {AbstractCard.CardColor.class}, requiredModId = "CustomStart")
    public static class IncludeStatusCardsPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                int line = 0;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("removeIf")) {
                        line ++;
                        if (line == 2) {
                            m.replace("$_ = PatchEverything.patches.CustomDeckPatches.CleanseCardList(cardList);");
                        }
                    }
                }
            };
        }
    }
}
