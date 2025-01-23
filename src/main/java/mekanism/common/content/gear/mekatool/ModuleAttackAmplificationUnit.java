package mekanism.common.content.gear.mekatool;

import mekanism.api.EnumColor;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentGroup;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.ModuleConfigItem.EnumData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModuleAttackAmplificationUnit extends Module {

    private ModuleConfigItem<AttackDamage> attackDamage;

    @Override
    public void init() {
        attackDamage = addConfigItem(new ModuleConfigItem<>(this, "attack_damage", MekanismLang.MODULE_BONUS_ATTACK_DAMAGE,
                new EnumData<>(AttackDamage.class, getInstalledCount() + 2), AttackDamage.MED));
    }

    public int getDamage() {
        return attackDamage.get().getDamage();
    }


    @Override
    public void addHUDStrings(EntityPlayer player, List<String> hudStringAdder) {
        if (isEnabled()) {
            hudStringAdder.add(MekanismLang.MODULE_DAMAGE.translateColored(EnumColor.DARK_GREY, EnumColor.INDIGO, attackDamage.get().getDamage()).getFormattedText());
        }
    }

    public enum AttackDamage implements IHasTextComponent {
        OFF(0),
        LOW(4),
        MED(8),
        HIGH(16),
        EXTREME(24),
        MAX(32);

        private final int damage;
        private final ITextComponent label;

        AttackDamage(int damage) {
            this.damage = damage;
            this.label = new TextComponentGroup().getString(Integer.toString(damage));
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public int getDamage() {
            return damage;
        }
    }
}