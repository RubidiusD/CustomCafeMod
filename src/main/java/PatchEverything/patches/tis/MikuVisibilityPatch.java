package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import javassist.CannotCompileException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;

import static com.evacipated.cardcrawl.modthespire.lib.SpirePatch.CONSTRUCTOR;

@SpirePatch2(cls= "dumbjokedivamod.character.Diva", method= CONSTRUCTOR, paramtypez = {}, requiredModId = "")
public class MikuVisibilityPatch {
    @SpireInstrumentPatch public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override public void edit(ConstructorCall c) throws CannotCompileException {
                if (c.getClassName().equals("basemod.animations.AbstractAnimation")) {
                    c.replace("$_ = $proceed($$);");
                }
            }
        };
    }
}
