package tech.xigam.onelineofcode.objects;

import com.jagrosh.discordipc.entities.RichPresence;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
public final class PresenceDetails {
    public String state;
    public String details;
    public OffsetDateTime startTimestamp;
    public OffsetDateTime endTimestamp;
    public String largeImageKey;
    public String largeImageText;
    public String smallImageKey;
    public String smallImageText;
    public String partyId;
    public int partySize;
    public int partyMax;
    public String matchSecret;
    public String joinSecret;
    public String spectateSecret;
    public boolean instance;

    public PresenceDetails() { }
    
    public RichPresence build() {
        return new RichPresence(this.state, this.details, this.startTimestamp, this.endTimestamp, this.largeImageKey, this.largeImageText, this.smallImageKey, this.smallImageText, this.partyId, this.partySize, this.partyMax, this.matchSecret, this.joinSecret, this.spectateSecret, this.instance);
    }

    public PresenceDetails setState(String state) {
        this.state = state; return this;
    }

    public PresenceDetails setDetails(String details) {
        this.details = details; return this;
    }

    public PresenceDetails setStartTimestamp(OffsetDateTime startTimestamp) {
        this.startTimestamp = startTimestamp; return this;
    }

    public PresenceDetails setEndTimestamp(OffsetDateTime endTimestamp) {
        this.endTimestamp = endTimestamp; return this;
    }

    public PresenceDetails setLargeImage(String largeImageKey, String largeImageText) {
        this.largeImageKey = largeImageKey;
        this.largeImageText = largeImageText;
        return this;
    }

    public PresenceDetails setLargeImage(String largeImageKey) {
        return this.setLargeImage(largeImageKey, null);
    }

    public PresenceDetails setSmallImage(String smallImageKey, String smallImageText) {
        this.smallImageKey = smallImageKey; this.smallImageText = smallImageText;
        return this;
    }

    public PresenceDetails setSmallImage(String smallImageKey) {
        return this.setSmallImage(smallImageKey, null);
    }

    public PresenceDetails setParty(String partyId, int partySize, int partyMax) {
        this.partyId = partyId; this.partySize = partySize; 
        this.partyMax = partyMax; return this;
    }

    public PresenceDetails setMatchSecret(String matchSecret) {
        this.matchSecret = matchSecret; return this;
    }

    public PresenceDetails setJoinSecret(String joinSecret) {
        this.joinSecret = joinSecret; return this;
    }

    public PresenceDetails setSpectateSecret(String spectateSecret) {
        this.spectateSecret = spectateSecret; return this;
    }

    public PresenceDetails setInstance(boolean instance) {
        this.instance = instance; return this;
    }
}
