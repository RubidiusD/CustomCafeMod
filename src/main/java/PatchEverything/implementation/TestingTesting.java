package PatchEverything.implementation;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class TestingTesting extends BaseCard {
    public static final String ID = ("PatchEverything:" + TestingTesting.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.ENEMY,
            0    // card cost!! (-1 is X, -2 is unplayable)
    );

    private static final int MAGIC = 500;
    private static final int UPG_MAGIC = 100;

    public TestingTesting() {
        super(ID, info); // calls the parent constructor

        setMagic(MAGIC, UPG_MAGIC);
        setSelfRetain(true);
        setInnate(true);
        setCustomVar("Draw", 0, 1);
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.name = cardStrings.NAME + "+" + this.timesUpgraded;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber * 1000000)));
        addToBot(new ApplyPowerAction(m, p, new RegenPower(m, magicNumber * 1000000)));
        if (customVar("Draw") != 0) {addToBot(new DrawCardAction(customVar("Draw")));}
        this.upgrade();
        addToBot(new UpgradeMasterAction(this.uuid));
    }

    @SpirePatch2(clz= TestingTesting.class, method= "use", requiredModId = "rubimod") public static class RubiMod {@SpireInstrumentPatch public static ExprEditor Instrument() {return new ExprEditor() {@Override public void edit(MethodCall m) throws CannotCompileException {if (m.getMethodName().equals("upgrade")) {m.replace("$_ = $proceed($$); com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, p, new rubimod.powers.debuff.Bleeding(m))); com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, p, new rubimod.powers.debuff.LeechToxin(m, p, magicNumber * 1000000))); com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, p, new rubimod.powers.debuff.Necrotoxin(m, p, magicNumber * 1000000)));");}}};}}
    @SpirePatch2(clz= TestingTesting.class, method= "use", requiredModId = "dumbjokedivamod") public static class DivaMod {@SpireInstrumentPatch public static ExprEditor Instrument() {return new ExprEditor() {@Override public void edit(MethodCall m) throws CannotCompileException {if (m.getMethodName().equals("upgrade")) {m.replace("$_ = $proceed($$); com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, p, new dumbjokedivamod.powers.CaptivationPower(m, magicNumber * 1000000)));");}}};}}
    @SpirePatch2(clz= TestingTesting.class, method= "use", requiredModId = "BuxomMod") public static class BuxomMod {@SpireInstrumentPatch public static ExprEditor Instrument() {return new ExprEditor() {@Override public void edit(MethodCall m) throws CannotCompileException {if (m.getMethodName().equals("upgrade")) {m.replace("$_ = $proceed($$); com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p, new BuxomMod.powers.MilkPower(p, p, magicNumber * 1000000)));");}}};}}

    @Override
    public AbstractCard makeCopy() { // Optional
        return new TestingTesting();
    }
}
