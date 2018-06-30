package com.cramsan.demog1.subsystems.controller;

import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that implements the PlayerController but does not do any action.
 * Great for testing and simulating other controllers.
 */
public class DummyController implements PlayerController {

    private int port;
    private Map<Integer, Boolean> buttonState;
    private Map<Integer, Float> axisState;

    public DummyController(int port) {
        this.port = port;
        buttonState = new HashMap<Integer, Boolean>();
        axisState = new HashMap<Integer, Float>();
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

    public void clearInputs() {
        axisState.clear();
        buttonState.clear();
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
        throw new RuntimeException("This class does not support listeners");
    }

    @Override
    public void removeListener(ControllerListener listener) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    @Override
    public int getControllerIndex() {
        return port;
    }
}