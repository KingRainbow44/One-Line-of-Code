package tech.xigam.onelineofcode.utils;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import static tech.xigam.onelineofcode.OneLineOfCode.cache;

public final class HttpUtil {
    public static String shortenUrl(String toShorten) {
        if(cache.urls.containsKey(toShorten))
            return cache.urls.get(toShorten);

        var httpClient = new OkHttpClient();
        var request = new Request.Builder()
                .method("POST", RequestBody.create(null, ""))
                .url("https://app.ponjo.club/v1/urlshortener/create?url=" + toShorten)
                .build();
        try (var response = httpClient.newCall(request).execute()) {
            assert response.body() != null;
            if(response.code() != 200) {
                OneLineOfCode.logger.error("Failed to shorten url: " + toShorten + " (" + response.code() + ")");
                cache.urls.put(toShorten, toShorten);
            }

            var json = JsonUtil.jsonDeserialize(response.body().string(), URLShortenResponse.class);
            cache.urls.put(toShorten, "https://app.ponjo.club/" + json.data.get("short").getAsString());
            OneLineOfCode.logger.info("Shortened url: " + toShorten + " to " + cache.urls.get(toShorten));
        } catch (Exception exception) {
            OneLineOfCode.logger.warn("Unable to shorten URL.", exception);
            cache.urls.put(toShorten, toShorten);
        }

        FileUtil.writeToFile(Constants.CACHE_FILE, JsonUtil.jsonFileSerialize(cache));
        return cache.urls.get(toShorten);
    }
    
    static class URLShortenResponse {
        public JsonObject data;
    }
}
