package PatchEverything.implementation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.UUID;

public class UpgradeMasterAction extends AbstractGameAction {
    private final UUID uuid;

    public UpgradeMasterAction(UUID uuid) {this.uuid = uuid;}

    @Override
    public void update() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid)) {
                c.upgrade();
                break;
            }
        }

        this.isDone = true;
    }
}
