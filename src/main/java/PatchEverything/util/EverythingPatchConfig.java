package PatchEverything.util;

import basemod.EasyConfigPanel;
import PatchEverything.EverythingPatchMod;

public class EverythingPatchConfig extends EasyConfigPanel {
    public static boolean includeEventRelics = true;
    public static boolean includeStatusCards = true;
    public static boolean includeCurseCards = true;
    public static boolean divaRhythmGlow = true;
    public static boolean showPowerTotals = false;
    public static boolean quickPlayCards = false;
    public static boolean tweakArtifact = true; // in an instrument patch
    public static boolean glowCalm = true;
    public static boolean glowDiscard = true;
    public static boolean glowExhaust = true;

    public EverythingPatchConfig() {
        super(EverythingPatchMod.modID, ("PatchEverything:" + EverythingPatchConfig.class.getSimpleName()));
    }
}
