package PatchEverything.patches.helpers;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.*;

import static PatchEverything.util.EverythingPatchConfig.glowDiscard;
import static com.evacipated.cardcrawl.modthespire.lib.SpirePatch.CONSTRUCTOR;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class DiscardGlowPatch {
    @SpireEnum public static AbstractCard.CardTags Discards;
    @SpireEnum public static AbstractCard.CardTags WantsDiscarding;

    @SpirePatches2({
            @SpirePatch2(clz= Acrobatics.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= AllOutAttack.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= CalculatedGamble.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= Concentrate.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= DaggerThrow.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= Prepared.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= StormOfSteel.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= Survivor.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= Unload.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(cls= "BuxomMod.cards.BrainDrain", method = CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod"),
            @SpirePatch2(cls= "BuxomMod.cards.MassiveGrowth", method = CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod")
    })
    public static class DiscardsPatch { @SpirePostfixPatch public static void Postfix(AbstractCard __instance) { __instance.tags.add(Discards); } }

    @SpirePatches2({
            @SpirePatch2(clz= Reflex.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(clz= Tactician.class, method = CONSTRUCTOR, paramtypez = {}),
            @SpirePatch2(cls= "tisCardPack.cards.green.GroupPlans", method = CONSTRUCTOR, paramtypez = {}, requiredModId = "tisCardPack"),
    })
    public static class WantsDiscardingPatch { @SpirePostfixPatch public static void Postfix(AbstractCard __instance) { __instance.tags.add(WantsDiscarding); } }

    public static CardBorderGlowManager.GlowInfo GlowCondition() {
        return new CardBorderGlowManager.GlowInfo() {
            @Override public String glowID() { return "EverythingPatchMod:DiscardCardsGlow"; }
            @Override public Color getColor(AbstractCard c) { return Color.GOLDENROD; }
            @Override public boolean test(AbstractCard c) {
                if (!glowDiscard) {
                    return false;
                }

                if (c.hasTag(WantsDiscarding)) {
                    for (AbstractCard card : player.hand.group) {
                        if (card.hasTag(Discards)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }
}
