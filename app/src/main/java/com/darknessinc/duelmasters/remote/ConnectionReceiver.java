package com.darknessinc.duelmasters.remote;

/**
 * Created by abrahamphilip on 2/1/16.
 */
public interface ConnectionReceiver {
    void receiveChatMessage(String msg);
    void receiveInstruction(String instruction);
    void onConnectionError();
}
