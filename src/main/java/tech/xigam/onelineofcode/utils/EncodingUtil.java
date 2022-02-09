package tech.xigam.onelineofcode.utils;

import java.util.Base64;

public final class EncodingUtil {
    public static String base64Encode(String encode) {
        return Base64.getUrlEncoder().encodeToString(encode.getBytes());
    }
    
    public static String base64Decode(String decode) {
        return new String(Base64.getUrlDecoder().decode(decode));
    }
}
