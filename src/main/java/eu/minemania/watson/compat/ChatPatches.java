package eu.minemania.watson.compat;

public class ChatPatches {
    
    public static final boolean IS_INSTALLED;
    
    static {
        boolean isInstalled1;
        try {
            Class.forName("obro1961.chatpatches.ChatPatches");
            isInstalled1 = true;
        } catch (ClassNotFoundException e) {
            isInstalled1 = false;
        }
        IS_INSTALLED = isInstalled1;
    }
    
}
