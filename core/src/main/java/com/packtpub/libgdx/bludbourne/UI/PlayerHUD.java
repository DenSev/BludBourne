package com.packtpub.libgdx.bludbourne.UI;


import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.packtpub.libgdx.bludbourne.conversation.ConversationGraph;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityConfig;
import com.packtpub.libgdx.bludbourne.inventory.InventoryItemLocation;
import com.packtpub.libgdx.bludbourne.inventory.ItemTypeID;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import com.packtpub.libgdx.bludbourne.observers.ConversationGraphObserver;
import com.packtpub.libgdx.bludbourne.observers.EntityObserver;
import com.packtpub.libgdx.bludbourne.observers.ProfileObserver;
import com.packtpub.libgdx.bludbourne.observers.StatusObserver;
import com.packtpub.libgdx.bludbourne.observers.StoreInventoryObserver;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import com.packtpub.libgdx.bludbourne.utility.InventoryUtil;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/27/2017.
 */
public class PlayerHUD extends ScreenAdapter implements ProfileObserver, EntityObserver, ConversationGraphObserver, StoreInventoryObserver, StatusObserver {
    private final static Logger LOGGER = LoggerFactory.getLogger(PlayerHUD.class);

    private Stage stage;
    private Viewport viewport;
    private StatusUI statusUI;
    private InventoryUI inventoryUI;
    private Camera camera;
    private Entity player;


    private ConversationUI conversationUI;
    private StoreInventoryUI storeInventoryUI;


    public PlayerHUD(Camera camera, Entity player) {
        this.camera = camera;
        this.player = player;
        viewport = new ScreenViewport(this.camera);
        stage = new Stage(viewport);
        //stage.setDebugAll(true);

        statusUI = new StatusUI();
        statusUI.setVisible(true);
        statusUI.setPosition(0, 0);

        inventoryUI = new InventoryUI();
        inventoryUI.setMovable(false);
        inventoryUI.setVisible(false);
        inventoryUI.setPosition(stage.getWidth() / 2, 0);
        conversationUI = new ConversationUI();
        conversationUI.setMovable(true);
        conversationUI.setVisible(false);
        conversationUI.setPosition(stage.getWidth() / 2, 0);
        conversationUI.setWidth(stage.getWidth() / 2);
        conversationUI.setHeight(stage.getHeight() / 2);

        storeInventoryUI = new StoreInventoryUI();
        storeInventoryUI.setMovable(false);
        storeInventoryUI.setVisible(false);
        storeInventoryUI.setPosition(0, 0);

        stage.addActor(statusUI);
        stage.addActor(inventoryUI);
        stage.addActor(conversationUI);
        stage.addActor(storeInventoryUI);

        //add tooltips to the stage
        Array<Actor> actors = inventoryUI.getInventoryActors();
        for (Actor actor : actors) {
            stage.addActor(actor);
        }

        Array<Actor> storeActors = storeInventoryUI.getInventoryActors();
        for (Actor actor : storeActors) {
            stage.addActor(actor);
        }
        //Observers
        ProfileManager.INSTANCE.addObserver(this);
        player.registerObserver(this);
        statusUI.addObserver(this);
        storeInventoryUI.addObserver(this);

        //Listeners
        ImageButton inventoryButton = statusUI.getInventoryButton();
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                LOGGER.info("inventory clicked");
                inventoryUI.setVisible(!inventoryUI.isVisible());
            }
        });


        conversationUI.getCloseButton().addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    conversationUI.setVisible(false);
                    MapManager.INSTANCE.clearCurrentSelectedMapEntity();
                }
            }
        );

        storeInventoryUI.getCloseButton().addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    storeInventoryUI.savePlayerInventory();
                    storeInventoryUI.cleanupStoreInventory();
                    storeInventoryUI.setVisible(false);
                    MapManager.INSTANCE.clearCurrentSelectedMapEntity();
                }
            }
        );
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch (event) {
            case PROFILE_LOADED:
                //if goldval is negative, this is our first save
                int goldVal = profileManager.getProperty("currentPlayerGP", Integer.class);
                boolean firstTime = goldVal < 0 ? true : false;

                if (firstTime) {
                    //add default items if nothing is found
                    Array<ItemTypeID> items = player.getEntityConfig().getInventory();
                    Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();
                    for (int i = 0; i < items.size; i++) {
                        itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1));
                    }
                    InventoryUtil.populateInventory(inventoryUI.getInventorySlotTable(), itemLocations, inventoryUI.getDragAndDrop());
                    profileManager.setProperty("playerInventory", InventoryUtil.getInventory(inventoryUI.getInventorySlotTable()));
                }

                Array<InventoryItemLocation> inventory = profileManager.getProperty("playerInventory", Array.class);
                InventoryUtil.populateInventory(inventoryUI.getInventorySlotTable(), inventory, inventoryUI.getDragAndDrop());


                Array<InventoryItemLocation> equipInventory = profileManager.getProperty("playerEquipInventory", Array.class);
                if (equipInventory != null && equipInventory.size > 0) {
                    InventoryUtil.populateInventory(inventoryUI.getEquipSlotTable(), equipInventory, inventoryUI.getDragAndDrop());
                }

                //Check gold
                if (firstTime) {
                    //start the player with some money
                    goldVal = 20;
                }
                statusUI.setGoldValue(goldVal);


                break;
            case SAVING_PROFILE:
                profileManager.setProperty("playerInventory", InventoryUtil.getInventory(inventoryUI.getInventorySlotTable()));
                profileManager.setProperty("playerEquipInventory", InventoryUtil.getInventory(inventoryUI.getEquipSlotTable()));
                profileManager.setProperty("currentPlayerGP", statusUI.getGoldValue());

                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(EntityConfig config, EntityObserver.ComponentEvent event) {
        switch (event) {
            case LOAD_CONVERSATION:

                conversationUI.loadConversation(config);
                conversationUI.getCurrentConversationGraph().addObserver(this);
                break;
            case SHOW_CONVERSATION:

                if (config.getEntityID().equalsIgnoreCase(conversationUI.getCurrentEntityID())) {
                    conversationUI.setVisible(true);
                }
                break;
            case HIDE_CONVERSATION:

                if (config.getEntityID().equalsIgnoreCase(conversationUI.getCurrentEntityID())) {
                    conversationUI.setVisible(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(ConversationGraph graph, ConversationCommandEvent event) {
        LOGGER.error("received notification for {} and {}", graph.toString(), event.toString());
        switch (event) {
            case LOAD_STORE_INVENTORY:
                Entity selectedEntity = MapManager.INSTANCE.getCurrentSelectedMapEntity();
                if (selectedEntity == null) {
                    break;
                }

                Array<InventoryItemLocation> inventory = InventoryUtil.getInventory(inventoryUI.getInventorySlotTable());
                storeInventoryUI.loadPlayerInventory(inventory);

                Array<ItemTypeID> items = selectedEntity.getEntityConfig().getInventory();
                Array<InventoryItemLocation> itemLocations = new Array<InventoryItemLocation>();
                for (int i = 0; i < items.size; i++) {
                    itemLocations.add(new InventoryItemLocation(i, items.get(i).toString(), 1));
                }

                storeInventoryUI.loadStoreInventory(itemLocations);

                conversationUI.setVisible(false);
                storeInventoryUI.toFront();
                storeInventoryUI.setVisible(true);
                break;
            case EXIT_CONVERSATION:
                conversationUI.setVisible(false);
                MapManager.INSTANCE.clearCurrentSelectedMapEntity();
                break;
            case NONE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(String value, StoreInventoryEvent event) {
        switch (event) {

            //TODO kill hobos

            case PLAYER_GP_TOTAL_UPDATED:
                int val = Integer.valueOf(value);
                statusUI.setGoldValue(val);
                break;
            case PLAYER_INVENTORY_UPDATED:
                Array<InventoryItemLocation> items = MapperProvider.INSTANCE.parse(Array.class, value);
                InventoryUtil.populateInventory(inventoryUI.getInventorySlotTable(), items, inventoryUI.getDragAndDrop());
                break;
            default:
                break;
        }
    }

    @Override
    public void onNotify(int value, StatusEvent event) {
        switch (event) {
            case UPDATED_GP:
                storeInventoryUI.setPlayerGP(value);
                break;
            default:
                break;
        }
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

}
