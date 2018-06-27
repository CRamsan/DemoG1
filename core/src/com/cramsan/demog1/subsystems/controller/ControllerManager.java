package com.cramsan.demog1.subsystems.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.subsystems.IGameSubsystem;
import com.cramsan.demog1.subsystems.ui.IUISystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ControllerManager extends ControllerAdapter implements IGameSubsystem {

    private static final float UI_WAIT = 1f;

    @Override
    public void OnGameLoad() {

    }

    @Override
    public void OnScreenLoad() {

    }

    @Override
    public void OnScreenClose() {

    }

    @Override
    public void OnGameClose() {

    }

    /**
     * Small class that wraps around an UI_EVENT and the controller
     * that it came from.
     */
    public class ControllerEventTuple {
        public IUISystem.UI_EVENTS event;
        public int index;

        public ControllerEventTuple(IUISystem.UI_EVENTS event, int index) {
            this.event = event;
            this.index = index;
        }
    }

    private ControllerConnectionListener listener;
    private List<PlayerController> controllerList;
    private HashMap<Controller, PlayerController> controllerMap;
    private HashMap<Integer, Float> blockedMap;
    private ArrayList<ControllerEventTuple> tupleList;

    public ControllerManager() {
        controllerList = new ArrayList<PlayerController>();
        controllerMap = new HashMap<Controller, PlayerController>();
        blockedMap = new HashMap<Integer, Float>();
        tupleList = new ArrayList<ControllerEventTuple>();
        addPlayerController(new KeyboardController());
        DummyController dummy = new DummyController(1);
        addPlayerController(dummy);
        controllerMap.put(dummy, dummy);
        for (Controller controller : Controllers.getControllers()) {
            connected(controller);
        }
        Controllers.addListener(this);
    }

    public void setControllerConnectionListener(ControllerConnectionListener listener) {
        this.listener = listener;
    }

    public Iterator<PlayerController> getConnectedControllersIterator() {
        return controllerList.iterator();
    }

    public void update(float delta) {
        tupleList.clear();
        for (PlayerController controller : controllerList) {
            IUISystem.UI_EVENTS event = IUISystem.UI_EVENTS.NOOP;

            if (controller == null) // An unplugged controller can leave a null controller
                continue;
            float dx = controller.getAxis(0);
            float dy = controller.getAxis(1);
            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);
            if (Vector2.len(absDx, absDy) > 0.2)
            {
                if (absDx > absDy) {
                    if (dx > 0) {
                        event = IUISystem.UI_EVENTS.RIGHT;
                    } else if (dx < 0) {
                        event = IUISystem.UI_EVENTS.LEFT;
                    }
                } else {
                    if (dy > 0) {
                        event = IUISystem.UI_EVENTS.UP;
                    } else if (dy < 0) {
                        event = IUISystem.UI_EVENTS.DOWN;
                    }
                }
            }

            boolean isSelected = controller.getButton(0);
            if (isSelected)
                event = IUISystem.UI_EVENTS.SELECT;

            if (blockedMap.containsKey(controller.getControllerIndex())) {
                // There was a UI event that is blocking other events
                // If the new event is NOOP then the event was released.
                // Otherwise wait for the timeout
                float waitBuffer = blockedMap.get(controller.getControllerIndex());
                if (event == IUISystem.UI_EVENTS.NOOP) {
                    blockedMap.remove(controller.getControllerIndex());
                } else {
                    // Wait until we reach the timeout.
                    waitBuffer += delta;
                    if (waitBuffer >= UI_WAIT && !isSelected) {
                        // Is the UI_EVENT is of type SELECT then do not trigger a UI event.
                        // For movement events we want to wait for the timeout so we can do things
                        // like scrolling. But we do not want to do that for selection otherwise
                        // we will send SELECT events that can cause the user to take action accidentally.
                        blockedMap.remove(controller.getControllerIndex());
                    } else {
                        blockedMap.put(controller.getControllerIndex(), waitBuffer);
                        event = IUISystem.UI_EVENTS.NOOP;
                    }
                }
            }

            if (event == IUISystem.UI_EVENTS.DOWN || event == IUISystem.UI_EVENTS.UP ||
                    event == IUISystem.UI_EVENTS.LEFT || event == IUISystem.UI_EVENTS.RIGHT ||
                    event == IUISystem.UI_EVENTS.SELECT) {
                blockedMap.put(controller.getControllerIndex(), 0f);
            }
            ControllerEventTuple tuple = new ControllerEventTuple(event, controller.getControllerIndex());
            tupleList.add(tuple);
        }
    }

    public List<ControllerEventTuple> getUIEvents () {
        return tupleList;
    }

    public void addPlayerController(PlayerController newController) {
        int index = newController.getControllerIndex();
        if (index == controllerList.size()) {
            controllerList.add(newController);
        } else {
            if (controllerList.get(index) == null)
                controllerList.set(index, newController);
            else
                throw new RuntimeException("Another controller is already set to this port");
        }
        if (this.listener != null) {
            this.listener.onControllerConnected(index, newController);
        }
    }

    @Override
    public void connected (Controller newController) {
        int i = 0;
        for (; i < controllerList.size(); i++) {
            Controller controller = controllerList.get(i);
            if (controller == null) {
                break;
            }
        }

        PlayerController newPlayerController = new ExternalController(newController, i);
        addPlayerController(newPlayerController);
        controllerMap.put(newController, newPlayerController);
    }

    @Override
    public void disconnected (Controller disconnectedController) {
        PlayerController controller = controllerMap.get(disconnectedController);
        if (controller == null)
            throw new RuntimeException("Controller at position was null");
        int index = controller.getControllerIndex();
        controllerList.set(index, null);

        if (this.listener != null) {
            this.listener.onControllerDisconnected(index, controller);
        }
    }
}
