package PatchEverything.patches;

import PatchEverything.util.ExprViewer;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import javassist.CannotCompileException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;

@SpirePatch2(cls= "tisCardPack.powers.ScrapperPower", method= "onAllyManuallyDiscarded", paramtypes = {"spireTogether.network.P2P.P2PPlayer"}, requiredModId = "tisCardPack")
public class ScrapperHandLimitPatch {
    @SpireInstrumentPatch public static ExprEditor Instrument() {
        return new ExprViewer("Scrapper Hand Limit Patch") {
            @Override public void edit(ConstructorCall c) throws CannotCompileException {
                super.edit(c);

                if (c.getMethodName().equals("VFXAction")) {
                    c.replace("$_ = com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction(shivToAdd);");
                }
            }
        };
    }
}
