package PatchEverything.patches.helpers;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Purity;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import static PatchEverything.util.EverythingPatchConfig.glowDiscard;
import static PatchEverything.util.EverythingPatchConfig.glowExhaust;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.CURSE;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@SpirePatches2({
        @SpirePatch2(clz= Purity.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(cls= "BuxomMod.cards.Flee", method= SpirePatch.CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod"),
        @SpirePatch2(cls= "BuxomMod.cards.Hardheaded", method= SpirePatch.CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod"),
        @SpirePatch2(cls= "BuxomMod.cards.Omegabsorption", method= SpirePatch.CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod"),
        @SpirePatch2(cls= "BuxomMod.cards.OmegaCurse", method= SpirePatch.CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod"),
})
public class ExhaustGlowPatch {
    @SpireEnum public static AbstractCard.CardTags Exhausts;

    @SpirePostfixPatch public static void Postfix(AbstractCard __instance) {
        __instance.tags.add(Exhausts);
    }

    public static class GlowConditionSource {
        public static CardBorderGlowManager.GlowInfo GlowCondition() {
            return new CardBorderGlowManager.GlowInfo() {
                @Override public String glowID() { return "EverythingPatchMod:ExhaustCardsGlow"; }
                @Override public Color getColor(AbstractCard c) { return Color.GOLDENROD; }
                @Override public boolean test(AbstractCard c) {
                    if (!glowExhaust) {
                        return false;
                    }

                    if (c.hasTag(Exhausts)) {
                        for (AbstractCard card : player.hand.group) {
                            if (card.type == CURSE || glowDiscard) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            };
        }
    }

    @SpirePatch2(clz= GlowConditionSource.class, method= "GlowCondition", paramtypez = {}, requiredModId = "BuxomMod")
    public static class UsesSmartCheckPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("glowDiscard")) {
                        f.replace("$_ = BuxomMod.BuxomMod.getType(card) == com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS;");
                    }
                }
            };
        }
    }
}
