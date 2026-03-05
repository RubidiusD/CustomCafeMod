package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class UnEndTurnPatches {
    @SpirePatch2(clz=ShowCardAndAddToHandEffect.class, method= "update", paramtypez = {}, requiredModId = "spireTogether")
    public static class UnEndTurnPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("isDone")) {
                        f.replace("$_ = $proceed($$); if (spireTogether.network.P2P.P2PManager.GetPlayerCount().intValue() != 1) { spireTogether.util.SpireHelp.Gameplay.UnEndTurn(); }");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz= BetterDiscardPileToHandAction.class, method= "update", paramtypez = {}, requiredModId = "spireTogether")
    public static class LiquidMemoriesPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("lighten")) {
                        m.replace("$_ = $proceed($$); if (spireTogether.network.P2P.P2PManager.GetPlayerCount().intValue() != 1) { spireTogether.util.SpireHelp.Gameplay.UnEndTurn(); }");
                    }
                }
            };
        }
    }
}
