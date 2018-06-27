package com.cramsan.demog1.subsystems;

public interface IGameSubsystem {

    void OnGameLoad();

    void OnScreenLoad();

    void OnLoopStart();

    void OnLoopEnd();

    void OnScreenClose();

    void OnGameClose();
}
