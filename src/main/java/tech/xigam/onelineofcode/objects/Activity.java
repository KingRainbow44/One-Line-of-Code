package tech.xigam.onelineofcode.objects;

public final class Activity {
    public RichPresence richPresence;
    public Bot bot;

    public static class RichPresence {
        public String details, state, largeImage, smallImage;
        public Button[] buttons;
        
        public static class Button {
            public String label, url;
        }
    }

    public static class Bot {
        public String presence;
        public Status status;

        public static class Status {
            public String text, action;
        }
    }
}
