package me.gravityio.viewboboptions.mixin;

public class TransientMixinData {
    public static BobType CURRENT = BobType.NONE;
    public enum BobType {
        HAND, CAMERA, NONE;
    }
}
