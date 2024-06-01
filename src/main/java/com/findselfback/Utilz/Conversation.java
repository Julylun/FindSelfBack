package com.findselfback.Utilz;

public class Conversation {
    public static class Task {
        public final static String TRASH_CAN_CLIMBING_TASK = "Press [W] to climb on the trash can";
        public final static String TALK_TO_THE_POLICE = "Get closer the police and press E to ask him what happened.";
    }
    public static class Begin {
        public static String[] FIRST_TALK = {"...",
                "Skeleton: Where am I?",
                "Skeleton: Wait, Who am I?",
                "Skeleton: This city... Maybe I used to see...",
                "Skeleton: Arggg! My head is hurt..",
                "Skeleton: ...",
                "Skeleton: Something happened in ahead..",
                "Skeleton: An accident?",
                "Skeleton: I have to know what happened."};
        public static String[] TRASH_EVENT = {"Skeleton: ...","Skeleton: Who would leave a trash can in the middle of the road like this?",
                                                "Skeleton: I need to climb over this trash can to get to the other side."};
        public static String[] POLICE_TALKING_EVENT = {
            "Skeleton: ...",
            "Skeleton: A officer is doing his duty.",
            "Skeleton: Should I go to ask him what happened?",
            "Skeleton: ...",
            "Skeleton: I think I should."
        };
    }
}
