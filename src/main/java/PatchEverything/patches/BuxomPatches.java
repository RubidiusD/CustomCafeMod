package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class BuxomPatches {
    @SpirePatch2(clz= AbstractPlayer.class, method= SpirePatch.CLASS, requiredModId = "BuxomMod")
    public static class NakedField {
        public static SpireField<Boolean> naked = new SpireField<>(() -> false);
    }

    public static void setNaked(boolean naked) {
        NakedField.naked.set(AbstractDungeon.player, naked);
    }

    public static boolean getNaked() {
        return NakedField.naked.get(AbstractDungeon.player);
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onStartCombat", paramtypez = {}, requiredModId = "BuxomMod")
    public static class RemoveNakedOne {
        @SpireInstrumentPatch() public static ExprEditor Instrument() {
            return new ExprViewer() {
                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    super.edit(f);

                    if (f.getFieldName().equals("naked")) {
                        f.replace("$_ = PatchEverything.patches.BuxomPatches.getNaked();");
                    }
                }

                @Override public void edit(Cast c) throws CannotCompileException {
                    super.edit(c);

                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = null;");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "changeNaked", paramtypez = {boolean.class}, requiredModId = "BuxomMod")
    public static class RemoveNakedTwo {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean first = true;

                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (first) {
                        first = false;
                        m.replace("$_ = null; {" +
                            "    if (naked) {" +
                            "        PatchEverything.patches.BuxomPatches.setNaked(true);" +
                            "        if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.chosenClass.equals(BuxomMod.characters.TheBuxom.Enums.THE_BUXOM)) {" +
                            "            ((BuxomMod.characters.TheBuxom) com.megacrit.cardcrawl.dungeons.AbstractDungeon.player).naked = true;" +
                            "        }" +
                            "        com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, new BuxomMod.powers.ExposedPower(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, -1), -1)); " +
                            "    } else {" +
                            "        PatchEverything.patches.BuxomPatches.setNaked(false);" +
                            "        if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.chosenClass.equals(BuxomMod.characters.TheBuxom.Enums.THE_BUXOM)) {" +
                            "            ((BuxomMod.characters.TheBuxom) com.megacrit.cardcrawl.dungeons.AbstractDungeon.player).naked = false;" +
                            "        }" +
                            "        com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, BuxomMod.powers.ExposedPower.POWER_ID));" +
                            "    }" +
                            "}");
                    }
                    else {
                        m.replace("$_ = null;");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onGrow", paramtypez = {int.class}, requiredModId = "BuxomMod")
    public static class RemoveBeginGrow {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("beginGrowth")) {
                        m.replace("System.out.println(\"Removed the beginGrow\"); $_ = null;");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onShrink", paramtypez = {int.class}, requiredModId = "BuxomMod")
    public static class RemoveBeginShrink {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("beginShrink")) {
                        m.replace("System.out.println(\"Removed the beginShrink\"); $_ = null;");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.powers.ExposedPower", method= "atEndOfRound", paramtypez = {}, requiredModId = "BuxomMod")
    public static class NoLongerExposed {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("naked")) {
                        f.replace("$_ = PatchEverything.patches.BuxomPatches.getNaked();");
                    }
                }

                @Override public void edit(Cast c) throws CannotCompileException {
                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = null;");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.BuxomMod", method= "receiveOnBattleStart", paramtypez = {AbstractRoom.class}, requiredModId = "BuxomMod")
    public static class BattleStartBra {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                private boolean first = true;

                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (first) {
                        m.replace("$_ = null; BuxomMod.BuxomMod.braManager.onStartCombat();");
                        first = false;
                    } else {
                        m.replace("$_ = null;");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.BuxomMod", method= "receiveOnPlayerTurnStart", paramtypez = {}, requiredModId = "BuxomMod")
    public static class TurnStartBra {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                private boolean first = true;

                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (first) {
                        m.replace("$_ = null; BuxomMod.BuxomMod.braManager.onTurnStart();");
                        first = false;
                    } else {
                        m.replace("$_ = null;");
                    }
                }
            };
        }
    }

    public static void ReplacePatches() {
//        ClassPool pool = ClassPool.getDefault();
//        try {
//            CtClass cc = pool.get("fakermod.cards.saber.ManaTransfer");
//            CtMethod m = cc.getDeclaredMethod("use");
//            m.setBody("com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new PatchEverything.patches.ManaTransferAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player));");
//            cc.writeFile();
//            System.out.println("------------------------------------Mana Transfer Patched Successfully. -------------");
//        } catch (NotFoundException e) {
//            System.out.println("------------------------------------Mana Transfer not Found. -------------");
//        } catch (CannotCompileException e) {
//            System.out.println("------------------------------------Mana Transfer not Compiling. -------------");
//        } catch (IOException e) {
//            System.out.println("------------------------------------Mana Transfer not Being Happy. -------------");
//        }
    }
}
