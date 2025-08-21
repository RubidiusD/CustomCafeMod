package customcafepatchmod.patches;

import CustomStart.CustomRunMods.CustomDeckScreenBase;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class CustomDeckPatches {
    @SpirePatch2(clz= CustomDeckScreenBase.CustomDeckScreen.class, method= "open", paramtypez = {})
    public static class OrderingPatch {
        @SpireInsertPatch(rloc= 5)
        public static void Insert(ArrayList<AbstractCard.CardColor> ___colorList) {
            ___colorList.remove(AbstractCard.CardColor.BLUE);
            ___colorList.add(AbstractCard.CardColor.BLUE);
        }
    }

    @SpirePatch2(clz= CustomDeckScreenBase.CustomDeckScreen.class, method= "InitCardList", paramtypez = {AbstractCard.CardColor.class})
    public static class IncludeStatusCardsPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean second = false;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("removeIf")) {
                        if (!second) {
                            second = true;
                        } else {
                            m.replace("$_ = $proceed((abstractCard) -> abstractCard.color == com.megacrit.cardcrawl.cards.AbstractCard.CardColor.CURSE || (!customcafepatchmod.util.CustomCafeConfig.includeStatusCards && abstractCard.type == com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS));");
                            second = false;
                        }
                    }
                }
            };
        }
    }
}
