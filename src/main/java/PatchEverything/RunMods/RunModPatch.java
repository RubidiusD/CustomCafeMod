package PatchEverything.RunMods;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.neow.NeowEvent;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@SpirePatch(clz = NeowEvent.class, method = "dailyBlessing")
public class RunModPatch {
    @SpirePostfixPatch public static void Postfix(NeowEvent __instance) {
        if (CardCrawlGame.trial.dailyModIDs().contains(BlasphemyRunMod.ID)) {
            player.relics.add(new BlasphemyRunRelic());
            player.reorganizeRelics();
        }
        if (CardCrawlGame.trial.dailyModIDs().contains(CreativeAIRunMod.ID)) {
            player.relics.add(new CreativeAIRelic());
            player.reorganizeRelics();
        }
    }
}
