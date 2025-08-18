package customcafepatchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class OmegaRecallFixes {
    @SpirePatch2(cls= "BuxomMod.actions.OmegaRecallAction", method= "update", paramtypez = {}, requiredModId = "BuxomMod")
    public static class OmegaRecallActionFix {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("moveToHand")) {
                        m.replace("$_ = $proceed($$); nonStatusCardsDrawPile.removeTopCard();");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.powers.OmegaRecallPower", method= "atStartOfTurnPostDraw", paramtypez = {}, requiredModId = "BuxomMod")
    public static class OmegaRecallPowerFix {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("addToBot")) {
                        m.replace("$_ = $proceed($$);" +
                            "if (BuxomMod.BuxomMod.specialGetCardsOfType(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.drawPile, com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS, true).size() < this.amount) {" +
                            "   com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToTop(new com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction());" +
                            "}");
                    }
                }
            };
        }
    }
}
