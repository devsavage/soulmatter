package io.savagedev.soulmatter.handlers.events;

/*
 * MobDropsHandler.java
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

import io.savagedev.soulmatter.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.Random;

public class MobDropsHandler
{
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        LivingEntity target = event.getEntity();
        Level world = target.level();
        BlockPos blockPos = target.getOnPos();
        Collection<ItemEntity> drops = event.getDrops();

        double rand = Math.random();

        if(event.getSource().getDirectEntity() instanceof Player) {
            Player player = (Player) event.getSource().getDirectEntity();
            if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.SOUL_STEALER.get()) {
                if(target instanceof Monster) {
                    if(target instanceof EnderMan) {
                        if(rand < 0.3D) {
                            drops.add(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.RAW_SOUL_MATTER.get(), 2)));
                            return;
                        }
                    }

                    if(rand < 0.15D) {
                        drops.add(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.RAW_SOUL_MATTER.get(), 1)));
                    }
                }

                if (target instanceof EnderDragon) {
                    Random amt = new Random();
                    drops.add(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.RAW_SOUL_MATTER.get(), amt.nextInt(32))));
                }
            }
        }
    }
}
