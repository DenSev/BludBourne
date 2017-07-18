package com.packtpub.libgdx.bludbourne.UI;


import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlot;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotTarget;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotTooltip;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotTooltipListener;
import com.packtpub.libgdx.bludbourne.inventory.ItemUseType;
import com.packtpub.libgdx.bludbourne.utility.Utility;

/**
 * Created on 04/27/2017.
 */

public class InventoryUI extends Window {

    public final static int _numSlots = 50;
    private int numSlots = 50;
    private int lengthSlotRow = 10;
    private Table inventorySlotTable;
    private Table playerSlotsTable;
    private Table equipSlots;
    private DragAndDrop dragAndDrop;
    private Array<Actor> inventoryActors;

    private final int slotWidth = 52;
    private final int slotHeight = 52;

    private InventorySlotTooltip inventorySlotTooltip;

    public InventoryUI() {
        super("Inventory", Utility.STATUSUI_SKIN, "solidbackground");

        dragAndDrop = new DragAndDrop();
        inventoryActors = new Array<Actor>();

        //create
        inventorySlotTable = new Table();
        inventorySlotTable.setName("Inventory_Slot_Table");

        playerSlotsTable = new Table();
        equipSlots = new Table();
        equipSlots.setName("Equipment_Slot_Table");

        equipSlots.defaults().space(10);
        inventorySlotTooltip = new InventorySlotTooltip(Utility.STATUSUI_SKIN);

        InventorySlot headSlot = new InventorySlot(
            ItemUseType.ARMOR_HELMET.getValue(),
            new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_helmet")));

        InventorySlot leftArmSlot = new InventorySlot(
            ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
            new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_weapon"))
        );

        InventorySlot rightArmSlot = new InventorySlot(
            ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
            new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_shield"))
        );

        InventorySlot chestSlot = new InventorySlot(
            ItemUseType.ARMOR_CHEST.getValue(),
            new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_chest")));

        InventorySlot legsSlot = new InventorySlot(
            ItemUseType.ARMOR_FEET.getValue(),
            new Image(Utility.ITEMS_TEXTUREATLAS.findRegion("inv_boot")));

        headSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        leftArmSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        rightArmSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        chestSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
        legsSlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));

        dragAndDrop.addTarget(new InventorySlotTarget(headSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(leftArmSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(chestSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(rightArmSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(legsSlot));

        playerSlotsTable.setBackground(new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog"))).getDrawable());

        //layout
        for (int i = 1; i <= numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            inventorySlotTable.add(inventorySlot).size(slotWidth, slotHeight);

            if (i % lengthSlotRow == 0) {
                inventorySlotTable.row();
            }
        }

        equipSlots.add();
        equipSlots.add(headSlot).size(slotWidth, slotHeight);
        equipSlots.row();

        equipSlots.add(leftArmSlot).size(slotWidth, slotHeight);
        equipSlots.add(chestSlot).size(slotWidth, slotHeight);
        equipSlots.add(rightArmSlot).size(slotWidth, slotHeight);
        equipSlots.row();

        equipSlots.add();
        equipSlots.right().add(legsSlot).size(slotWidth, slotHeight);

        playerSlotsTable.add(equipSlots);

        inventoryActors.add(inventorySlotTooltip);

        this.add(playerSlotsTable).padBottom(20).row();
        this.add(inventorySlotTable).row();
        this.debug();
        this.pack();
    }

    public Table getInventorySlotTable() {
        return inventorySlotTable;
    }

    public Table getEquipSlotTable() {
        return equipSlots;
    }

    public DragAndDrop getDragAndDrop() {
        return dragAndDrop;
    }

    public Array<Actor> getInventoryActors() {
        return inventoryActors;
    }
}

