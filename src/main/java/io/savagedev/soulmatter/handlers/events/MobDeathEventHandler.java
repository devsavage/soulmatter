package io.savagedev.soulmatter.handlers.events;

/*
 * MobDeathEventHandler.java
 * Copyright (C) 2014 - 2021 Savage - github.com/devsavage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import io.savagedev.savagecore.nbt.NBTHelper;
import io.savagedev.savagecore.util.logger.LogHelper;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobDeathEventHandler
{
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if(event.getSource().getDirectEntity() instanceof Player) {
            Player player = (Player) event.getSource().getDirectEntity();
            if(event.getEntity() instanceof Monster) {
                if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.SOUL_STEALER.get()) {
                    ItemStack soulStealer = player.getItemInHand(InteractionHand.MAIN_HAND);
                    LogHelper.info(soulStealer.getDisplayName().getString());
                    if(soulStealer.hasTag() && !soulStealer.getTag().contains(ModNames.Tags.SOULS_TAKEN)) {
                        NBTHelper.setInt(soulStealer, ModNames.Tags.SOULS_TAKEN, 1);
                    } else {
                        int count = NBTHelper.getInt(soulStealer, ModNames.Tags.SOULS_TAKEN);
                        if(count >= 0)
                            NBTHelper.setInt(soulStealer, ModNames.Tags.SOULS_TAKEN, count + 1);
                        else
                            NBTHelper.setInt(soulStealer, ModNames.Tags.SOULS_TAKEN, 0);
                    }
                }
            }
        }
    }
}
