package PatchEverything.RunMods;

import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.localization.RunModStrings;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class BlasphemyRunMod extends AbstractDailyMod {
    public static final String ID = ("PatchEverything:" + BlasphemyRunMod.class.getSimpleName());
    private static final RunModStrings modStrings = languagePack.getRunModString(ID);
    public static final String NAME = modStrings.NAME;
    public static final String DESC = modStrings.DESCRIPTION;

    public BlasphemyRunMod() {
        super(ID, NAME, DESC, null, false);
    }
}
