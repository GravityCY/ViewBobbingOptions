package me.gravityio.viewboboptions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class Helkper {

    public static ItemStack[] getHandSlots(Player player) {
        return new ItemStack[] {player.getMainHandItem(), player.getOffhandItem()};
    }

}
