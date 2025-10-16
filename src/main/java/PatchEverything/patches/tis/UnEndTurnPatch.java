package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch2(clz=ShowCardAndAddToHandEffect.class, method= "update", paramtypez = {}, requiredModId = "spireTogether")
public class UnEndTurnPatch {
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
