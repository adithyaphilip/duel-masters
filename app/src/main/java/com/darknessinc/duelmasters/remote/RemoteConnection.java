package com.darknessinc.duelmasters.remote;

import android.os.Parcelable;

import com.darknessinc.duelmasters.feed.InstructionDecoder;

/**
 * @AUTHOR abrahamphilip
 */
public abstract class RemoteConnection implements Parcelable {
    private static final String CHAT_MESSAGE_INDICATOR = "!#";
    private ConnectionReceiver mReceiver;

    public void sendChatMessage(String msg) {
        String oppMsg = CHAT_MESSAGE_INDICATOR + msg;//starting P is token indicating message
        sendEncodedMessage(oppMsg);
    }

    public void sendInstruction(String ownInstruction) {
        sendEncodedMessage(InstructionDecoder.getInstructionToSend(ownInstruction));
    }

    public void setListener(ConnectionReceiver receiver) {
        mReceiver = receiver;
    }

    protected void receiveEncodedMessage(String encodedMsg) {
        if (isChatMessage(encodedMsg)) {
            mReceiver.receiveChatMessage(getChatMessage(encodedMsg));
        } else {
            mReceiver.receiveInstruction(encodedMsg);
        }
    }

    protected abstract void sendEncodedMessage(String encodedMessage);

    public boolean isChatMessage(String encodedMessage) {
        return encodedMessage.startsWith(CHAT_MESSAGE_INDICATOR);
    }

    public String getChatMessage(String encodedMsg) {
        if(!isChatMessage(encodedMsg)) {
            throw new UnsupportedOperationException("Invalid encoding for chat message: "
                    + encodedMsg);
        }
        return encodedMsg.substring(CHAT_MESSAGE_INDICATOR.length());
    }

    public abstract void onActivityPaused();

    public abstract void onActivityResumed();
}
