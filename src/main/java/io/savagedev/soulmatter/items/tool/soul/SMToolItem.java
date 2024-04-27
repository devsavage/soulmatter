package io.savagedev.soulmatter.items.tool.soul;

/*
 * SMToolItem.java
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

import com.google.common.collect.Sets;
import io.savagedev.savagecore.nbt.NBTHelper;
import io.savagedev.soulmatter.handlers.SMToolLevelHandler;
import io.savagedev.soulmatter.init.ModConfig;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.init.ModToolTier;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class SMToolItem extends DiggerItem
{
    private final String toolName;
    private TagKey<Block> effectiveBlocks;

    public SMToolItem(String toolName, float attackDamageIn, TagKey<Block> effectiveBlocksIn, Function<Properties, Properties> properties) {
        super(attackDamageIn, 2, ModToolTier.SOUL_MATTER, effectiveBlocksIn, properties.apply(new Properties()));
        this.toolName = toolName;
        this.effectiveBlocks = effectiveBlocksIn;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean isSelected) {
        if(stack.hasTag() && !stack.getTag().contains(SMToolLevelHandler.SOUL_TOOL_TAG) && !world.isClientSide()) {
            NBTHelper.setString(stack, SMToolLevelHandler.SOUL_TOOL_TAG, stack.getDisplayName().toString());
            SMToolLevelHandler.addLevelTag(stack);
        } else {
            if(stack.getDamageValue() == stack.getMaxDamage() - 1) {
                stack.setDamageValue(0);
            }
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(!SMToolLevelHandler.isMaxToolLevel(stack)) {
            if(stack.getItem() == ModItems.SOUL_MATTER_SWORD.get()) {
                if(attacker instanceof Player) {
                    SMToolLevelHandler.addXp(stack, (Player) attacker, Mth.nextInt(RandomSource.create(), 2, 2));
                }
            }

            stack.hurtAndBreak(1, attacker, (event) -> {
                event.broadcastBreakEvent(InteractionHand.MAIN_HAND);
            });
        } else {
            if(ModConfig.SHOULD_DAMAGE_MAX_TOOL.get()) {
                stack.hurtAndBreak(1, attacker, (event) -> {
                    event.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                });
            } else {
                stack.hurtAndBreak(0, attacker, (event) -> {
                    event.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                });
            }
        }

        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (worldIn.isClientSide() || !(entityLiving instanceof Player)) {
            return false;
        }

        if(SMToolLevelHandler.hasLevelTags(stack)) {
            if(state.is(effectiveBlocks) || isCorrectToolForDrops(stack, state)) {
                if(!SMToolLevelHandler.isMaxToolLevel(stack)) {
                    SMToolLevelHandler.addXp(stack, (Player) entityLiving, entityLiving.getRandom().nextInt(6));
                    stack.hurtAndBreak(1, entityLiving, (event) -> {
                        event.broadcastBreakEvent(entityLiving.getUsedItemHand());
                    });
                } else {
                    if(ModConfig.SHOULD_DAMAGE_MAX_TOOL.get()) {
                        stack.hurtAndBreak(1, entityLiving, (event) -> {
                            event.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                    } else {
                        stack.hurtAndBreak(0, entityLiving, (event) -> {
                            event.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                    }
                }
            } else {
                if(!SMToolLevelHandler.isMaxToolLevel(stack)) {
                    stack.hurtAndBreak(1, entityLiving, (event) -> {
                        event.broadcastBreakEvent(entityLiving.getUsedItemHand());
                    });
                } else {
                    if(ModConfig.SHOULD_DAMAGE_MAX_TOOL.get()) {
                        stack.hurtAndBreak(1, entityLiving, (event) -> {
                            event.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                    } else {
                        stack.hurtAndBreak(0, entityLiving, (event) -> {
                            event.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() instanceof SMToolItem && repair.getItem() == ModItems.SOUL_MATTER.get();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if(isCorrectToolForDrops(stack, state)) {
            if(SMToolLevelHandler.isMaxToolLevel(stack)) {
                return state.is(effectiveBlocks) ? getTier().getSpeed() * SMToolLevelHandler.getToolLevel(stack) : getTier().getSpeed();
            }

            return state.is(effectiveBlocks) ? getTier().getSpeed() : 1.0F;
        }

        return 1.0F;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        switch (toolName) {
            case "pickaxe":
            case "hammer":
                return state.is(effectiveBlocks) ||
                        state.is(BlockTags.MINEABLE_WITH_PICKAXE);
            case "axe":
                return state.is(effectiveBlocks) ||
                        state.is(BlockTags.MINEABLE_WITH_AXE);
            case "sword":
                return state.is(effectiveBlocks) ||
                        state.is(Blocks.COBWEB);
            case "shovel":
                return state.is(effectiveBlocks) || state.is(BlockTags.MINEABLE_WITH_SHOVEL);
            default:
                return super.isCorrectToolForDrops(stack, state);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if(!SMToolLevelHandler.isMaxToolLevel(stack)) {
            tooltip.add(Component.literal("Level: " + ChatFormatting.AQUA + SMToolLevelHandler.getToolLevel(stack)));
            tooltip.add(Component.literal("XP: " + ChatFormatting.GREEN + SMToolLevelHandler.getToolXp(stack)));
        } else {
            tooltip.add(Component.literal("Level: " + ChatFormatting.AQUA + SMToolLevelHandler.getToolLevel(stack) + " (Max Level)"));
        }
    }
}
