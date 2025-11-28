package PatchEverything.patches.tis;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import javassist.expr.*;

public class BuxomPatches {
    @SpirePatch2(cls= "BuxomMod.BuxomMod", method= SpirePatch.CONSTRUCTOR, paramtypez = {}, requiredModId = "BuxomMod")
    public static class OnByDefault {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getBool")) {
                        m.replace("$_ = true;");
                    }
                }
            };
        }
    }

    @SpirePatch2(cls = "PatchEverything.EverythingPatchMod", method= "getGeorge", requiredModId = "BuxomMod")
    public static class NewGeorge {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("println")) {
                        m.replace("george = new BuxomMod.characters.TheBuxom(\"George\", BuxomMod.characters.TheBuxom.Enums.THE_BUXOM);");
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
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.EverythingPatchMod.getGeorge();");
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
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.EverythingPatchMod.getGeorge();");
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
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onGrow", paramtypez = {int.class}, requiredModId = "BuxomMod")
    public static class OnlyGrowBuxom {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("beginGrowth")) {
                        m.replace("$_ = null; if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof BuxomMod.characters.TheBuxom) {((BuxomMod.characters.TheBuxom)com.megacrit.cardcrawl.dungeons.AbstractDungeon.player).beginGrowth((float) growthAmount);}");
                    }
                }

                @Override public void edit(Cast c) throws CannotCompileException {
                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.EverythingPatchMod.getGeorge();");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @SpirePatch2(cls= "BuxomMod.ui.BraManager", method= "onShrink", paramtypez = {int.class}, requiredModId = "BuxomMod")
    public static class OnlyShrinkBuxom {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("beginShrink")) {
                        m.replace("$_ = null; if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof BuxomMod.characters.TheBuxom) {((BuxomMod.characters.TheBuxom)com.megacrit.cardcrawl.dungeons.AbstractDungeon.player).beginShrink((float) shrinkAmount);}");
                    }
                }

                @Override public void edit(Cast c) throws CannotCompileException {
                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("$_ = (BuxomMod.characters.TheBuxom)PatchEverything.EverythingPatchMod.getGeorge();");
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

    @SpirePatches2({
            @SpirePatch2(cls= "BuxomMod.vfx.ShrinkEvent", method= SpirePatch.CONSTRUCTOR, requiredModId = "BuxomMod"),
            @SpirePatch2(cls= "BuxomMod.vfx.ShrinkEvent", method= "initiate", paramtypez = {}, requiredModId = "BuxomMod"),
            @SpirePatch2(cls= "BuxomMod.vfx.ShrinkEvent", method= "apply", paramtypez = {}, requiredModId = "BuxomMod")
    })
    public static class ShrinkEvent {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(Cast c) throws CannotCompileException {
                    super.edit(c);

                    try {
                        if (c.getType().getSimpleName().equals("TheBuxom"))
                            c.replace("if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof BuxomMod.characters.TheBuxom) {$_ = $proceed($$);} else {$_ = (BuxomMod.characters.TheBuxom)PatchEverything.EverythingPatchMod.getGeorge();}");
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }
}
