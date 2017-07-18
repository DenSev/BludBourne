package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.inventory.InventoryItemLocation;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlot;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotTarget;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotTooltip;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotTooltipListener;
import com.packtpub.libgdx.bludbourne.observers.InventorySlotObserver;
import com.packtpub.libgdx.bludbourne.observers.StoreInventoryObserver;
import com.packtpub.libgdx.bludbourne.observers.StoreInventorySubject;
import com.packtpub.libgdx.bludbourne.utility.InventoryUtil;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;
import com.packtpub.libgdx.bludbourne.utility.Utility;

/**
 * Created on 17.07.2017.
 */

public class StoreInventoryUI extends Window implements InventorySlotObserver, StoreInventorySubject {

    private int numStoreInventorySlots = 30;
    private int lengthSlotRow = 10;
    private Table inventorySlotTable;
    private Table playerInventorySlotTable;
    private DragAndDrop dragAndDrop;
    private Array<Actor> inventoryActors;

    private static final String STORE_INVENTORY = "Store_Inventory";
    private static final String PLAYER_INVENTORY = "Player_Inventory";

    private final int slotWidth = 52;
    private final int slotHeight = 52;

    private InventorySlotTooltip inventorySlotTooltip;

    private Label sellTotalLabel;
    private Label buyTotalLabel;
    private Label playerTotalGP;

    private int tradeInVal = 0;
    private int fullValue = 0;
    private int playerTotal = 0;

    private Button sellButton;
    private Button buyButton;
    public TextButton closeButton;

    private Table buttons;
    private Table totalLabels;

    private Array<StoreInventoryObserver> observers;


    private static String SELL = "SELL";
    private static String BUY = "BUY";
    private static String GP = " GP";
    private static String PLAYER_TOTAL = "Player Total";

    public StoreInventoryUI() {
        super("Store Inventory", Utility.STATUSUI_SKIN, "solidbackground");

        observers = new Array<>();

        this.setFillParent(true);

        //create
        dragAndDrop = new DragAndDrop();
        inventoryActors = new Array<>();
        inventorySlotTable = new Table();
        inventorySlotTable.setName(STORE_INVENTORY);

        playerInventorySlotTable = new Table();
        playerInventorySlotTable.setName(PLAYER_INVENTORY);
        inventorySlotTooltip = new InventorySlotTooltip(Utility.STATUSUI_SKIN);

        sellButton = new TextButton(SELL, Utility.STATUSUI_SKIN, "inventory");
        disableButton(sellButton, true);

        sellTotalLabel = new Label(SELL + " : " + tradeInVal + GP, Utility.STATUSUI_SKIN);
        sellTotalLabel.setAlignment(Align.center);
        buyTotalLabel = new Label(BUY + " : " + fullValue + GP, Utility.STATUSUI_SKIN);
        buyTotalLabel.setAlignment(Align.center);

        playerTotalGP = new Label(PLAYER_TOTAL + " : " + playerTotal + GP, Utility.STATUSUI_SKIN);

        buyButton = new TextButton(BUY, Utility.STATUSUI_SKIN, "inventory");
        disableButton(buyButton, true);

        closeButton = new TextButton("X", Utility.STATUSUI_SKIN);

        buttons = new Table();
        buttons.defaults().expand().fill();
        buttons.add(sellButton).padLeft(10).padRight(10);
        buttons.add(buyButton).padLeft(10).padRight(10);

        totalLabels = new Table();
        totalLabels.defaults().expand().fill();
        totalLabels.add(sellTotalLabel).padLeft(40);
        totalLabels.add();
        totalLabels.add(buyTotalLabel).padRight(40);

        //layout
        for (int i = 1; i <= numStoreInventorySlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
            inventorySlot.addObserver(this);
            inventorySlot.setName(STORE_INVENTORY);

            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            inventorySlotTable.add(inventorySlot).size(slotWidth, slotHeight);
            if (i % lengthSlotRow == 0) {
                inventorySlotTable.row();
            }
        }

        for (int i = 1; i <= InventoryUI._numSlots; i++) {
            InventorySlot inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(inventorySlotTooltip));
            inventorySlot.addObserver(this);
            inventorySlot.setName(PLAYER_INVENTORY);

            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            playerInventorySlotTable.add(inventorySlot).size(slotWidth, slotHeight);

            if (i % lengthSlotRow == 0) {
                playerInventorySlotTable.row();
            }
        }

        inventoryActors.add(inventorySlotTooltip);

        this.add();
        this.add(closeButton);
        this.row();

        //this.debugAll();
        this.defaults().expand().fill();
        this.add(inventorySlotTable).pad(10, 10, 10, 10).row();
        this.add(buttons).row();
        this.add(totalLabels).row();
        this.add(playerInventorySlotTable).pad(10, 10, 10, 10).row();
        this.add(playerTotalGP);
        this.pack();

        //Listeners
        buyButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (fullValue > 0 && playerTotal >= fullValue) {
                        playerTotal -= fullValue;
                        StoreInventoryUI.this.notify(
                            Integer.toString(playerTotal),
                            StoreInventoryObserver.StoreInventoryEvent.PLAYER_GP_TOTAL_UPDATED
                        );
                        fullValue = 0;
                        buyTotalLabel.setText(BUY + " : " + fullValue + GP);

                        checkButtonStates();

                        //Make sure we update the owner of the items
                        InventoryUtil.setInventoryItemNames(playerInventorySlotTable, PLAYER_INVENTORY);
                        savePlayerInventory();
                    }
                }
            }
        );

        sellButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (tradeInVal > 0) {
                        playerTotal += tradeInVal;
                        StoreInventoryUI.this.notify(
                            Integer.toString(playerTotal),
                            StoreInventoryObserver.StoreInventoryEvent.PLAYER_GP_TOTAL_UPDATED
                        );
                        tradeInVal = 0;
                        sellTotalLabel.setText(SELL + " : " + tradeInVal + GP);

                        checkButtonStates();

                        //Remove sold items
                        Array<Cell> cells = inventorySlotTable.getCells();
                        for (int i = 0; i < cells.size; i++) {
                            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
                            if (inventorySlot == null) continue;
                            if (inventorySlot.hasItem() &&
                                PLAYER_INVENTORY.equalsIgnoreCase(inventorySlot.getTopInventoryItem().getName())) {

                                inventorySlot.clearAllInventoryItems();
                            }
                        }
                        savePlayerInventory();
                    }
                }
            }
        );
    }

    public TextButton getCloseButton() {
        return closeButton;
    }

    public Table getInventorySlotTable() {
        return inventorySlotTable;
    }

    public Array<Actor> getInventoryActors() {
        return inventoryActors;
    }

    public void loadPlayerInventory(Array<InventoryItemLocation> playerInventoryItems) {
        InventoryUtil.populateInventory(playerInventorySlotTable, playerInventoryItems, dragAndDrop);
    }

    public void loadStoreInventory(Array<InventoryItemLocation> storeInventoryItems) {
        InventoryUtil.populateInventory(inventorySlotTable, storeInventoryItems, dragAndDrop);
    }

    public void savePlayerInventory() {
        Array<InventoryItemLocation> playerItemsInPlayerInventory = InventoryUtil.getInventory(playerInventorySlotTable, PLAYER_INVENTORY);
        Array<InventoryItemLocation> playerItemsInStoreInventory = InventoryUtil.getInventory(playerInventorySlotTable, inventorySlotTable, PLAYER_INVENTORY);
        playerItemsInPlayerInventory.addAll(playerItemsInStoreInventory);
        StoreInventoryUI.this.notify(MapperProvider.INSTANCE.writeValueAsString(playerItemsInPlayerInventory), StoreInventoryObserver.StoreInventoryEvent.PLAYER_INVENTORY_UPDATED);
    }

    public void cleanupStoreInventory() {
        InventoryUtil.removeInventoryItems(STORE_INVENTORY, playerInventorySlotTable);
        InventoryUtil.removeInventoryItems(PLAYER_INVENTORY, inventorySlotTable);
    }

    @Override
    public void onNotify(InventorySlot slot, SlotEvent event) {
        switch (event) {
            case ADDED_ITEM:
                //moving from player inventory to store inventory to sell
                if (slot.getTopInventoryItem().getName().equalsIgnoreCase(PLAYER_INVENTORY) &&
                    slot.getName().equalsIgnoreCase(STORE_INVENTORY)) {
                    tradeInVal += slot.getTopInventoryItem().getTradeValue();
                    sellTotalLabel.setText(SELL + " : " + tradeInVal + GP);
                }
                //moving from store inventory to player inventory to buy
                if (slot.getTopInventoryItem().getName().equalsIgnoreCase(STORE_INVENTORY) &&
                    slot.getName().equalsIgnoreCase(PLAYER_INVENTORY)) {
                    fullValue += slot.getTopInventoryItem().getItemValue();
                    buyTotalLabel.setText(BUY + " : " + fullValue + GP);
                }
                break;
            case REMOVED_ITEM:
                if (slot.getTopInventoryItem().getName().equalsIgnoreCase(PLAYER_INVENTORY) &&
                    slot.getName().equalsIgnoreCase(STORE_INVENTORY)) {
                    tradeInVal -= slot.getTopInventoryItem().getTradeValue();
                    sellTotalLabel.setText(SELL + " : " + tradeInVal + GP);
                }
                if (slot.getTopInventoryItem().getName().equalsIgnoreCase(STORE_INVENTORY) &&
                    slot.getName().equalsIgnoreCase(PLAYER_INVENTORY)) {
                    fullValue -= slot.getTopInventoryItem().getItemValue();
                    buyTotalLabel.setText(BUY + " : " + fullValue + GP);
                }
                break;
        }
        checkButtonStates();
    }

    public void checkButtonStates() {
        if (tradeInVal <= 0) {
            disableButton(sellButton, true);
        } else {
            disableButton(sellButton, false);
        }

        if (fullValue <= 0 || playerTotal < fullValue) {
            disableButton(buyButton, true);
        } else {
            disableButton(buyButton, false);
        }
    }

    public void setPlayerGP(int value) {
        playerTotal = value;
        playerTotalGP.setText(PLAYER_TOTAL + " : " + playerTotal + GP);
    }

    private void disableButton(Button button, boolean disable) {
        if (disable) {
            button.setDisabled(true);
            button.setTouchable(Touchable.disabled);
        } else {
            button.setDisabled(false);
            button.setTouchable(Touchable.enabled);
        }
    }

    @Override
    public void addObserver(StoreInventoryObserver storeObserver) {
        observers.add(storeObserver);
    }

    @Override
    public void removeObserver(StoreInventoryObserver storeObserver) {
        observers.removeValue(storeObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for (StoreInventoryObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(String value, StoreInventoryObserver.StoreInventoryEvent event) {
        for (StoreInventoryObserver observer : observers) {
            observer.onNotify(value, event);
        }
    }
}
