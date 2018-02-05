package com.mygdx.game.controller;

import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Class that implements the PlayerControllerAdapter but does not do any action.
 * Great for testing and simulating other controllers.
 */
public class DummyController implements PlayerController {

    private int port;

    public DummyController(int port) {
        this.port = port;
    }

    @Override
    public boolean getButton(int buttonCode) {
        return false;
    }

    @Override
    public float getAxis(int axisCode) {
        return 0;
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