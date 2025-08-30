package PatchEverything.patches.faker;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch2(cls= "fakermod.cards.GlassHeart", method= "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class}, requiredModId = "fakermod")
public class GlassHeartPatch {
    @SpireInstrumentPatch public static ExprEditor Instrument() {
        return new ExprEditor() {
            boolean second = false;
            @Override public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("addToBot")) {
                    if (!second) {
                        second = true;
                    } else {
                        m.replace("$_ = null; if (this.magicNumber != 0) {$proceed($$);}");
                    }
                }
            }
        };
    }
}
