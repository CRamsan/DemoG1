package com.cramsan.demog1.subsystems.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.*;

/**
 * Class that implements the PlayerController but does not do any action.
 * Great for testing and simulating other controllers.
 */
public class DummyController implements PlayerController, ControllerListener {

    private int port;
    private boolean handleEvents;
    private Map<Integer, Boolean> buttonState;
    private Map<Integer, Float> axisState;
    private Set<ControllerListener> listeners;

    public DummyController(int port) {
        this(port, false);
    }

    public DummyController(int port, boolean handleEvents) {
        this.port = port;
        this.buttonState = new HashMap<Integer, Boolean>();
        this.axisState = new HashMap<Integer, Float>();
        this.handleEvents = handleEvents;
        this.listeners = new HashSet<ControllerListener>();
    }

    @Override
    public boolean getButton(int buttonCode) {
        if (!buttonState.containsKey(buttonCode))
            return false;
        return buttonState.get(buttonCode);
    }

    public void setButton(int buttonCode, boolean state) {
        buttonState.put(buttonCode, state);
    }

    @Override
    public float getAxis(int axisCode) {
        if (!axisState.containsKey(axisCode))
            return 0;
        return axisState.get(axisCode);
    }

    public void setAxis(int axisCode, float value) {
        axisState.put(axisCode, value);
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
        return "Dummy";
    }

    @Override
    public void addListener(ControllerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ControllerListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public boolean supportsEvents() {
        return handleEvents;
    }

    @Override
    public int getControllerIndex() {
        return port;
    }

    public void setHandleEvents(boolean handleEvents) {
        this.handleEvents = handleEvents;
    }

    @Override
    public void connected(Controller controller) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void disconnected(Controller controller) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (!this.handleEvents)
            throw new RuntimeException("This controller is set to handle events");
        for (ControllerListener listener : listeners) {
            listener.buttonDown(controller, buttonCode);
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (!this.handleEvents)
            throw new RuntimeException("This controller is set to handle events");
        for (ControllerListener listener : listeners) {
            listener.buttonUp(controller, buttonCode);
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (!this.handleEvents) throw new RuntimeException("This controller is set to handle events");
        for (ControllerListener listener : listeners) {
            listener.axisMoved(controller, axisCode, value);
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        throw new RuntimeException("Not Implemented");
    }
}