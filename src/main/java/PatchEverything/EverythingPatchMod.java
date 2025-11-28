package PatchEverything;

import PatchEverything.RunMods.CreativeAIRelic;
import PatchEverything.RunMods.CreativeAIRunMod;
import PatchEverything.implementation.BaseCard;
import PatchEverything.RunMods.BlasphemyRunMod;
import PatchEverything.RunMods.BlasphemyRunRelic;
import PatchEverything.implementation.Poof;
import PatchEverything.patches.CalmCardsPatch;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.CardBorderGlowManager;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import PatchEverything.patches.PowerCardScreen;
import PatchEverything.util.EverythingPatchConfig;
import PatchEverything.util.GeneralUtils;
import PatchEverything.util.KeywordInfo;
import PatchEverything.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.util.*;

import static PatchEverything.util.EverythingPatchConfig.divaRhythmGlow;
import static basemod.BaseMod.addCustomScreen;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@SpireInitializer
@SpireSideload(modIDs = {"justclick"})
public class EverythingPatchMod implements
        PostInitializeSubscriber, EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    public static AbstractPlayer george;

    public static AbstractPlayer getGeorge() {
        if (george == null) {
            System.out.println("Oh Shit George is Dead");
        }
        return george;
    };

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new EverythingPatchMod();
    }

    public EverythingPatchMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause().getMessage());
            }
        }
    }

    @Override
    public void receiveEditCards() { // adds any cards to the game
        new AutoAdd(modID) // Loads files
                .packageFilter(BaseCard.class) // in the same package as this class
                .any(BaseCard.class, (info, card) -> {
                    BaseMod.addCard(card);
                    if (info.seen || card.rarity == AbstractCard.CardRarity.BASIC)
                        UnlockTracker.markCardAsSeen(card.cardID); // marks as discovered if seen before or a starter
                });

        if (Loader.isModLoaded("dumbjokedivamod")) {
            CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
                @Override public String glowID() { return ("EverythingPatchMod:RhythmGlow"); }
                @Override public Color getColor(AbstractCard card) {
                    return new Color(0.5f, 1.0f, 0.65f, (player.hasPower("dumbjokedivamod:Silenced")) ? 0.5f : 1.0f);
                }
                @Override public boolean test(AbstractCard card) {
                    return (divaRhythmGlow && player.hasPower("dumbjokedivamod:Rhythm") && player.getPower("dumbjokedivamod:Rhythm").amount == card.costForTurn);
                }
            });
        }
        CardBorderGlowManager.addGlowInfo(CalmCardsPatch.GlowCondition());
    }

    @Override
    public void receiveEditRelics() { // adds any relics to the game
        BaseMod.addRelic(new Poof(), RelicType.SHARED);
        BaseMod.addRelic(new BlasphemyRunRelic(), RelicType.SHARED);
        BaseMod.addRelic(new CreativeAIRelic(), RelicType.SHARED);
    }

    @Override
    public void receivePostDungeonInitialize() {
        if (Loader.isModLoaded("ExhaustPoof")) {
            player.relics.add(new Poof());
            player.reorganizeRelics();
        }
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, new EverythingPatchConfig());
        if (Loader.isModLoadedOrSideloaded("anniv7")) {
            addCustomScreen(new PowerCardScreen());
        }
    }

    @Override
    public void receiveCustomModeMods(List<CustomMod> list) {
        list.add(new CustomMod(BlasphemyRunMod.ID, "b", true));
        list.add(new CustomMod(CreativeAIRunMod.ID, "b", true));
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
        BaseMod.loadCustomStringsFile(RunModStrings.class,
                localizationPath(lang, "RunStrings.json"));
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {return resourcesFolder + "/images/" + file;}
    public static String powerPath(String file) {return resourcesFolder + "/images/powers/" + file;}
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = EverythingPatchMod.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + EverythingPatchMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(EverythingPatchMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
}
