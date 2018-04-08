package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Class that exposes the keyboard as a controller by implementing the PlayerController and InputProcessor
 * interface.
 */
public class KeyboardController implements PlayerController, InputProcessor {

    private ControllerListener keyboardListener;

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
            case 7:
                return Gdx.input.isKeyPressed(Input.Keys.P);
            default:
                return false;
        }
    }

    @Override
    public float getAxis(int axisCode) {
        switch (axisCode) {
            case 0:
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    return -1;
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    return 1;
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
        if (supportsEvents()) {
            keyboardListener = listener;
            Gdx.input.setInputProcessor(this);
        } else {
            throw new RuntimeException("This class does not support listeners");
        }
    }

    @Override
    public void removeListener(ControllerListener listener) {
        if (supportsEvents()) {
            Gdx.input.setInputProcessor(null);
            keyboardListener = null;
        } else {
            throw new RuntimeException("This class does not support listeners");
        }
    }

    @Override
    public boolean supportsEvents() {
        return true;
    }

    @Override
    public int getControllerIndex() {
        return 0;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keyboardListener == null)
            return false;

        switch (keycode){
            case Input.Keys.LEFT:
                return keyboardListener.axisMoved(this,0, -1);
            case Input.Keys.RIGHT:
                return keyboardListener.axisMoved(this,0, 1);
            case Input.Keys.UP:
                return keyboardListener.axisMoved(this,1, -1);
            case Input.Keys.DOWN:
                return keyboardListener.axisMoved(this,1, 1);
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                return keyboardListener.buttonDown(this, 0);
            case Input.Keys.BACKSPACE:
                return keyboardListener.buttonDown(this, 5);
            case Input.Keys.P:
                return keyboardListener.buttonDown(this, 7);
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keyboardListener == null)
            return false;

        switch (keycode){
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
                return keyboardListener.axisMoved(this,0, 0);
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                return keyboardListener.axisMoved(this,1, 0);
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                return keyboardListener.buttonUp(this, 0);
            case Input.Keys.BACKSPACE:
                return keyboardListener.buttonUp(this, 5);
            case Input.Keys.P:
                return keyboardListener.buttonUp(this, 7);
            default:
                return false;
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}