package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;


public class OmegaRecallPreAction extends AbstractGameAction {
    public OmegaRecallPreAction(int amount) {
        this.amount = amount;
    }

    @Override public void update() {
        System.out.println("Omega Recall isn't set up properly you muppet.");
        this.isDone = true;
    }

    // call the pre-action instead
    @SpirePatch2(cls= "BuxomMod.powers.OmegaRecallPower", method= "atStartOfTurnPostDraw", paramtypez = {}, requiredModId = "BuxomMod")
    public static class OmegaRecallPowerFix {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("addToBot")) {
                        m.replace("$_ = null; com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new PatchEverything.patches.OmegaRecallPreAction(this.amount));");
                    }
                }
            };
        }
    }

    // the code of the pre-action (which, yes, calls the action.)
    @SpirePatch2(clz = OmegaRecallPreAction.class, method= "update", paramtypez = {}, requiredModId = "BuxomMod")
    public static class OmegaRecallPreActionContent {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    m.replace("com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToTop(new BuxomMod.actions.OmegaRecallAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, this.amount));" +
                            "if (BuxomMod.BuxomMod.specialGetCardsOfType(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.drawPile, com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS, true).size() < this.amount) {" +
                            "   com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToTop(new com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction());" +
                            "}" +
                            "$_ = $proceed($$);"
                    );
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.actions.OmegaRecallAction", method= "update", paramtypez = {}, requiredModId = "BuxomMod")
    public static class OmegaRecallActionFix {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("moveToHand")) {
                        m.replace("$_ = $proceed($$); nonStatusCardsDrawPile.removeTopCard();");
                    }
                }
            };
        }
    }
}
