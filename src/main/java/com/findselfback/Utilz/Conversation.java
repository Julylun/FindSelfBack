package com.findselfback.Utilz;

public class Conversation {
    public static class Task {
        public final static String TRASH_CAN_CLIMBING_TASK = "Press [W] to climb on the trash can";
        public final static String TALK_TO_THE_POLICE = "Get closer the police and press [E] to ask him what happened.";
        public final static String JUMP_THROUGH_THE_GLASSES = "Press [shift] and [D] to run then press [space] to jump.";
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

        public static String[] RETURN_THE_POLICE = {".",
                "Skeleton: I think I have to tủn back then ask the officer some questions."
        };

        public static String[] POLICE_CONVERSATION = {"",
                "Skeleton: Hello sir, could you let me know what happened?",
                "The traffic police: Uhm...",
                "The traffic police: The girl was hit by that car which lost control....",
                "The traffic police: ...",
                "The traffic police: She is gone...",
                "Skeleton: ...",
                "Skeleton: *I feel heartbroken, and I don't understand why I feel this way.*",
                "Skeleton: *This girl seems somewhat familiar to me; I think I've met her somewhere before.*",
                "The traffic police: The guy over there is probably her boyfriend.",
                "The traffic police: He looks devastated. Since the accident happened, he has been crying and saying to her, \"I'm sorry.\"",
                "The traffic police: But in any case, the accident has happened and she is gone.",
                "The traffic police: I have been in this profession for 10 years, and I have witnessed many accidents.",
                "The traffic police: The family and loved ones are always the ones who suffer the most.",
                "The traffic police: Do you agree with me?",
                "Skeleton: ...",
                "Skeleton: Of course, I still don't understand the pain of family or loved ones because I remember having them...",
                "Skeleton: ... but I feel something very strange inside me.",
                "The traffic police: I think you might be feeling afraid of someone's death and the loss of a family member.",
                "The traffic police: But regardless, traffic accidents are increasing day by day.",
                "The traffic police: You have to be careful when participating in traffic, okay?",
                "The traffic police: Oh, over there, there are some sharp glass shards from the car, quite dangerous.",
                "The traffic police: Remember to stay away from those sharp glass pieces.",
                "Skeleton: ... Yes, sir.",
        };

        public static String[] CANT_WALK_THROUGH_GLASS = {"",
                "Skeleton: This glass debris field is too large, I can't easily pass through.",
                "Skeleton: I think I have to run fast and then jump over it to get through."
        };
    }
}
