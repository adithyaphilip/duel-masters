package com.darknessinc.duelmasters.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.activity.util.ZoneContainerCollection;
import com.darknessinc.duelmasters.bluetooth.BluetoothChatService;
import com.darknessinc.duelmasters.bluetooth.BluetoothMediator;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.feed.InstructionDecoder;
import com.darknessinc.duelmasters.layout.ZoneContainer;
import com.darknessinc.duelmasters.player.GamePlayer;
import com.darknessinc.duelmasters.player.Player;

/**
 *
 * @author USER
 * Expects "player1" and "player2" containing Parcelable Player objects
 * Expects passed Player objects to be already initialised with given cards
 * Assumes playerid is last digit of gameid
 * Assumes player with id 1 is opponent and player with id 2 is not opponent
 */
public class GameActivity extends Activity {
	//Bluetooth Stuff
	ArrayList<String> msgs=new ArrayList<>();
	ListView lv;
	ArrayAdapter<String> adapter;
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;

	//End of Bluetooth Stuff

	public final static int CARD_OPTIONS_ACTIVITY = 1;
	public static final int CARD_CHOOSER_ACTIVITY = 2;

	public static final String KEY_PLAYER1="player1";
	public static final String KEY_PLAYER2="player2";

	private static final String CHAT_MESSAGE_INDICATOR="!#";

    private static final String SAVED_CARD_LIST_P1 = "card_list_p1";
    private static final String SAVED_CARD_LIST_P2 = "card_list_p2";
    private static final String SAVED_PLAYER_P1 = "player_p1";
    private static final String SAVED_PLAYER_P2 = "player_p2";

    Player mP1;
    Player mP2;
	GamePlayer mGp1;
	GamePlayer mGp2;

	//TODO refactor below names

	LinearLayout mP1Hand;
	LinearLayout mP1Mana;
	LinearLayout mP1Shield;
	LinearLayout mP1Battle;
	ImageView mP1Deck;

	ImageView mP1Grave;

	LinearLayout mP2Hand;
	LinearLayout mP2Mana;
	LinearLayout mP2Shield;
	LinearLayout mP2Battle;
	ImageView mP2Deck;
	ImageView mP2Grave;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		mChatService =	BluetoothMediator.mChatService;//should be set by calling activity
		mChatService.setHandler(mHandler);
		setContentView(R.layout.activity_game);
		//Retrieving all layouts
		mP1Hand = (LinearLayout)findViewById(R.id.p1_hand);
		mP1Mana = (LinearLayout)findViewById(R.id.p1_mana);
		mP1Shield = (LinearLayout)findViewById(R.id.p1_shield);
		mP1Battle = (LinearLayout)findViewById(R.id.p1_battle);
		mP1Deck = (ImageView)findViewById(R.id.p1_deck_iv);
		mP1Grave = (ImageView)findViewById(R.id.p1_grave_iv);

		mP2Hand = (LinearLayout)findViewById(R.id.p2_hand);
		mP2Mana = (LinearLayout)findViewById(R.id.p2_mana);
		mP2Shield = (LinearLayout)findViewById(R.id.p2_shield);
		mP2Battle = (LinearLayout)findViewById(R.id.p2_battle);
		mP2Deck = (ImageView)findViewById(R.id.p2_deck_iv);
		mP2Grave = (ImageView)findViewById(R.id.p2_grave_iv);
		//Finished retrieving all layouts

        ZoneContainerCollection p1ZcCollection;
        ZoneContainerCollection p2ZcCollection;

		//Setting up players for later use by containers and gameplayers

        if (savedInstanceState != null) {// we're being restored from a saved state
			Log.e("PHILIP", "being restored from saved state");
			mP1 = savedInstanceState.getParcelable(SAVED_PLAYER_P1);
            mP2 = savedInstanceState.getParcelable(SAVED_PLAYER_P2);
            p1ZcCollection = new ZoneContainerCollection(
                    (ZoneContainerCollection.GameCardsLists)
                            savedInstanceState.getParcelable(SAVED_CARD_LIST_P1),
                    mP1Mana, mP1Hand, mP1Shield, mP1Battle, mP1Grave, mP1Deck);
            p2ZcCollection = new ZoneContainerCollection(
                    (ZoneContainerCollection.GameCardsLists)
                            savedInstanceState.getParcelable(SAVED_CARD_LIST_P2),
                    mP2Mana, mP2Hand, mP2Shield, mP2Battle, mP2Grave, mP2Deck);
			Log.e("PHILIP", "zc1" + p1ZcCollection);
			Log.e("PHILIP", "being restored from saved state");
		} else { // we're being started afresh
            Intent i = getIntent();
            mP1 = i.getParcelableExtra("player1");
            mP2 = i.getParcelableExtra("player2");
            p1ZcCollection = new ZoneContainerCollection(mP1.getGameCards(),
                    mP1Mana, mP1Hand, mP1Shield, mP1Battle, mP1Grave, mP1Deck);
            p2ZcCollection = new ZoneContainerCollection(mP2.getGameCards(),
                    mP2Mana, mP2Hand, mP2Shield, mP2Battle, mP2Grave, mP2Deck);
        }

		//initialising all zones. Only decks are set with cards from players. Requires players to be initialised
		//mP1Deck used simply to ensure each layout will know what size to make the cards they add
		//Finished initialising zones

		//Setting up gameplayers
		mGp1 = new GamePlayer(mP1, p1ZcCollection);
		mGp2 = new GamePlayer(mP2, p2ZcCollection);
		//finished setting up gameplayers

		ViewTreeObserver observer = mP1Deck.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		    	//TODO add grave when it becomes linearlayout
		    	mP1Hand.setMinimumHeight(mP1Deck.getHeight());
		    	mP1Mana.setMinimumHeight(mP1Deck.getHeight());
		    	mP1Shield.setMinimumHeight(mP1Deck.getHeight());
		    	mP1Battle.setMinimumHeight(mP1Deck.getHeight());

		    	mP2Hand.setMinimumHeight(mP2Deck.getHeight());
		    	mP2Mana.setMinimumHeight(mP2Deck.getHeight());
		    	mP2Shield.setMinimumHeight(mP2Deck.getHeight());
		    	mP2Battle.setMinimumHeight(mP2Deck.getHeight());
		    }
		});

		lv = (ListView) findViewById(R.id.feed_lv);

		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, msgs);
		 lv.setAdapter(adapter);
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save the state so it can be restored on re-creation
        outState.putParcelable(SAVED_PLAYER_P1, mP1);
        outState.putParcelable(SAVED_PLAYER_P2, mP2);
        outState.putParcelable(SAVED_CARD_LIST_P1, mGp1.getGameCardLists());
        outState.putParcelable(SAVED_CARD_LIST_P2, mGp2.getGameCardLists());

        super.onSaveInstanceState(outState);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode,Intent data){
		switch(requestCode){
		case CARD_OPTIONS_ACTIVITY:{//Assumes data can never be null
			GameCard gc = data.getParcelableExtra(CardOptionChooserActivity.KEY_RETURN_CARD);
			boolean viewed = data.getBooleanExtra(CardOptionChooserActivity.KEY_RETURN_CARD_VIEWED, false);
			if(viewed){
				String instr = InstructionDecoder.getPeekInstruction(gc);
				InstructionDecoder.decodeInstruction(instr, this);
				String msg = InstructionDecoder.getInstructionMessage(instr, this);
				msgs.add(msg);
				updateFeed();
				sendInstruction(instr);
				}
			if(resultCode==Activity.RESULT_OK){
				if(viewed){
					String instr = InstructionDecoder.getPeekInstruction(gc);
					InstructionDecoder.decodeInstruction(instr, this);
					String msg = InstructionDecoder.getInstructionMessage(instr, this);
					msgs.add(msg);
					updateFeed();
					sendInstruction(instr);
					}
				String option = data.getStringExtra(CardOptionChooserActivity.KEY_RETURN_OPTION);
				ZoneContainer zc = getZoneContainer(gc.getGameId(), gc.getZoneId());
				String instruction = zc.generateInstruction(gc.getGameId(), option);
				InstructionDecoder.decodeInstruction(instruction, this);
				String msg = InstructionDecoder.getInstructionMessage(instruction, this);
				msgs.add(msg);
				updateFeed();
				sendInstruction(instruction);
			}
			break;
		}
		case CARD_CHOOSER_ACTIVITY:
			if(resultCode==Activity.RESULT_OK){//TODO separate set of options should be available for chosen cards
				GameCard gc = data.getParcelableExtra(CardChooserActivity.KEY_RETURN_CARD);
				ZoneContainer zc = getZoneContainer(gc.getGameId(), gc.getZoneId());
				Intent i = new Intent(this,CardOptionChooserActivity.class);
				i.putExtra(CardOptionChooserActivity.KEY_CARD,gc);
				i.putExtra(CardOptionChooserActivity.KEY_OPTIONS, zc.getOwnCardOptions(gc));
				startActivityForResult(i,CARD_OPTIONS_ACTIVITY);
			}
			break;
		}
	}
	/**
	 * gameId necessary as Player id is encoded in it
	 * @param gameId gameId of any card belonging to concerned player
	 * @param zoneId
	 * @return
	 */
	public ZoneContainer getZoneContainer(int gameId, int zoneId){
		Log.d("GameActivity","getZoneContainer: gameId: "+gameId+" zoneId: "+zoneId);
		GamePlayer gPlayer = getPlayer(gameId%10);
		return gPlayer.getZoneContainer(zoneId);
	}
	public GamePlayer getPlayer(int playerId){
		if(mGp1.getId()==playerId)
			return mGp1;
		if(mGp2.getId()==playerId)
			return mGp2;
		Log.e("GameActivity","GetZoneContainer: No player with id: "+playerId);
		return null;
	}
	public static boolean isOpponent(int id){
		return id==1;
	}
	public static int getPlayerId(GameCard gc){
		return gc.getGameId()%10;
	}
	public GameCard getGameCard(int gameId){
		for(int i=0;i< mGp1.getGameCards().size();i++)
			if(gameId== mGp1.getGameCards().get(i).getGameId())
				return mGp1.getGameCards().get(i);
		for(int i=0;i< mGp2.getGameCards().size();i++)
			if(gameId== mGp2.getGameCards().get(i).getGameId())
				return mGp2.getGameCards().get(i);
		return null;
	}

	//Bluetoot Stuff
	@Override
	public void onStart() {
		super.onStart();
		/*if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}*/
	}
	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}
	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}
	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}
	private void sendInstruction(String instruction){
		sendBTMessage(InstructionDecoder.getInstructionToSend(instruction));
	}
	/**
	 * Sends a message.
	 *
	 * @param message
	 *            A string of text to send.
	 */
	private void sendBTMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Log.d("sendBTMessage",message);
		// Check that there's actually something to send

			if (message.length() > 0) {
				// Get the message bytes and tell the BluetoothChatService to write
 //Decoder acts in before sending
			byte[]	send=message.getBytes();
			mChatService.write(send);
			}
	}
   public void onSendClicked(View v){
	   EditText et = (EditText)findViewById(R.id.message_et);
	   String msg = et.getText()+"";
	   msg = msg.replaceAll(":", " ");
	   String ownMsg = "You: "+msg;
	   String oppMsg = CHAT_MESSAGE_INDICATOR+"Opponent: "+msg;//starting P is token indicating message
	   if(!msg.equals("")){
		   msgs.add(ownMsg);
		   updateFeed();
		   sendBTMessage(oppMsg);
	   }
   }
	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					Toast.makeText(GameActivity.this,getString(R.string.title_connected_to,
							mConnectedDeviceName),Toast.LENGTH_SHORT).show();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					Toast.makeText(GameActivity.this,getString(R.string.title_connecting,
							mConnectedDeviceName),Toast.LENGTH_SHORT).show();
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					Toast.makeText(GameActivity.this,getString(R.string.title_not_connected,
							mConnectedDeviceName),Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				String show = "";
				if(readMessage.startsWith(CHAT_MESSAGE_INDICATOR)){
					show = readMessage.substring(CHAT_MESSAGE_INDICATOR.length(),readMessage.length());
				}
				else{
					InstructionDecoder.decodeInstruction(readMessage, GameActivity.this);
					show=InstructionDecoder.getInstructionMessage(readMessage, GameActivity.this);
				}
				msgs.add(show);
				updateFeed();
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void updateFeed(){
		adapter = new ArrayAdapter<>(this,
		        android.R.layout.simple_list_item_1, msgs);
		 lv.setAdapter(adapter);
	}

}
