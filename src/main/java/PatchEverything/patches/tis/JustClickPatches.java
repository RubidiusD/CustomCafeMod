package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.InvocationTargetException;

import static PatchEverything.util.EverythingPatchConfig.quickPlayCards;

public class JustClickPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method= "clickAndDragCards", requiredModId= "spireTogether")
    public static class JustClickPatch {
        @SpireInsertPatch(rlocs = {28, 15}) public static void Insert(AbstractPlayer __instance) {
            if (quickPlayCards && (InputHelper.justClickedLeft || CInputHelper.isJustPressed(0)) && __instance.hoveredCard != null && !AbstractDungeon.isScreenUp) {
                try {
                    AutoplayCardFor(__instance);
                } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.out.println("------- Unsuccessful Auto-playing of TiS Card --------------------");
                }
            }
        }

        public static void AutoplayCardFor(AbstractPlayer p) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            System.out.println("Cannae Autoplay that Card for ye lassie :/");
        }
    }

    @SpirePatch2(clz= JustClickPatch.class, method= "AutoplayCardFor", paramtypez= {AbstractPlayer.class}, requiredModId = "spireTogether")
    public static class AutoplayPatch {
        public static java.util.ArrayList<Object> availableAllies = new java.util.ArrayList<Object>();

        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("println")) {
                        m.replace("$_ = null;" +
                            "if (p.hoveredCard instanceof spireTogether.cards.CustomMultiplayerCard) {" +
                            "    spireTogether.cards.CustomMultiplayerCard clickedCard = (spireTogether.cards.CustomMultiplayerCard) p.hoveredCard;" +
                            "    java.lang.reflect.Method playCardMethod = com.megacrit.cardcrawl.characters.AbstractPlayer.class.getDeclaredMethod(\"playCard\", new Class[0]);" +
                            "    playCardMethod.setAccessible(true);" +
                            "    PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.clear();" +
                            "    PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.addAll(spireTogether.util.SpireHelp.Multiplayer.Players.GetPlayers(true, true));" +
                            "    if (" +
                            "            (clickedCard.getAllyTargetingRule() == spireTogether.cards.CustomMultiplayerCard.AllyCardTargeting.ALLY_ONLY && PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.size() == 1) ||" +
                            "            (clickedCard.getAllyTargetingRule() == spireTogether.cards.CustomMultiplayerCard.AllyCardTargeting.ALLY_AND_ENEMY && PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.size() == 1) ||" +
                            "            (clickedCard.getAllyTargetingRule() == spireTogether.cards.CustomMultiplayerCard.AllyCardTargeting.ALLY_OR_SELF && PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.isEmpty())" +
                            "    ) {" +
                            "        com.megacrit.cardcrawl.core.AbstractCreature hoveredCreature;" +
                            "        if (PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.isEmpty()) {" +
                            "            hoveredCreature = p;" +
                            "        } else {" +
                            "            hoveredCreature = ((spireTogether.network.P2P.P2PPlayer) PatchEverything.patches.tis.JustClickPatches$AutoplayPatch.availableAllies.get(0)).GetEntity();" +
                            "        }" +
                            "        java.lang.reflect.Field field = com.megacrit.cardcrawl.characters.AbstractPlayer.class.getDeclaredField(\"hoveredMonster\");" +
                            "        field.setAccessible(true);" +
                            "        field.set(p, hoveredCreature);" +
                            "        playCardMethod.invoke(p, new Object[0]);" +
                            "    }" +
                            "}"
                        );
                    }
                }
            };
        }
    }
}
