package com.mygdx.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that handles global Controller events. The main functionality
 * of this class will be notify about controller connection or disconnection.
 */
public class ControllerManager extends ControllerAdapter {
    private static ControllerManager ourInstance = new ControllerManager();

    public static ControllerManager getInstance() {
        return ourInstance;
    }

    public static void setControllerConnectionListener(ControllerConnectionListener listener) {
        ourInstance.setListener(listener);
    }

    public static List<Controller> getConnectedControllers() {
        return ourInstance.getConnectedControllersInternal();
    }

    private ControllerConnectionListener listener;
    private List<Controller> controllerList;

    private ControllerManager() {
        controllerList = new ArrayList<Controller>();
        for (Controller controller : Controllers.getControllers()) {
            connected(controller);
        }
        Controllers.addListener(this);
    }

    public void setListener(ControllerConnectionListener listener) {
        this.listener = listener;
    }

    public List<Controller> getConnectedControllersInternal() {
        return controllerList;
    }

    public void clearListener() {
        this.listener = null;
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

        if (i == controllerList.size()) {
            controllerList.add(newController);
        } else {
            controllerList.set(i, newController);
        }
        if (this.listener != null) {
            this.listener.onControllerConnected(i, newController);
        }
    }

    @Override
    public void disconnected (Controller disconnectedController) {
        int i = 0;
        for (; i < controllerList.size(); i++) {
            Controller controller = controllerList.get(i);
            if (controller != null && controller.equals(disconnectedController)) {
                controllerList.set(i, null);
                break;
            }
        }

        if (this.listener != null) {
            this.listener.onControllerDisconnected(i, disconnectedController);
        }
    }

    public interface ControllerConnectionListener {
        public void onControllerConnected(int port, Controller controller);

        public void onControllerDisconnected(int port, Controller controller);
    }
}
