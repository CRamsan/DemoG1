package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.*;
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
    private KeyboardController keyboardController;

    private ControllerManager() {
        controllerList = new ArrayList<Controller>();
        for (Controller controller : Controllers.getControllers()) {
            connected(controller);
        }
        Controllers.addListener(this);
        keyboardController = new KeyboardController();
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

    public Globals.UI_EVENTS getNextUIEvent () {

        Controller controller = keyboardController;
        int index = 0;
        while (controller != null) {
            float dx = controller.getAxis(0);
            float dy = controller.getAxis(1);
            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);
            if (absDx == 0 && absDy == 0) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){

                }
            } else {
                if (absDx > absDy) {
                    if (dx > 0) {
                        return Globals.UI_EVENTS.RIGHT;
                    } else {
                        return Globals.UI_EVENTS.LEFT;
                    }
                } else {
                    if (dy > 0) {
                        return Globals.UI_EVENTS.UP;
                    } else {
                        return Globals.UI_EVENTS.DOWN;
                    }
                }
            }

            boolean isSelected = controller.getButton(4);
            if (isSelected)
                return Globals.UI_EVENTS.SELECT;

            if (index < controllerList.size()){
                controller = controllerList.get(index);
            } else {
                break;
            }
            index++;
        }
        return Globals.UI_EVENTS.NOOP;
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

    public class KeyboardController implements Controller{
        @Override
        public boolean getButton(int buttonCode) {
            switch (buttonCode) {
                case 0:
                    return Gdx.input.isKeyPressed(Input.Keys.UP);
                case 1:
                    return Gdx.input.isKeyPressed(Input.Keys.DOWN);
                case 2:
                    return Gdx.input.isKeyPressed(Input.Keys.LEFT);
                case 3:
                    return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
                case 4:
                    return Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
                case 5:
                    return Gdx.input.isKeyPressed(Input.Keys.BACKSPACE);
                default:
                    throw new RuntimeException("Not Implemented");
            }
        }

        @Override
        public float getAxis(int axisCode) {
            switch (axisCode) {
                case 0:

                    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        return 1;
                    else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        return -1;
                    else
                        return 0;
                case 1:
                    if (Gdx.input.isKeyPressed(Input.Keys.UP))
                        return 1;
                    else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                        return -1;
                    else
                        return 0;
                default:
                    throw new RuntimeException("Not Implemented");
            }
        }

        @Override
        public PovDirection getPov(int povCode) {
            throw new RuntimeException("Not Implemented");
        }

        @Override
        public boolean getSliderX(int sliderCode) {
            throw new RuntimeException("Not Implemented");
        }

        @Override
        public boolean getSliderY(int sliderCode) {
            throw new RuntimeException("Not Implemented");
        }

        @Override
        public Vector3 getAccelerometer(int accelerometerCode) {
            throw new RuntimeException("Not Implemented");
        }

        @Override
        public void setAccelerometerSensitivity(float sensitivity) {
            throw new RuntimeException("Not Implemented");
        }

        @Override
        public String getName() {
            return "Keyboard";
        }

        @Override
        public void addListener(ControllerListener listener) {
            throw new RuntimeException("Not Implemented");
        }

        @Override
        public void removeListener(ControllerListener listener) {
            throw new RuntimeException("Not Implemented");
        }
    }
}
