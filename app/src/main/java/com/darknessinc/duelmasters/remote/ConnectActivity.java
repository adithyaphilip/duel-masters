package com.darknessinc.duelmasters.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.activity.GameActivity;
import com.darknessinc.duelmasters.cards.DatabaseCardRetriever;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.player.Player;

public class ConnectActivity extends Activity {

    private final int DM_SERVER_PORT = 6989;
    private final int DM_GAME_SERVER_PORT = 6990;

    private String remoteIp = null;

    ArrayList<GameCard> mOwnCards;
    ArrayList<GameCard> mOpponentCards;
    DatabaseCardRetriever mDBRetriever;
    public static final String KEY_OWN_CARDS = "owncards";

    private Handler mStartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startGame();
        }
    };

    private AtomicBoolean mSearching = new AtomicBoolean(false);

    Thread mListener = new Thread() {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(DM_SERVER_PORT);
                while (true) {
                    Socket connSocket = serverSocket.accept();
                    if (mSearching.get()) {
                        connSocket.close();
                        continue;
                    }

                    remoteIp = connSocket.getInetAddress().getHostAddress();

                    Scanner sc = new Scanner(connSocket.getInputStream());
                    String cardsString = sc.nextLine();
                    mOpponentCards = getOpponentCards(cardsString);

                    PrintWriter pw = new PrintWriter(connSocket.getOutputStream());
                    pw.println(CardEncoder.getCardsString(mOwnCards));
                    pw.flush();

                    // depends on the client socket to close the connection socket
                    serverSocket.close();
                    mStartHandler.sendEmptyMessage(0);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mDBRetriever = new DatabaseCardRetriever(this);
        mOwnCards = (ArrayList<GameCard>) getIntent().getSerializableExtra(KEY_OWN_CARDS);
        mListener.start();
    }

    private void startGame() {
        Intent i = new Intent(this, GameActivity.class);
        int player1Id = 1;
        Player p1 = new Player(player1Id, "Player1", mOpponentCards);

        int player2Id = 2;
        Player p2 = new Player(player2Id, "Player2", mOwnCards);

        i.putExtra(GameActivity.KEY_PLAYER1, p1);
        i.putExtra(GameActivity.KEY_PLAYER2, p2);
        i.putExtra(GameActivity.INTENT_KEY_CONNECTION, new LanConnection(DM_GAME_SERVER_PORT,
                remoteIp, DM_GAME_SERVER_PORT));

        startActivity(i);
    }

    /**
     * changes cardids to reflect opponent ids
     *
     * @param message encoded card list as returned by CardEncoder
     * @return decoded list of opponent's card with corrected ids
     */
    private ArrayList<GameCard> getOpponentCards(String message) {
        ArrayList<GameCard> gcs = CardEncoder.getGameCards(message, mDBRetriever);
        for (int i = 0; i < gcs.size(); i++) {
            GameCard gc = gcs.get(i);
            Log.d("getOpponentCards", "" + gcs.get(i).getGameId());
            gc.setGameId(gc.getGameId() / 10 * 10 + 1);
            Log.d("getOpponentCards", "" + gcs.get(i).getGameId());
        }
        return gcs;
    }

    /**
     * xml click function to search for devices
     */
    public void onSearchClick(View v) {
        remoteIp = null;
        mSearching.set(true);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Scouring the battlefield for blood..");
        pd.setCancelable(false);
        pd.show();

        final Handler progressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // search finished
                pd.dismiss();
                if (remoteIp == null) {
                    Toast.makeText(ConnectActivity.this, "No one dares battle thee :(",
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        Thread th = new Thread() {
            @Override
            public void run() {

                final ExecutorService executorService = Executors.newCachedThreadPool();
                final int startingIp = (getOwnIp() >> 8) << 8;

                Log.d("ConnectAcitivity", "own ip: " + getOwnIp());

                for (int i = startingIp; i - startingIp < 256; i++) {
                    final String ip = intToIp(i);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("ConnectActivity", "Connecting to " + ip);

                                Socket connSocket = new Socket(ip, DM_SERVER_PORT);

                                remoteIp = connSocket.getInetAddress().getHostAddress();

                                PrintWriter pw = new PrintWriter(connSocket.getOutputStream());
                                pw.println(CardEncoder.getCardsString(mOwnCards));
                                pw.flush();

                                Scanner sc = new Scanner(connSocket.getInputStream());
                                String cardsString = sc.nextLine();
                                mOpponentCards = getOpponentCards(cardsString);

                                connSocket.close();

                                mStartHandler.sendEmptyMessage(1);
                            } catch (Exception e) {
                                Log.d("ConnectActivity", "Failed to connect to " + ip, e);
                            }
                        }
                    };
                    executorService.execute(r);
                }
                executorService.shutdown();
                try {
                    executorService.awaitTermination(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    // nothing to do
                }
                progressHandler.sendEmptyMessage(0);
            }
        };
        th.start();
    }

    private int getOwnIp() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
                Integer.reverseBytes(ipAddress) : ipAddress;
    }

    private static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }
}
