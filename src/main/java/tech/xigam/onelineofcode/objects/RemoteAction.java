package tech.xigam.onelineofcode.objects;

public final class RemoteAction {
    public final String action;
    public boolean blankStatus;
    public String statusMessage, statusEmoji, statusEmojiId;
    public RemoteAction(String action) {
        this.action = action;
    }

    public static RemoteAction blankStatus() {
        var remoteAction = new RemoteAction("status");
        remoteAction.blankStatus = true;

        return remoteAction;
    }

    public static RemoteAction customStatus(String statusMessage, String statusEmoji, String statusEmojiId) {
        var remoteAction = new RemoteAction("status");
        remoteAction.blankStatus = false;
        remoteAction.statusMessage = statusMessage;
        remoteAction.statusEmoji = statusEmoji;
        remoteAction.statusEmojiId = statusEmojiId;

        return remoteAction;
    }
}
