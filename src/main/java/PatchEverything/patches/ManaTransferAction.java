package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;


public class ManaTransferAction extends AbstractGameAction {
    public ManaTransferAction(AbstractPlayer owner) {this.source = owner;}

    @Override
    public void update() {
        int i = 0;
        AbstractPlayer p = (AbstractPlayer)this.source;
        for (AbstractOrb o : p.orbs) {
            if (o.ID.equals("FakerMod:AP")) {
                i ++;
            }
        }
        p.orbs.removeIf((o1) -> o1.ID.equals("FakerMod:AP"));
        System.out.println("Found " + i + " AP orbs.");
        addToTop(new IncreaseMaxOrbAction(i));

        isDone = true;
    }

    @SpirePatch2(cls= "fakermod.cards.saber.ManaTransfer", method= "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class}, requiredModId = "FakerMod")
    public static class ManaTransferFix {
        public static void Replace(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            AbstractDungeon.actionManager.addToBottom(new ManaTransferAction(p));
        }
    }

//    @SpirePatch(cls= "fakermod.cards.saber.ManaTransfer", method= "use", requiredModId = "FakerMod")
//    public static class ManaTransferFix {
//        @SpireInstrumentPatch()
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                boolean first = true;
//                @Override
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (first) {
//                        m.replace("$_ = null; com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new PatchEverything.patches.ManaTransferAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player));");
//                        first = false;
//                    }
//                    else {
//                        m.replace("$_ = null;");
//                    }
//                }
//            };
//        }
//    }
}
