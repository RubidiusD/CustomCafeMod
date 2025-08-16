package customcafepatchmod.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicCard;

import java.util.ArrayList;

import static basemod.BaseMod.getCustomScreen;
import static basemod.BaseMod.openCustomScreen;
import static customcafepatchmod.util.CustomCafeConfig.includeEventRelics;

public class RelicCardScreen extends CustomScreen implements ScrollBarListener {
    @SpireEnum public static AbstractDungeon.CurrentScreen RELICCARDGRIDSCREEN;

    private static final float SPACE = 80.0F * Settings.scale;

    private static final float START_X = 600.0F * Settings.scale;

    private static final float START_Y = Settings.HEIGHT - 300.0F * Settings.scale;

    private float scrollY = START_Y;

    private float targetY = this.scrollY;

    private final float scrollLowerBound = Settings.HEIGHT - 100.0F * Settings.scale;

    private float scrollUpperBound = this.scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;

    private int row = 0;

    private int col = 0;

    private static final Color RED_OUTLINE_COLOR = new Color(-10132568);

    private static final Color GREEN_OUTLINE_COLOR = new Color(2147418280);

    private static final Color BLUE_OUTLINE_COLOR = new Color(-2016482392);

    private static final Color BLACK_OUTLINE_COLOR = new Color(168);

    private AbstractRelic hoveredRelic = null;

    private AbstractRelic clickStartedRelic = null;

    private boolean grabbedScreen = false;

    private float grabStartY = 0.0F;

    private final ScrollBar scrollBar;

    private Hitbox controllerRelicHb = null;

    private final ConfirmButton confirmButton;

    private ArrayList<AbstractRelic> relics;

    private boolean show = false;

    private boolean isDone = false;

    public ArrayList<PowerelicCard> cards;

    public RelicCardScreen() {
        this.scrollBar = new ScrollBar(this);
        this.confirmButton = new ConfirmButton("Selection done");
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return RELICCARDGRIDSCREEN;
    }

    @Override
    public void reopen() {

    }

    public void open() {
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(RelicLibrary.starterList);
        relics.addAll(RelicLibrary.commonList);
        relics.addAll(RelicLibrary.uncommonList);
        relics.addAll(RelicLibrary.rareList);
        relics.addAll(RelicLibrary.bossList);
        relics.addAll(RelicLibrary.shopList);
        if (includeEventRelics) {
            relics.addAll(RelicLibrary.specialList);
        }
        relics.removeIf((o1) -> !UnlockTracker.isRelicSeen(o1.relicId));
        relics.removeIf(PowerelicAllowlist::isRelicConvertibleToCard);
        relics.sort((o1, o2) -> o1.name.compareToIgnoreCase(o2.name));
        AbstractDungeon.isScreenUp = true;
        this.confirmButton.hideInstantly();
        AbstractDungeon.overlayMenu.showBlackScreen(0.5F);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.show = true;
        this.controllerRelicHb = null;
        this.relics = relics;
        this.targetY = this.scrollLowerBound;
        this.scrollY = Settings.HEIGHT - 400.0F * Settings.scale;
        AbstractDungeon.overlayMenu.cancelButton.hide();
        this.confirmButton.isDisabled = false;
        this.confirmButton.show();
        calculateScrollBounds();
    }

    @Override
    public void close() {
        this.show = false;
    }

    public boolean isClosed() {
        return !this.show;
    }

    @Override
    public void update() {
        if (isClosed() && !this.isDone)
            open();
        updateControllerInput();
        if (Settings.isControllerMode && this.controllerRelicHb != null)
            if (Gdx.input.getY() > Settings.HEIGHT * 0.7F) {
                this.targetY += Settings.SCROLL_SPEED;
                if (this.targetY > this.scrollUpperBound)
                    this.targetY = this.scrollUpperBound;
            } else if (Gdx.input.getY() < Settings.HEIGHT * 0.3F) {
                this.targetY -= Settings.SCROLL_SPEED;
                if (this.targetY < this.scrollLowerBound)
                    this.targetY = this.scrollLowerBound;
            }
        if (this.hoveredRelic != null) {
            if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())
                this.clickStartedRelic = this.hoveredRelic;
            if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed()) {
                CInputActionSet.select.unpress();
                if (this.hoveredRelic == this.clickStartedRelic) {
                    cards.remove(0).setRelicInfoForNewlyConvertedCard(this.hoveredRelic.makeCopy());
                    if (cards.isEmpty())
                        AbstractDungeon.closeCurrentScreen();
                }
            }
            if (InputHelper.justClickedRight || CInputActionSet.select.isJustPressed())
                this.clickStartedRelic = this.hoveredRelic;
            if (InputHelper.justReleasedClickRight || CInputActionSet.select.isJustPressed()) {
                CInputActionSet.select.unpress();
                if (this.hoveredRelic == this.clickStartedRelic) {
                    CardCrawlGame.relicPopup.open(this.hoveredRelic, this.relics);
                    this.clickStartedRelic = null;
                }
            }
        } else {
            this.clickStartedRelic = null;
        }
        boolean isScrollingScrollBar = this.scrollBar.update();
        if (!isScrollingScrollBar)
            updateScrolling();
        this.confirmButton.update();
        if (this.confirmButton.hb.clicked) {
            this.confirmButton.hb.clicked = false;
            this.isDone = true;
            AbstractDungeon.closeCurrentScreen();
        } else {
            InputHelper.justClickedLeft = false;
            InputHelper.justClickedRight = false;
            this.hoveredRelic = null;
            updateList(this.relics);
            if (Settings.isControllerMode && this.controllerRelicHb != null)
                Gdx.input.setCursorPosition((int)this.controllerRelicHb.cX, (int)(Settings.HEIGHT - this.controllerRelicHb.cY));
        }
    }

    private void updateControllerInput() {}

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.targetY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.targetY -= Settings.SCROLL_SPEED;
            }
            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartY = y - this.targetY;
            }
        } else if (InputHelper.isMouseDown) {
            this.targetY = y - this.grabStartY;
        } else {
            this.grabbedScreen = false;
        }
        this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.targetY);
        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        int size = this.relics.size();
        int scrollTmp;
        if (size > 10) {
            scrollTmp = size / 10 - 2;
            if (size % 10 != 0)
                scrollTmp++;
            this.scrollUpperBound = this.scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * 80.0F * Settings.scale;
        } else {
            this.scrollUpperBound = this.scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;
        }
    }

    private void resetScrolling() {
        if (this.targetY < this.scrollLowerBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollLowerBound);
        } else if (this.targetY > this.scrollUpperBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollUpperBound);
        }
    }

    private void updateList(ArrayList<AbstractRelic> list) {
        for (AbstractRelic r : list) {
            r.hb.move(r.currentX, r.currentY);
            r.update();
            if (r.hb.hovered)
                this.hoveredRelic = r;
        }
    }

    public void render(SpriteBatch sb) {
        if (isClosed())
            return;
        this.row = -1;
        this.col = 0;
        renderList(sb, this.relics);
        this.scrollBar.render(sb);
        this.confirmButton.render(sb);
    }

    @Override
    public void openingSettings() {
        // Required if you want to reopen your screen when the settings screen closes
        AbstractDungeon.previousScreen = RELICCARDGRIDSCREEN;
    }

    private void renderList(SpriteBatch sb, ArrayList<AbstractRelic> list) {
        this.row++;
        this.col = 0;
        for (AbstractRelic r : list) {
            if (this.col == 10) {
                this.col = 0;
                this.row++;
            }
            r.currentX = START_X + SPACE * this.col;
            r.currentY = this.scrollY - SPACE * this.row;
            if (RelicLibrary.redList.contains(r)) {
                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, RED_OUTLINE_COLOR);
                } else {
                    r.render(sb, false, RED_OUTLINE_COLOR);
                }
            } else if (RelicLibrary.greenList.contains(r)) {
                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, GREEN_OUTLINE_COLOR);
                } else {
                    r.render(sb, false, GREEN_OUTLINE_COLOR);
                }
            } else if (RelicLibrary.blueList.contains(r)) {
                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, BLUE_OUTLINE_COLOR);
                } else {
                    r.render(sb, false, BLUE_OUTLINE_COLOR);
                }
            } else if (UnlockTracker.isRelicLocked(r.relicId)) {
                r.renderLock(sb, BLACK_OUTLINE_COLOR);
            } else {
                r.render(sb, false, BLACK_OUTLINE_COLOR);
            }
            this.col++;
        }
    }

    public void scrolledUsingBar(float newPercent) {
        float newPosition = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.scrollY = newPosition;
        this.targetY = newPosition;
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    @SpirePatch2(clz= AbstractPlayer.class, method="preBattlePrep", paramtypez={})
    public static class CheckForEmptyCards {
        @SpirePostfixPatch
        public static void PostFix() {
            if (!((RelicCardScreen)getCustomScreen(RELICCARDGRIDSCREEN)).cards.isEmpty()) {
                openCustomScreen(RELICCARDGRIDSCREEN);
            }
        }
    }

    @SpirePatch2(clz= PowerelicCard.class, method="onObtainCard", paramtypez={})
    public static class AddEmptyCard {
        @SpirePostfixPatch
        public static void PostFix(PowerelicCard __instance) {
            if (__instance.capturedRelic == null) {
                ((RelicCardScreen)getCustomScreen(RELICCARDGRIDSCREEN)).cards.add(__instance);
            }
        }
    }
}
