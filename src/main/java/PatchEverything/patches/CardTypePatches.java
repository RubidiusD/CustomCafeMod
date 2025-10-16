package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class CardTypePatches {
    @SpirePatch2(cls= "tisCardPack.cards.colorless.WeakPoint", method= SpirePatch.STATICINITIALIZER, requiredModId= "tisCardPack")
    public static class WeakPointsRarityPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("COMMON")) {
                        f.replace("$_ = com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.cards.ToplessStatus", method= SpirePatch.STATICINITIALIZER, requiredModId= "BuxomMod")
    public static class ToplessPowerPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("SKILL")) {
                        f.replace("$_ = com.megacrit.cardcrawl.cards.AbstractCard.CardType.POWER;");
                    }
                }
            };
        }
    }
}
