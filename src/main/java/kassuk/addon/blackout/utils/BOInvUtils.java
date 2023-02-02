package kassuk.addon.blackout.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import kassuk.addon.blackout.managers.Managers;
import meteordevelopment.meteorclient.mixininterface.IClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BOInvUtils {
    static int[] slots;

    // Credits to rickyracuun
    public static boolean invSwitch(int slot) {
        if (slot >= 0) {
            ScreenHandler handler = mc.player.currentScreenHandler;
            Int2ObjectArrayMap<ItemStack> stack = new Int2ObjectArrayMap<>();
            stack.put(slot, handler.getSlot(slot).getStack());

            mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(handler.syncId,
                handler.getRevision(), PlayerInventory.MAIN_SIZE + Managers.HOLDING.slot,
                slot, SlotActionType.SWAP, handler.getCursorStack().copy(), stack)
            );
            ((IClientPlayerInteractionManager) mc.interactionManager).syncSelected();
            slots = new int[]{slot, Managers.HOLDING.slot};
            return true;
        }
        return false;
    }

    public static void swapBack() {
        ScreenHandler handler = mc.player.currentScreenHandler;
        Int2ObjectArrayMap<ItemStack> stack = new Int2ObjectArrayMap<>();
        stack.put(slots[0], handler.getSlot(slots[0]).getStack());

        mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(handler.syncId,
            handler.getRevision(), PlayerInventory.MAIN_SIZE + slots[1],
            slots[0], SlotActionType.SWAP, handler.getCursorStack().copy(), stack)
        );
        ((IClientPlayerInteractionManager) mc.interactionManager).syncSelected();
    }
}
