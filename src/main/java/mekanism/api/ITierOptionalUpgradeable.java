package mekanism.api;

public interface ITierOptionalUpgradeable<T> {

    boolean upgrade(T upgradeTier);

    default int UpgradeAmount(){
        return 1;
    }
}
