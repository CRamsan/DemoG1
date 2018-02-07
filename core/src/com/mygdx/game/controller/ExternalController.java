package com.mygdx.game.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Public class that wraps around the native libgdx Controller
 * This method expands with controller by implementing the PlayerControllerAdapter interface
 * and an index that represents the port of the controller.
 */
public class ExternalController implements PlayerController {
    private Controller controller;
    private int index;

    public ExternalController(Controller controller, int index) {
        this.controller = controller;
        this.index = index;
    }

    @Override
    public boolean supportsEvents() {
        return true;
    }

    @Override
    public int getControllerIndex() {
    return index;
    }

    @Override
    public boolean getButton(int buttonCode) {
        return controller.getButton(buttonCode);
    }

    @Override
    public float getAxis(int axisCode) {
        float modifier = 1f;
        if (axisCode == 1)
            modifier = -1f;
        return controller.getAxis(axisCode) * modifier;
    }

    @Override
    public PovDirection getPov(int povCode) {
        return controller.getPov(povCode);
    }

    @Override
    public boolean getSliderX(int sliderCode) {
        return controller.getSliderX(sliderCode);
    }

    @Override
    public boolean getSliderY(int sliderCode) {
        return controller.getSliderY(sliderCode);
    }

    @Override
    public Vector3 getAccelerometer(int accelerometerCode) {
        return controller.getAccelerometer(accelerometerCode);
    }

    @Override
    public void setAccelerometerSensitivity(float sensitivity) {
        controller.setAccelerometerSensitivity(sensitivity);
    }

    @Override
    public String getName() {
        return controller.getName();
    }

    @Override
    public void addListener(ControllerListener listener) {
        controller.addListener(listener);
    }

    @Override
    public void removeListener(ControllerListener listener) {
        controller.removeListener(listener);
    }
}