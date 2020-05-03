package io.savagedev.soulmatter.handlers;

/*
 * MobDeathEventHandler.java
 * Copyright (C) 2020 Savage - github.com/devsavage
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

import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.util.LogHelper;
import io.savagedev.soulmatter.util.NBTHelper;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobDeathEventHandler
{
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if(event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity livingEntity = (PlayerEntity) event.getSource().getTrueSource();
            if(event.getEntityLiving() instanceof MonsterEntity) {
                if(livingEntity.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.SOUL_STEALER.get()) {
                    ItemStack soulStealer = livingEntity.getHeldItem(Hand.MAIN_HAND).getStack();
                    if(!NBTHelper.hasTag(soulStealer, "SoulsTaken")) {
                        NBTHelper.setInt(soulStealer, "SoulsTaken", 1);
                    } else {
                        int count = NBTHelper.getInt(soulStealer, "SoulsTaken");
                        if(count >= 0)
                            NBTHelper.setInt(soulStealer, "SoulsTaken", count + 1);
                        else
                            NBTHelper.setInt(soulStealer, "SoulsTaken", 0);
                    }
                }
            }
        }
    }
}
