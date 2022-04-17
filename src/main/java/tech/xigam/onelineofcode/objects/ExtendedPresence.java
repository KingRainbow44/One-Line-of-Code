package tech.xigam.onelineofcode.objects;

import com.google.gson.Gson;
import com.jagrosh.discordipc.entities.RichPresence;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONArray;
import org.json.JSONObject;
import tech.xigam.onelineofcode.OneLineOfCode;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExtendedPresence extends RichPresence {
    public List<Button> buttons;
    
    public ExtendedPresence(String state, String details, OffsetDateTime startTimestamp, OffsetDateTime endTimestamp, String largeImageKey, 
            String largeImageText, String smallImageKey, String smallImageText, String partyId, int partySize, int partyMax, 
            String matchSecret, String joinSecret, String spectateSecret, List<Button> buttons, boolean instance) {
        super(state, details, startTimestamp, endTimestamp, largeImageKey, 
                largeImageText, smallImageKey, smallImageText, partyId, partySize, 
                partyMax, matchSecret, joinSecret, spectateSecret, instance);
        this.buttons = buttons;
    }

    @Override
    public JSONObject toJson() {
        var object = super.toJson();
        if(buttons.size() > 0) {
            var buttonsArray = new JSONArray();
            for(var button : buttons)
                buttonsArray.put(button.toJson());
            object.put("buttons", buttonsArray);
        } return object;
    }
    
    @Setter @Accessors(chain = true)
    public static class Button {
        public String label;
        public String url;
        
        public JSONObject toJson() {
            return new JSONObject()
                    .put("label", label)
                    .put("url", url);
        }
    }
}
