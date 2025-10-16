package PatchEverything.patches;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.*;
import com.megacrit.cardcrawl.stances.WrathStance;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@SpirePatches2({
        @SpirePatch2(clz= EmptyBody.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(clz= EmptyFist.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(clz= EmptyMind.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(clz= FearNoEvil.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(clz= Meditate.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(clz= Tranquility.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {}),
        @SpirePatch2(clz= Vigilance.class, method= SpirePatch.CONSTRUCTOR, paramtypez = {})
})
public class CalmCardsPatch {
    @SpireEnum public static AbstractCard.CardTags CALM;

    @SpirePostfixPatch public static void Postfix(AbstractCard __instance) {
        __instance.tags.add(CALM);
    }

    public static CardBorderGlowManager.GlowInfo GlowCondition() {
        return new CardBorderGlowManager.GlowInfo() {
            @Override public String glowID() { return "EverythingPatchMod:CalmCardsGlow"; }
            @Override public Color getColor(AbstractCard c) { return Color.PURPLE; }
            @Override public boolean test(AbstractCard c) { return player.stance.ID.equals(WrathStance.STANCE_ID) && c.hasTag(CALM); }
        };
    }
}
