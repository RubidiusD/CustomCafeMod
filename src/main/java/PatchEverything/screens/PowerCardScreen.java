package PatchEverything.screens;

import PatchEverything.patches.ExprViewer;
import basemod.BaseMod;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.Iterator;

public class PowerCardScreen extends CustomScreen implements ScrollBarListener {
    @SpireEnum public static AbstractDungeon.CurrentScreen POWERCARDGRIDSCREEN;

    private float grabStartY = 0.0F;

    private float currentDiffY = 0.0F;

    private AbstractCard hoveredCard = null;

    private final AbstractCard upgradePreviewCard = null;

    private AbstractRelic relic;

    public PowerCardScreen() {
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.grabbedScreen = false;
        this.confirmScreenUp = false;
        this.confirmButton = new GridSelectConfirmButton("Selection done");
        this.controllerCard = null;
        drawStartX = Settings.WIDTH;
        drawStartX -= 5.0F * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartX -= 4.0F * Settings.CARD_VIEW_PAD_X;
        drawStartX /= 2.0F;
        drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        this.scrollBar = new ScrollBar(this);
        this.scrollBar.move(0.0F, -30.0F * Settings.scale);
        this.targetGroup = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
    }

    private void InitCardList() {
        ArrayList<AbstractCard> cardList = CardLibrary.getAllCards();
        cardList.removeIf(abstractCard -> (abstractCard.type != AbstractCard.CardType.POWER));
        cardList.sort((o1, o2) -> o2.cost - o1.cost);
        cardList.sort((o1, o2) -> o2.rarity.compareTo(o1.rarity));
        cardList.sort((o1, o2) -> o2.color.compareTo(o1.color));
        this.targetGroup.clear();
        for (AbstractCard card : cardList) {
            card.isSeen = true;
            this.targetGroup.addToBottom(card);
        }
    }

    public void open(AbstractRelic target) {
        InitCardList();
        callOnOpen();
        AbstractDungeon.overlayMenu.cancelButton.hide();
        this.confirmButton.isDisabled = false;
        this.confirmButton.show();
        calculateScrollBounds();
        this.relic = target;
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return POWERCARDGRIDSCREEN;
    }

    @Override
    public void reopen() {

    }

    @Override
    public void close() {

    }

    @Override
    public void update() {
        updateControllerInput();
        if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen && this.upgradePreviewCard == null)
            if (Gdx.input.getY() > Settings.HEIGHT * 0.75F) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if (Gdx.input.getY() < Settings.HEIGHT * 0.25F) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }
        boolean isDraggingScrollBar = false;
        if (shouldShowScrollBar())
            isDraggingScrollBar = this.scrollBar.update();
        if (!isDraggingScrollBar)
            updateScrolling();
        this.confirmButton.update();
        if (this.confirmButton.hb.clicked) {
            this.confirmButton.hb.clicked = false;
            AbstractDungeon.closeCurrentScreen();
        } else {
            updateCardPositionsAndHoverLogic();
            if (this.hoveredCard != null && InputHelper.justClickedLeft)
                this.hoveredCard.hb.clickStarted = true;
            if (this.hoveredCard != null && InputHelper.justClickedRight)
                if (this.hoveredCard.canUpgrade()) {
                    this.hoveredCard.upgrade();
                } else if (this.hoveredCard.upgraded) {
                    this.hoveredCard = CardLibrary.getCard(this.hoveredCard.cardID);
                }
            if (this.hoveredCard != null && (this.hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())) {
                this.hoveredCard.hb.clicked = false;
                setRelicsCard(this.hoveredCard.makeStatEquivalentCopy());
                this.relic.atPreBattle();
                this.hoveredCard.targetDrawScale = 0.75F;
                this.hoveredCard.drawScale = 0.875F;
                CardCrawlGame.sound.play("CARD_SELECT");
                AbstractDungeon.closeCurrentScreen();
                return;
            }
            if (Settings.isControllerMode && this.controllerCard != null)
                Gdx.input.setCursorPosition((int)this.controllerCard.hb.cX, (int)(Settings.HEIGHT - this.controllerCard.hb.cY));
        }
    }

    private void setRelicsCard(AbstractCard c) {
        System.out.println("If You're Seeing This, Panic. A Lot.");
    }

    @SpirePatch2(clz= PowerCardScreen.class, method= "setRelicsCard", paramtypez = {AbstractCard.class}, requiredModId = "anniv7")
    public static class SetRelicsCardPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor()
            {
                @Override public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("println")) {
                        m.replace("((spireCafe.interactables.patrons.powerelic.implementation.PowerelicRelic) this.relic).capturedCard = c;");
                    }
                }
            };
        }
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && this.upgradePreviewCard == null) {
            boolean anyHovered = false;
            int index = 0;
            for (Iterator<AbstractCard> var3 = this.targetGroup.group.iterator(); var3.hasNext(); index++) {
                AbstractCard c = var3.next();
                if (c.hb.hovered) {
                    anyHovered = true;
                    break;
                }
            }
            if (!anyHovered) {
                Gdx.input.setCursorPosition((int)(this.targetGroup.group.get(0)).hb.cX, (int)(this.targetGroup.group.get(0)).hb.cY);
                this.controllerCard = this.targetGroup.group.get(0);
            } else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && this.targetGroup.size() > 5) {
                if (index < 5) {
                    index = this.targetGroup.size() + 2 - 4 - index;
                    if (index > this.targetGroup.size() - 1)
                        index -= 5;
                    if (index > this.targetGroup.size() - 1 || index < 0)
                        index = 0;
                } else {
                    index -= 5;
                }
                Gdx.input.setCursorPosition((int)(this.targetGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int)(this.targetGroup.group.get(index)).hb.cY);
                this.controllerCard = this.targetGroup.group.get(index);
            } else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && this.targetGroup.size() > 5) {
                if (index < this.targetGroup.size() - 5) {
                    index += 5;
                } else {
                    index %= 5;
                }
                Gdx.input.setCursorPosition((int)(this.targetGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int)(this.targetGroup.group.get(index)).hb.cY);
                this.controllerCard = this.targetGroup.group.get(index);
            } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    if (index % 5 < 4) {
                        index++;
                        if (index > this.targetGroup.size() - 1)
                            index -= this.targetGroup.size() % 5;
                    } else {
                        index -= 4;
                        if (index < 0)
                            index = 0;
                    }
                    Gdx.input.setCursorPosition((int)(this.targetGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int)(this.targetGroup.group.get(index)).hb.cY);
                    this.controllerCard = this.targetGroup.group.get(index);
                }
            } else {
                if (index % 5 > 0) {
                    index--;
                } else {
                    index += 4;
                    if (index > this.targetGroup.size() - 1)
                        index = this.targetGroup.size() - 1;
                }
                Gdx.input.setCursorPosition((int)(this.targetGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int)(this.targetGroup.group.get(index)).hb.cY);
                this.controllerCard = this.targetGroup.group.get(index);
            }
        }
    }

    private void updateCardPositionsAndHoverLogic() {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.targetGroup.group;
        for (int i = 0; i < cards.size(); i++) {
            int mod = i % 5;
            if (mod == 0 && i != 0)
                lineNum++;
            (cards.get(i)).target_x = drawStartX + mod * padX;
            (cards.get(i)).target_y = drawStartY + this.currentDiffY - lineNum * padY;
            (cards.get(i)).fadingOut = false;
            (cards.get(i)).update();
            (cards.get(i)).updateHoverLogic();
            this.hoveredCard = null;
            for (AbstractCard c : cards) {
                if (c.hb.hovered)
                    this.hoveredCard = c;
            }
        }
    }

    private void callOnOpen() {
        if (Settings.isControllerMode) {
            Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            this.controllerCard = null;
        }
        this.confirmScreenUp = false;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.controllerCard = null;
        this.hoveredCard = null;
        AbstractDungeon.topPanel.unhoverHitboxes();
        this.currentDiffY = 0.0F;
        this.grabStartY = 0.0F;
        this.grabbedScreen = false;
        hideCards();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = POWERCARDGRIDSCREEN;
        AbstractDungeon.overlayMenu.showBlackScreen(0.5F);
        this.confirmButton.hideInstantly();
        if (this.targetGroup.group.size() <= 5) {
            drawStartY = Settings.HEIGHT * 0.5F;
        } else {
            drawStartY = Settings.HEIGHT * 0.66F;
        }
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        boolean isDraggingScrollBar = this.scrollBar.update();
        if (!isDraggingScrollBar)
            if (!this.grabbedScreen) {
                if (InputHelper.scrolledDown) {
                    this.currentDiffY += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.currentDiffY -= Settings.SCROLL_SPEED;
                }
                if (InputHelper.justClickedLeft) {
                    this.grabbedScreen = true;
                    this.grabStartY = y - this.currentDiffY;
                }
            } else if (InputHelper.isMouseDown) {
                this.currentDiffY = y - this.grabStartY;
            } else {
                this.grabbedScreen = false;
            }
        if (!this.targetGroup.isEmpty())
            calculateScrollBounds();
        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        if (this.targetGroup.size() > 10) {
            int scrollTmp = this.targetGroup.size() / 5 - 2;
            if (this.targetGroup.size() % 5 != 0)
                scrollTmp++;
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * padY;
        } else {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }
    }

    private void resetScrolling() {
        if (this.currentDiffY < this.scrollLowerBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        } else if (this.currentDiffY > this.scrollUpperBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }
    }

    private void hideCards() {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.targetGroup.group;
        for (int i = 0; i < cards.size(); i++) {
            (cards.get(i)).setAngle(0.0F, true);
            int mod = i % 5;
            if (mod == 0 && i != 0)
                lineNum++;
            cards.get(i).lighten(true);
            cards.get(i).current_x = drawStartX + mod * padX;
            cards.get(i).current_y = drawStartY + this.currentDiffY - lineNum * padY - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            cards.get(i).targetDrawScale = 0.75F;
            cards.get(i).drawScale = 0.75F;
        }
    }

    public void render(SpriteBatch sb) {
        if (shouldShowScrollBar())
            this.scrollBar.render(sb);
        if (this.hoveredCard != null) {
            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.render(sb);
            this.hoveredCard.renderCardTip(sb);
        }
        if (this.confirmScreenUp) {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT - 64.0F * Settings.scale);
            this.hoveredCard.current_x = Settings.WIDTH / 2.0F;
            this.hoveredCard.current_y = Settings.HEIGHT / 2.0F;
            this.hoveredCard.render(sb);
            this.hoveredCard.updateHoverLogic();
        }
        this.targetGroup.render(sb);
        this.confirmButton.render(sb);
        FontHelper.renderDeckViewTip(sb, "Select a power card to turn into a relic.", 96.0F * Settings.scale, Settings.CREAM_COLOR);
    }

    @Override
    public void openingSettings()
    {
        // Required if you want to reopen your screen when the settings screen closes
        AbstractDungeon.previousScreen = POWERCARDGRIDSCREEN;
    }

    public void scrolledUsingBar(float newPercent) {
        this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return (!this.confirmScreenUp && this.scrollUpperBound > SCROLL_BAR_THRESHOLD);
    }

    private static final float SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;

    private static float drawStartX;

    private static float drawStartY;

    private static float padX;

    private static float padY;

    private final CardGroup targetGroup;

    private final float scrollLowerBound;

    private float scrollUpperBound;

    private boolean grabbedScreen;

    private boolean confirmScreenUp;

    private final GridSelectConfirmButton confirmButton;

    private final ScrollBar scrollBar;

    private AbstractCard controllerCard;

    public static void openWithExtraSteps(AbstractRelic r) {
        BaseMod.openCustomScreen(POWERCARDGRIDSCREEN, r);
    }

    @SpirePatch2(cls= "spireCafe.interactables.patrons.powerelic.implementation.PowerelicRelic", method= "atPreBattle", paramtypez={}, requiredModId = "anniv7")
    public static class RelicPatch {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                boolean first = true;

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (first) {
                        first = false;
                        f.replace("$_ = $proceed($$);" +
                            "if (this.capturedCard == null && !com.megacrit.cardcrawl.dungeons.AbstractDungeon.isScreenUp) {" +
                            "    PatchEverything.screens.PowerCardScreen.openWithExtraSteps(this);" +
                            "}"
                        );
                    }
                }
            };
        }
    }
}
