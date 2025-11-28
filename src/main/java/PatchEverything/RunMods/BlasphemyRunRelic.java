package PatchEverything.RunMods;

import PatchEverything.implementation.BaseRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.DrawPower;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.actNum;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class BlasphemyRunRelic extends BaseRelic {
    private static final String NAME = BlasphemyRunRelic.class.getSimpleName();
    public static final String ID = ("PatchEverything:" + NAME);
    private static final RelicTier RARITY = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public BlasphemyRunRelic()  {
        super(ID, NAME, AbstractCard.CardColor.COLORLESS, RARITY, SOUND);
    }

    @Override public void atBattleStart() {
        this.addToBot(new ChangeStanceAction(DivinityStance.STANCE_ID));
        this.addToBot(new ApplyPowerAction(player, player, new EndTurnDeathPower(player)));
    }

    @Override public void atBattleStartPreDraw() {
        for (int index = player.drawPile.size() - 1; index != -1; index --) {
            if (player.drawPile.group.get(index).hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                addToTop(new ShowCardAndPoofAction(player.drawPile.group.get(index)));
                addToTop(new ExhaustSpecificCardAction(player.drawPile.group.get(index), player.drawPile, true));
            }
        }
        setCounter(actNum - 1);
        if (counter != 0) {
            this.addToBot(new ApplyPowerAction(player, player, new DrawPower(player, counter)));
        }
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
            addToTop(new DrawCardAction(1));
            addToTop(new ExhaustSpecificCardAction(drawnCard, player.hand, true));
        }
    }

    @Override public void onChestOpenAfter(boolean bossChest) {
        if (bossChest)
            setCounter(actNum);
    }

    @Override public void onObtainCard(AbstractCard c) {
        if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
            addToBot(new AbstractGameAction() {
                @Override public void update() {
                    player.masterDeck.removeCard(c);
                }
            });
        }
    }

    @Override public void onEquip() {
        UnlockTracker.markRelicAsSeen(ID);
    }

    @Override public String getUpdatedDescription() { return DESCRIPTIONS[0]; }
}
