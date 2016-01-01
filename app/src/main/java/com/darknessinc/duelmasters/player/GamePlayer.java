package com.darknessinc.duelmasters.player;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.util.Log;

import com.darknessinc.duelmasters.activity.util.ZoneContainerCollection;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.layout.ZoneContainer;
import com.darknessinc.duelmasters.zone.Zone;

public class GamePlayer extends Player {
    // does not manage to be Parcelable
    private ZoneContainerCollection mZoneContainerCollection;

    public GamePlayer(Player p, ZoneContainerCollection zc) {
        super(p.getId(), p.getName(), p.getGameCards());
        this.mZoneContainerCollection = zc;
    }

    public ZoneContainer getZoneContainer(int zoneId) {
        return mZoneContainerCollection.getZoneContainer(zoneId);
    }

    public GamePlayer(Parcel in) {
        super(in);
    }

    public static final Creator<GamePlayer> CREATOR = new Creator<GamePlayer>() {
        @Override
        public GamePlayer createFromParcel(Parcel in) {
            return new GamePlayer(in);
        }

        @Override
        public GamePlayer[] newArray(int size) {
            return new GamePlayer[size];
        }
    };

    public ZoneContainerCollection.GameCardsLists getGameCardLists() {
        return mZoneContainerCollection.getGameCardsLists();
    }
}
