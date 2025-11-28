package PatchEverything.RunMods;

import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.localization.RunModStrings;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class CreativeAIRunMod extends AbstractDailyMod {
    public static final String ID = ("PatchEverything:" + CreativeAIRunMod.class.getSimpleName());
    private static final RunModStrings modStrings = languagePack.getRunModString(ID);
    public static final String NAME = modStrings.NAME;
    public static final String DESC = modStrings.DESCRIPTION;

    public CreativeAIRunMod() {
        super(ID, NAME, DESC, null, false);
    }
}
