package tech.xigam.onelineofcode.utils;

import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.objects.RemoteAction;

import javax.annotation.Nullable;

public final class RemoteUtil {
    public static void updateClient(@Nullable RemoteAction action) {
        if (OneLineOfCode.client == null)
            return;
        OneLineOfCode.client.send(
                JsonUtil.jsonSerialize(action)
        );
    }
}
