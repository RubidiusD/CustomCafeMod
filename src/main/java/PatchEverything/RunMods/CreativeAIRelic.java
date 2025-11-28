package PatchEverything.RunMods;

import PatchEverything.implementation.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.CreativeAIPower;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static com.megacrit.cardcrawl.unlock.UnlockTracker.markRelicAsSeen;

public class CreativeAIRelic extends BaseRelic {
    private static final String NAME = CreativeAIRelic.class.getSimpleName();
    public static final String ID = ("PatchEverything:" + NAME);
    private static final RelicTier RARITY = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public CreativeAIRelic() {
        super(ID, NAME, AbstractCard.CardColor.COLORLESS, RARITY, SOUND);
    }

    @Override public void atBattleStartPreDraw() {
        CreativeAIPower p = new CreativeAIPower(player, 1);
        addToTop(new ApplyPowerAction(player, player, p));
        p.atStartOfTurn();
        --player.gameHandSize;
    }

    @Override public void onEquip() {
        markRelicAsSeen(ID);
    }

    @Override public String getUpdatedDescription() { return DESCRIPTIONS[0]; }
}
