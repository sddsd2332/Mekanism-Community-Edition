package mekanism.common.inventory.container;

import mekanism.common.inventory.container.slot.IInsertableSlot;
import mekanism.common.inventory.container.slot.MainInventorySlot;
import mekanism.common.inventory.slot.ArmorSlot;
import mekanism.common.inventory.slot.HotBarSlot;
import mekanism.common.inventory.slot.OffhandSlot;
import mekanism.common.util.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class MekanismContainer extends Container {

    public static final int BASE_Y_OFFSET = 84;
    public static final int TRANSPORTER_CONFIG_WINDOW = 0;
    public static final int SIDE_CONFIG_WINDOW = 1;
    public static final int UPGRADE_WINDOW = 2;
    public static final int SKIN_SELECT_WINDOW = 3;

    protected final InventoryPlayer inv;
    protected final List<ArmorSlot> armorSlots = new ArrayList<>();
    protected final List<MainInventorySlot> mainInventorySlots = new ArrayList<>();
    protected final List<HotBarSlot> hotBarSlots = new ArrayList<>();
    protected final List<OffhandSlot> offhandSlots = new ArrayList<>();


    protected MekanismContainer(InventoryPlayer inv) {
        this.inv = inv;
    }

    public boolean isRemote() {
        return inv.player.world.isRemote;
    }

    public UUID getPlayerUUID() {
        return inv.player.getUniqueID();
    }

    @Override
    protected Slot addSlotToContainer(Slot slot) {
        //Manually handle the code that is in super.addSlot so that we do not end up adding extra elements to
        // inventoryItemStacks as we handle the tracking/sync changing via the below track call. This way we are
        // able to minimize the amount of overhead that we end up with due to keeping track of the stack in SyncableItemStack
        slot.slotNumber = inventorySlots.size();
        inventorySlots.add(slot);
        if (slot instanceof ArmorSlot) {
            armorSlots.add((ArmorSlot) slot);
        } else if (slot instanceof MainInventorySlot) {
            mainInventorySlots.add((MainInventorySlot) slot);
        } else if (slot instanceof HotBarSlot) {
            hotBarSlots.add((HotBarSlot) slot);
        } else if (slot instanceof OffhandSlot) {
            offhandSlots.add((OffhandSlot) slot);
        }
        return slot;
    }

    protected void addSlotsAndOpen() {
        addSlots();
        addInventorySlots(inv);
        openInventory(inv);
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        //Is this the proper default
        //TODO: Re-evaluate this and maybe add in some distance based checks??
        return true;
    }


    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        if (slot instanceof IInsertableSlot insertableSlot) {
            if (!insertableSlot.canMergeWith(stack)) {
                return false;
            }
        }
        return super.canMergeSlot(stack, slot);
    }

    @Override
    public void onContainerClosed(@Nonnull EntityPlayer player) {
        super.onContainerClosed(player);
        closeInventory(player);
    }

    protected void closeInventory(@Nonnull EntityPlayer player) {
        if (!player.world.isRemote) {
        //    clearSelectedWindow(player.getUniqueID());
        }
    }

    protected void openInventory(@Nonnull InventoryPlayer inv) {
    }

    protected int getInventoryYOffset() {
        return BASE_Y_OFFSET;
    }

    protected int getInventoryXOffset() {
        return 8;
    }

    protected void addInventorySlots(@Nonnull InventoryPlayer inv) {
        if (this instanceof IEmptyContainer) {
            //Don't include the player's inventory slots
            return;
        }
        int yOffset = getInventoryYOffset();
        int xOffset = getInventoryXOffset();
        for (int slotY = 0; slotY < 3; slotY++) {
            for (int slotX = 0; slotX < 9; slotX++) {
                addSlotToContainer(new MainInventorySlot(inv, InventoryPlayer.getHotbarSize() + slotX + slotY * 9, xOffset + slotX * 18, yOffset + slotY * 18));
            }
        }
        yOffset += 58;
        for (int slotX = 0; slotX < InventoryPlayer.getHotbarSize(); slotX++) {
            addSlotToContainer(createHotBarSlot(inv, slotX, xOffset + slotX * 18, yOffset));
        }
    }

    public static final EntityEquipmentSlot[] EQUIPMENT_SLOT_TYPES = EntityEquipmentSlot.values();

    protected void addArmorSlots(@Nonnull InventoryPlayer inv, int x, int y, int offhandOffset) {
        for (int index = 0; index < inv.armorInventory.size(); index++) {
            final EntityEquipmentSlot slotType = EQUIPMENT_SLOT_TYPES[2 + inv.armorInventory.size() - index - 1];
            addSlotToContainer(new ArmorSlot(inv, 36 + inv.armorInventory.size() - index - 1, x, y, slotType));
            y += 18;
        }
        if (offhandOffset != -1) {
            addSlotToContainer(new OffhandSlot(inv, 40, x, y + offhandOffset));
        }
    }

    protected HotBarSlot createHotBarSlot(@Nonnull InventoryPlayer inv, int index, int x, int y) {
        return new HotBarSlot(inv, index, x, y);
    }

    protected void addSlots() {
    }


    public List<MainInventorySlot> getMainInventorySlots() {
        return Collections.unmodifiableList(mainInventorySlots);
    }

    public List<HotBarSlot> getHotBarSlots() {
        return Collections.unmodifiableList(hotBarSlots);
    }

    /**
     * {@inheritDoc}
     *
     * @return The contents in this slot AFTER transferring items away.
     */
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int slotID){
        Slot currentSlot = inventorySlots.get(slotID);
        if (currentSlot == null || !currentSlot.getHasStack()) {
            return ItemStack.EMPTY;
        }
        ItemStack slotStack = currentSlot.getStack();
        ItemStack stackToInsert = slotStack;


        if (stackToInsert.getCount() == slotStack.getCount()) {
            //If nothing changed then return that fact
            return ItemStack.EMPTY;
        }
        //Otherwise, decrease the stack by the amount we inserted, and return it as a new stack for what is now in the slot
        return transferSuccess(currentSlot, player, slotStack, stackToInsert);

    }


    @Nonnull
    protected ItemStack transferSuccess(@Nonnull Slot currentSlot, @Nonnull EntityPlayer player, @Nonnull ItemStack slotStack, @Nonnull ItemStack stackToInsert) {
        int difference = slotStack.getCount() - stackToInsert.getCount();
        currentSlot.decrStackSize(difference);
        ItemStack newStack = StackUtils.size(slotStack, difference);
        currentSlot.onTake(player, newStack);
        return newStack;
    }










}
