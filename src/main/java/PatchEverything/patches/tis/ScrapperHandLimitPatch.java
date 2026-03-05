package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import javassist.CannotCompileException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;

@SpirePatch2(cls= "tisCardPack.powers.ScrapperPower", method= "onAllyManuallyDiscarded", paramtypes = {"spireTogether.network.P2P.P2PPlayer"}, requiredModId = "tisCardPack")
public class ScrapperHandLimitPatch {
    @SpireInstrumentPatch public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override public void edit(ConstructorCall c) throws CannotCompileException {
                if (c.getMethodName().equals("VFXAction")) {
                    c.replace("$_ = com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction(shivToAdd);");
                }
            }
        };
    }
}
