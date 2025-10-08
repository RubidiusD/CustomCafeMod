package PatchEverything.implementation;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class Poof extends BaseRelic {
    private static final String NAME = Poof.class.getSimpleName();
    public static final String ID = ("PatchEverything:" + NAME);
    private static final RelicTier RARITY = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.HEAVY;

    public Poof()  {
        super(ID, NAME, AbstractCard.CardColor.COLORLESS, RARITY, SOUND);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        addToBot(new TalkAction(true, this.name, 0.5F, 0.75F));
    }

    @Override
    public void onEquip() {
        UnlockTracker.markRelicAsSeen(ID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
