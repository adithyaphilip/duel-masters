package com.darknessinc.duelmasters.remote;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author abrahamphilip on 3/1/16.
 */
public class LanConnection extends RemoteConnection implements Parcelable {

    final String REMOTE_ADDR;
    final int REMOTE_PORT;
    final int LOCAL_PORT;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LanConnection.super.receiveEncodedMessage((String) msg.obj);
        }
    };

    Queue<String> mQueue = new LinkedList<>();

    // To prevent the queue from being added to while the check is being performed
    final private Object mSenderLock = new Object();
    AtomicBoolean mSenderActive = new AtomicBoolean(false);

    private class SenderThread extends Thread {
        @Override
        public void run() {
            mSenderActive.set(true);
            while (true) {
                synchronized (mSenderLock) {
                    if (mQueue.isEmpty()) {
                        mSenderActive.set(false);
                        break;
                    }
                }
                try {
                    sendToRemote(mQueue.peek());
                    String msg = mQueue.remove();
                    Log.d("LanConnection", "Successfully sent msg to remote: " + msg);
                } catch (IOException e) {
                    Log.e("LanConnection",
                            "Error connecting to " + REMOTE_ADDR + ":" + REMOTE_PORT);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        // ignore
                    }
                }
            }
        }

        private void sendToRemote(String encodedMsg) throws IOException {
            Socket s = new Socket(REMOTE_ADDR, REMOTE_PORT);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.print(encodedMsg);
            pw.close();
            s.close();
        }
    }

    private ServerSocket mListenerSocket;

    public LanConnection(int localPort, String remoteAddr, int remotePort) {
        REMOTE_ADDR = remoteAddr;
        REMOTE_PORT = remotePort;
        LOCAL_PORT = localPort;
    }

    /**
     * starts the thread that actually listens, assumed super.setListener has already been called
     */
    private void resumeListening() {
        Thread listener = new Thread() {
            @Override
            public void run() {
                try {
                    mListenerSocket = new ServerSocket();
                    mListenerSocket.setReuseAddress(true);
                    mListenerSocket.bind(new InetSocketAddress(LOCAL_PORT));
                    while (true) {
                        Socket connSocket = mListenerSocket.accept();
                        Scanner sc = new Scanner(connSocket.getInputStream());
                        String encMsg = sc.nextLine();
                        connSocket.close();
                        sc.close();
                        Message msg = mHandler.obtainMessage();
                        msg.obj = encMsg;
                        mHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    Log.d("LANConnection", "Stopping listener thread due to error", e);
                }
            }
        };
        listener.start();
    }

    public void stopListening() {
        // thread will die with exception when closed
        try {
            mListenerSocket.close();
        } catch (IOException e) {
            // just log
            Log.e("LanConnection","Error closing listener socket at " + LOCAL_PORT, e);
        }
    }

    @Override
    protected void sendEncodedMessage(String encodedMessage) {
        synchronized (mSenderLock) {
            mQueue.add(encodedMessage);
            if (!mSenderActive.get()) {
                new SenderThread().start();
            }
        }
    }

    @Override
    public void onActivityPaused() {
        stopListening();
    }

    @Override
    public void onActivityResumed() {
        resumeListening();
    }

    public LanConnection(Parcel in) {
        REMOTE_ADDR = in.readString();
        REMOTE_PORT = in.readInt();
        LOCAL_PORT = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(REMOTE_ADDR);
        dest.writeInt(REMOTE_PORT);
        dest.writeInt(LOCAL_PORT);
    }

    public static Creator<LanConnection> CREATOR = new Creator<LanConnection>() {
        @Override
        public LanConnection createFromParcel(Parcel source) {
            return new LanConnection(source);
        }

        @Override
        public LanConnection[] newArray(int size) {
            return new LanConnection[size];
        }
    };
}
