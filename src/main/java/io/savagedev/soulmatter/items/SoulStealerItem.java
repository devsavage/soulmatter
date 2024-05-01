package io.savagedev.soulmatter.items;

/*
 * SoulStealerItem.java
 * Copyright (C) 2014 - 2024 Savage - github.com/devsavage
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
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SoulStealerItem extends SMItem
{
    public static int MAX_SOULS = Integer.MAX_VALUE;
    public SoulStealerItem() {
        super(properties -> properties.stacksTo(1));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 18000;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level world = player.level();

        if(target instanceof Monster) {
            if(world.isClientSide) {
                world.addParticle(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getEyeY() + 0.3D, target.getZ(), 0, 0, 0);
            }

            int damage = random.nextInt(10);

            target.hurt(player.damageSources().playerAttack(player), damage);

            return InteractionResult.SUCCESS;
        }

        if(target instanceof EnderDragon) {
            if(world.isClientSide) {
                world.addParticle(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getEyeY() + 0.3D, target.getZ(), 0, 0, 0);
            }

            int damage = random.nextInt(20);

            target.hurt(player.damageSources().playerAttack(player), damage);

            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if(stack.hasTag() && stack.getTag().contains(ModNames.Tags.SOULS_TAKEN)) {
            tooltip.add(Component.literal("Souls Taken: " + ChatFormatting.DARK_RED + NBTHelper.getInt(stack, ModNames.Tags.SOULS_TAKEN)));
        }
    }
}