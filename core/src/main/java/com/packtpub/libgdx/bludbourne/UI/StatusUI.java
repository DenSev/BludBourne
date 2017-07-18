package com.packtpub.libgdx.bludbourne.UI;

/**
 * Created on 04/27/2017.
 */

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.observers.StatusObserver;
import com.packtpub.libgdx.bludbourne.observers.StatusSubject;
import com.packtpub.libgdx.bludbourne.utility.Utility;

public class StatusUI extends Window implements StatusSubject {

    private ImageButton inventoryButton;
    Array<StatusObserver> observers;

    //Attributes
    private int levelVal = 1;
    private int goldVal = -1;

    private Label goldValLabel;

    public StatusUI() {
        super("stats", Utility.STATUSUI_SKIN);
        observers = new Array<StatusObserver>();

        //groups
        WidgetGroup group = new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        //images
        Image hpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("HP_Bar"));
        Image bar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        Image mpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("MP_Bar"));
        Image bar2 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        Image xpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("XP_Bar"));
        Image bar3 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));

        //labels
        Label hpLabel = new Label(" hp:", Utility.STATUSUI_SKIN);
        int hpVal = 50;
        Label hpValLabel = new Label(String.valueOf(hpVal), Utility.STATUSUI_SKIN);
        Label mpLabel = new Label(" mp:", Utility.STATUSUI_SKIN);
        int mpVal = 50;
        Label mpValLabel = new Label(String.valueOf(mpVal), Utility.STATUSUI_SKIN);
        Label xpLabel = new Label(" xp:", Utility.STATUSUI_SKIN);
        int xpVal = 0;
        Label xpValLabel = new Label(String.valueOf(xpVal), Utility.STATUSUI_SKIN);
        Label levelLabel = new Label(" lv:", Utility.STATUSUI_SKIN);
        Label levelValLabel = new Label(String.valueOf(this.levelVal), Utility.STATUSUI_SKIN);
        Label goldLabel = new Label(" gp:", Utility.STATUSUI_SKIN);
        goldValLabel = new Label(String.valueOf(this.goldVal), Utility.STATUSUI_SKIN);

        //buttons
        inventoryButton = new ImageButton(Utility.STATUSUI_SKIN, "inventory-button");
        inventoryButton.getImageCell().size(32, 32);

        //Align images
        hpBar.setPosition(3, 6);
        mpBar.setPosition(3, 6);
        xpBar.setPosition(3, 6);

        //add to widget groups
        group.addActor(bar);
        group.addActor(hpBar);
        group2.addActor(bar2);
        group2.addActor(mpBar);
        group3.addActor(bar3);
        group3.addActor(xpBar);

        //Add to layout
        defaults().expand().fill();

        //account for the title padding
        this.pad(this.getPadTop() + 10, 10, 10, 10);

        this.add();
        this.add();
        this.add(inventoryButton).align(Align.right);
        this.row();

        this.add(group).size(bar.getWidth(), bar.getHeight());
        this.add(hpLabel);
        this.add(hpValLabel).align(Align.left);
        this.row();

        this.add(group2).size(bar2.getWidth(), bar2.getHeight());
        this.add(mpLabel);
        this.add(mpValLabel).align(Align.left);
        this.row();

        this.add(group3).size(bar3.getWidth(), bar3.getHeight());
        this.add(xpLabel);
        this.add(xpValLabel).align(Align.left);
        this.row();

        this.add(levelLabel).align(Align.left);
        this.add(levelValLabel).align(Align.left);
        this.row();
        this.add(goldLabel);
        this.add(goldValLabel).align(Align.left);

        this.debug();
        this.pack();
    }

    public ImageButton getInventoryButton() {
        return inventoryButton;
    }


    public int getGoldValue() {
        return goldVal;
    }

    public void setGoldValue(int goldValue) {
        this.goldVal = goldValue;
        goldValLabel.setText(String.valueOf(goldVal));
        notify(goldValue, StatusObserver.StatusEvent.UPDATED_GP);
    }

    @Override
    public void addObserver(StatusObserver statusObserver) {
        observers.add(statusObserver);
    }

    @Override
    public void removeObserver(StatusObserver statusObserver) {
        observers.removeValue(statusObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for (StatusObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(int value, StatusObserver.StatusEvent event) {
        for (StatusObserver observer : observers) {
            observer.onNotify(value, event);
        }
    }
}

