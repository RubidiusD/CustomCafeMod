package PatchEverything.util;

import basemod.EasyConfigPanel;
import PatchEverything.EverythingPatchMod;

public class EverythingPatchConfig extends EasyConfigPanel {
    public static boolean includeEventRelics = true;
    public static boolean includeStatusCards = true;
    public static boolean includeCurseCards = true;
    public static boolean divaRhythmGlow = true;

    public EverythingPatchConfig() {
        super(EverythingPatchMod.modID, ("PatchEverything:" + EverythingPatchConfig.class.getSimpleName()));
    }
}
