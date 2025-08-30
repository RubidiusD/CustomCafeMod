package PatchEverything.patches.faker;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class ManaTransferAction extends AbstractGameAction {
    public ManaTransferAction(AbstractPlayer owner) {this.source = owner;}

    @Override
    public void update() {
        int n = 0;
        AbstractPlayer p = (AbstractPlayer)this.source;
        for (int i = 0; i < AbstractDungeon.player.orbs.size(); i++) {
            if (AbstractDungeon.player.orbs.get(i).ID.equals("FakerMod:AP")) {
                addToTop(new EvokeSpecificOrbAction(AbstractDungeon.player.orbs.get(i)));
                n ++;
            }
        }
        System.out.println("Found " + n + " AP orbs.");
//        addToTop(new IncreaseMaxOrbAction(n));
        p.increaseMaxOrbSlots(n, true);

        isDone = true;
    }

    @SpirePatch2(cls= "fakermod.cards.saber.ManaTransfer", method= "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class}, requiredModId = "FakerMod")
    public static class ManaTransferFix {
        public static void Replace(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            AbstractDungeon.actionManager.addToBottom(new ManaTransferAction(p));
        }
    }
}
