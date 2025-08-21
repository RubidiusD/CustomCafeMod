package customcafepatchmod.util;

import basemod.EasyConfigPanel;
import customcafepatchmod.CustomCafePatchMod;

public class CustomCafeConfig extends EasyConfigPanel {
    public static boolean includeEventRelics = true;
    public static boolean includeStatusCards = true;

    public CustomCafeConfig() {
        super(CustomCafePatchMod.modID, ("customcafepatchmod:" + CustomCafeConfig.class.getSimpleName()));
    }
}
