package me.gravityio.viewboboptions.mixin;

/**
 * Global Temporary Data to create context for the bobbing.
 */
public class GlobalMixinData {
    /**
     * Current Bob Type Context, so the mixin knows what bob type it's performing.
     */
    public static BobType CURRENT = BobType.NONE;
    public enum BobType {
        HAND, CAMERA, NONE;
    }

}
