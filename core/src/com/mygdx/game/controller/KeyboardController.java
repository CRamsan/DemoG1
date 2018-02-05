package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Class that exposes the keyboard as a controller by implementing the PlayerControllerAdapter
 * interface.
 */
public class KeyboardController implements PlayerController {

    @Override
    public boolean getButton(int buttonCode) {
        switch (buttonCode) {
            case 0:
                return Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
            case 1:
                return Gdx.input.isKeyPressed(Input.Keys.DOWN);
            case 2:
                return Gdx.input.isKeyPressed(Input.Keys.LEFT);
            case 3:
                return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            case 4:
                return Gdx.input.isKeyPressed(Input.Keys.UP);
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
        return 0;
    }
}