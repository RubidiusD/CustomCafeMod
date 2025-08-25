package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.RegenPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class PowerTotalPatches {

    @SpirePatch(clz= PoisonPower.class, method= "updateDescription", paramtypez={})
    public static class PoisonTotalPatch {
        public static void Postfix(PoisonPower power) {
            power.description = power.description + languagePack.getUIString("PatchEverything:PowerTotalPatches").TEXT[0] + (power.amount * (power.amount + 1)) / 2 + ".";
        }
    }

    @SpirePatch(clz= RegenPower.class, method= "updateDescription", paramtypez={})
    public static class RegenTotalPatch {
        public static void Postfix(RegenPower power) {
            if (power.owner.isPlayer) {
                power.description = power.description + languagePack.getUIString("PatchEverything:PowerTotalPatches").TEXT[0] + (power.amount * (power.amount + 1)) / 2 + ".";
            }
        }
    }
}
