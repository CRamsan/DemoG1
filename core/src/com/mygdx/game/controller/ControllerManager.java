package com.mygdx.game.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton class that handles global Controller events. The main functionality
 * of this class will be notify about controller connection or disconnection. This class
 * will handle Controller objects and expose them through the PlayerControllerAdapter interface.
 */
public class ControllerManager extends ControllerAdapter {

    private static ControllerManager ourInstance = new ControllerManager();

    public static ControllerManager getInstance() {
        return ourInstance;
    }

    public static void setControllerConnectionListener(ControllerConnectionListener listener) {
        ourInstance.setListener(listener);
    }

    public static List<PlayerController> getConnectedControllers() {
        return ourInstance.getConnectedControllersInternal();
    }

    private ControllerConnectionListener listener;
    private List<PlayerController> controllerList;
    private HashMap<Controller, PlayerController> controllerMap;
    private float waitBuffer;
    private boolean blockInput;

    private ControllerManager() {
        controllerList = new ArrayList<PlayerController>();
        controllerMap = new HashMap<Controller, PlayerController>();
        for (Controller controller : Controllers.getControllers()) {
            connected(controller);
        }
        Controllers.addListener(this);
        addPlayerController(new KeyboardController());
        addPlayerController(new DummyController(1));
    }



    public void setListener(ControllerConnectionListener listener) {
        this.listener = listener;
    }

    public List<PlayerController> getConnectedControllersInternal() {
        return controllerList;
    }

    public Globals.UI_EVENTS getNextUIEvent (float delta) {

        Globals.UI_EVENTS event = Globals.UI_EVENTS.NOOP;

        for (PlayerController controller : controllerList) {
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
                        event = Globals.UI_EVENTS.RIGHT;
                    } else {
                        event = Globals.UI_EVENTS.LEFT;
                    }
                } else {
                    if (dy > 0) {
                        event = Globals.UI_EVENTS.UP;
                    } else {
                        event = Globals.UI_EVENTS.DOWN;
                    }
                }
            }

            boolean isSelected = controller.getButton(0);
            if (isSelected)
                event = Globals.UI_EVENTS.SELECT;
        }

        if (blockInput) {
            // There was a UI event that is blocking other events
            // If the new event is NOOP then the event was released.
            // Otherwise wait for the timeout
            if (event == Globals.UI_EVENTS.NOOP) {
                blockInput = false;
            } else {
                // Wait until we reach the timeout.
                waitBuffer += delta;
                if (waitBuffer >= Globals.UI_WAIT) {
                    blockInput = false;
                    waitBuffer = 0;
                } else {
                    return Globals.UI_EVENTS.NOOP;
                }
            }
        }

        if (event == Globals.UI_EVENTS.DOWN || event == Globals.UI_EVENTS.UP ||
                event == Globals.UI_EVENTS.LEFT || event == Globals.UI_EVENTS.RIGHT ||
                event == Globals.UI_EVENTS.SELECT) {
            blockInput = true;
            waitBuffer = 0;
        }
        return event;
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
