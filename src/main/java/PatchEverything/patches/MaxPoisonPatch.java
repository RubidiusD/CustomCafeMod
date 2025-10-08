package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MaxPoisonPatch {
    @SpirePatch2(clz= PoisonPower.class, method= SpirePatch.CLASS)
    public static class Excess {
        public static SpireField<Integer> excess = new SpireField<>(() -> 0);
    }

    @SpirePatch2(clz= PoisonPower.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, AbstractCreature.class, int.class})
    public static class applyMorePatch {
        @SpirePostfixPatch public static void Postfix(AbstractPower __instance, int poisonAmt) {
            __instance.amount = poisonAmt;
            __instance.updateDescription();
        }
    }

    @SpirePatch2(clz = PoisonPower.class, method= "stackPower", paramtypez = {int.class})
    public static class preventOverflowPatch {
        @SpireInsertPatch(rloc= 1) public static void Insert(AbstractPower __instance, int stackAmount) {
            while (__instance.amount >= 1000000000) {
                Excess.excess.set(__instance, Excess.excess.get(__instance) + 1);
                __instance.amount -= 1000000000;
            }
        }
    }

    public static String addCommas(String value) {
        int l = value.length();
        String result = "";
        while (l > 3) {
            result = ",".concat(value.substring(l - 3) + result);
            value = value.substring(0, l - 3);
            l -= 3;
        }
        result = value + result;

        return result;
    }

    public static String getAmount(AbstractPower power) {
        String temp = "";
        if (Excess.excess.get(power) != 0) {
            temp += Excess.excess.get(power).toString() + ("" + (power.amount + 1000000000)).substring(1);
        } else {
            temp += power.amount;
        }

        return addCommas(temp);
    }

    @SpirePatch2(clz= PoisonPower.class, method= "updateDescription", paramtypez = {})
    public static class displayOverflowPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                int appends = 0;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    super.edit(m);

                    if (m.getMethodName().equals("append")) {
                        appends += 1;
                        if (appends == 2 || appends == 5) {
                            m.replace("$_ = $proceed(PatchEverything.patches.MaxPoisonPatch.getAmount(this));");
                        }
                    }
                }
            };
        }
    }
}
