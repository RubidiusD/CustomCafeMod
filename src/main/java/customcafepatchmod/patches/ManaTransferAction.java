package customcafepatchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class ManaTransferAction extends AbstractGameAction {
    public ManaTransferAction(AbstractPlayer owner) {this.source = owner;}

    @Override
    public void update() {
        int i = 0;
        AbstractPlayer p = (AbstractPlayer)this.source;
        while (p.hasOrb()) {
            p.removeNextOrb();
            i ++;
        }
        p.increaseMaxOrbSlots(i, true);
        addToTop(new IncreaseMaxOrbAction(i));

        isDone = true;
    }

    @SpirePatch(cls= "fakermod.cards.saber.ManaTransfer", method= "use", requiredModId = "FakerMod")
    public static class ManaTransferFix {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            AbstractDungeon.actionManager.addToBottom(new ManaTransferAction(AbstractDungeon.player));
            return SpireReturn.Return();
        }
    }
}
