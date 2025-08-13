package customcafepatchmod.util;

import basemod.EasyConfigPanel;
import customcafepatchmod.CustomCafePatchMod;

public class CustomCafeConfig extends EasyConfigPanel {
    public static boolean includeEventRelics = true;

    public CustomCafeConfig() {
        super(CustomCafePatchMod.modID, CustomCafePatchMod.makeID("MyModConfig"));
    }
}
