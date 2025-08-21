package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static PatchEverything.util.EverythingPatchConfig.includeEventRelics;

@SpirePatch2(cls= "CustomStart.CustomRunMods.Relicselectscreen", method= "open", paramtypez = {}, requiredModId = "CustomStart")
public class RelicStartPatch {
    @SpireInsertPatch(rloc=9)
    public static void Insert(ArrayList<AbstractRelic> ___relics) {
        if (includeEventRelics) {
            ___relics.addAll(RelicLibrary.specialList);
        }
        ___relics.removeIf(o1 -> AbstractDungeon.player.hasRelic(o1.relicId));
    }
}
