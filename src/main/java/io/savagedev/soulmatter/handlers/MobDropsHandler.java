package io.savagedev.soulmatter.handlers;

/*
 * MobDropsHandler.java
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.Random;

public class MobDropsHandler
{
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition();
        Collection<ItemEntity> drops = event.getDrops();
        double rand  = Math.random();

        if(event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity livingEntity = (PlayerEntity) event.getSource().getTrueSource();
            if(livingEntity.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.SOUL_STEALER.get()) {
                if(entity instanceof MonsterEntity) {
                    ItemStack soulStealer = livingEntity.getHeldItem(Hand.MAIN_HAND).getStack();
                    // We only give souls stolen when we drop soul matter. This makes it 1:1; 1 raw soul matter to 1 soul stolen
                    if(entity instanceof EndermanEntity) {
                        if(rand < 0.3D) {
                            if(soulStealer.getTag() == null || !soulStealer.getTag().contains("SoulsTaken")) {
                                soulStealer.getOrCreateTag().putInt("SoulsTaken", 1);
                            }

                            int count = soulStealer.getTag().getInt("SoulsTaken");
                            if(count >= 0)
                                soulStealer.getTag().putInt("SoulsTaken", count + 1);
                            else
                                soulStealer.getTag().putInt("SoulsTaken", 0);

                            drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.RAW_SOUL_MATTER.get(), 2)));
                            return;
                        }
                    }

                    if(rand < 0.15D) {
                        if(soulStealer.getTag() == null || !soulStealer.getTag().contains("SoulsTaken")) {
                            soulStealer.getOrCreateTag().putInt("SoulsTaken", 1);
                        }

                        int count = soulStealer.getTag().getInt("SoulsTaken");
                        if(count >= 0)
                            soulStealer.getTag().putInt("SoulsTaken", count + 1);
                        else
                            soulStealer.getTag().putInt("SoulsTaken", 0);

                        drops.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.RAW_SOUL_MATTER.get(), 1)));
                    }
                }
            }
        }
    }
}
