package tech.xigam.onelineofcode.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Cache {
    public Cache() {
        this.coverArt = new ArrayList<>();
        this.urls = new HashMap<>();
    }
    
    public List<String> coverArt;
    public Map<String, String> urls;
    
    public boolean isEmpty() {
        return this.coverArt.isEmpty() || this.urls.isEmpty();
    }
}
