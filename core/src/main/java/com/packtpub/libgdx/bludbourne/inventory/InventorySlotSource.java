package com.packtpub.libgdx.bludbourne.inventory;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

/**
 * Created on 04/27/2017.
 */
public class InventorySlotSource extends Source {

    private DragAndDrop dragAndDrop;
    private InventorySlot sourceSlot;

    public InventorySlotSource(InventorySlot sourceSlot, DragAndDrop dragAndDrop) {
        super(sourceSlot.getTopInventoryItem());
        this.sourceSlot = sourceSlot;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        Payload payload = new Payload();

        sourceSlot = (InventorySlot) getActor().getParent();
        sourceSlot.decrementItemCount();

        payload.setDragActor(getActor());
        dragAndDrop.setDragActorPosition(-x + getActor().getWidth(), -y - getActor().getHeight() / 2);

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        if (target == null) {
            sourceSlot.add(payload.getDragActor());
        }
    }

    public InventorySlot getSourceSlot() {
        return sourceSlot;
    }
}
