package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch2(clz= ApplyPowerAction.class, method= "update", paramtypez = {})
public class ArtifactNegativeBuffPatch {
    @SpireInstrumentPatch public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override public void edit(FieldAccess f) throws CannotCompileException {
                if (f.getFieldName().equals("DEBUFF")) {
                    f.replace("if ((powerToApply.canGoNegative) && (powerToApply.amount < 0)) {$_ = com.megacrit.cardcrawl.powers.AbstractPower.PowerType.BUFF;} else {$_ = com.megacrit.cardcrawl.powers.AbstractPower.PowerType.DEBUFF;}");
                }
            }
        };
    }
}
