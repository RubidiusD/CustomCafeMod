package PatchEverything.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import javassist.expr.*;

public class BuxomPatches {
    @SpirePatch2(cls= "com.megacrit.cardcrawl.characters.AbstractPlayer", method= SpirePatch.CLASS, requiredModId = "BuxomMod")
    public static class George {
        public static SpireField<AbstractPlayer> george = new SpireField<>(() -> null);

        public static AbstractPlayer get() {
            AbstractPlayer g = George.george.get(AbstractDungeon.player);
            if (g == null) {George.george.set(AbstractDungeon.player, g);}
            return g;
        }
    }

    @SpirePatch2(cls = "PatchEverything.patches.BuxomPatches$George", method= "get", requiredModId = "BuxomMod")
    public static class NewGeorge {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("set")) {
                        m.replace("g = new BuxomMod.characters.TheBuxom(\"George\", BuxomMod.characters.TheBuxom.Enums.THE_BUXOM); $_ = $proceed($$);");
                    }
                }
            };
        }
    }

    @SpirePatches2({
            @SpirePatch2(cls= "BuxomMod.powers.ExposedPower", method= "atEndOfRound", paramtypez = {}, requiredModId = "BuxomMod"),
            @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "changeNaked", paramtypez = {boolean.class}, requiredModId = "BuxomMod")
    })
    public static class RemoveGetNaked {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(Cast c) throws CannotCompileException {
                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.patches.BuxomPatches.George.get();");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onStartCombat", paramtypez = {}, requiredModId = "BuxomMod")
    public static class DontAlwaysAddBuxom {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean first = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("addToBottom") && first) {
                        first = false;
                        m.replace("if (this.permaSize == 0) {$_ = null;} else {$_ = $proceed($$);}");
                    }
                }

                @Override public void edit(Cast c) throws CannotCompileException {
                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.patches.BuxomPatches.George.get();");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "startGame", paramtypez = {}, requiredModId = "BuxomMod")
    public static class StartingBuxom {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean first = true;

                @Override public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("permaSizeStart") && first) {
                        first = false;
                        f.replace("$_ = $proceed($$); if (!(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof BuxomMod.characters.TheBuxom)) {this.permaSizeStart = 0;}");
                    }

                    else if (f.getFieldName().equals("straining")) {
                        f.replace("$_ = $proceed($$); PatchEverything.patches.BuxomPatches.George.george.set(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, new BuxomMod.characters.TheBuxom(\"George\", BuxomMod.characters.TheBuxom.Enums.THE_BUXOM));");
                    }
                }
            };
        }
    }

    @SpirePatches2({
            @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onGrow", paramtypez = {int.class}, requiredModId = "BuxomMod"),
            @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onShrink", paramtypez = {int.class}, requiredModId = "BuxomMod")
    })
    public static class OnlyAnimateBuxom {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("beginGrowth") || m.getMethodName().equals("beginShrink")) {
                        m.replace("$_ = null; if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof BuxomMod.characters.TheBuxom) {$proceed($$);}");
                    }
                }

                @Override public void edit(Cast c) throws CannotCompileException {
                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.patches.BuxomPatches.George.get();");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @SpirePatches2({
            @SpirePatch2(cls= "BuxomMod.BuxomMod", method= "receiveOnBattleStart", paramtypez = {AbstractRoom.class}, requiredModId = "BuxomMod"),
            @SpirePatch2(cls= "BuxomMod.BuxomMod", method= "receiveOnPlayerTurnStart", paramtypez = {}, requiredModId = "BuxomMod")
    })
    public static class DoItAnyway {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(Instanceof i) throws CannotCompileException {
                    i.replace("$_ = true;");
                }
            };
        }
    }
}
