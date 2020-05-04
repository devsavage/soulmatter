package io.savagedev.soulmatter.items.soul.tools;

/*
 * BaseSoulTool.java
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

import com.google.common.collect.ImmutableSet;
import io.savagedev.soulmatter.handlers.SoulToolLevelHandler;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.init.ModToolTier;
import io.savagedev.soulmatter.util.LogHelper;
import io.savagedev.soulmatter.util.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class BaseSoulTool extends ToolItem
{
    private final String toolName;
    private Set<Block> effectiveBlocks;

    public BaseSoulTool(String toolName, float attackDamageIn, Set<Block> effectiveBlocksIn, Function<Properties, Properties> properties) {
        super(attackDamageIn, 1, ModToolTier.SOUL_MATTER, effectiveBlocksIn, properties.apply(new Properties()));
        this.toolName = toolName;
        this.effectiveBlocks = effectiveBlocksIn;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!NBTHelper.hasTag(stack, SoulToolLevelHandler.SOUL_TOOL_TAG) && !worldIn.isRemote) {
            NBTHelper.setString(stack, SoulToolLevelHandler.SOUL_TOOL_TAG, stack.getDisplayName().getFormattedText());
            SoulToolLevelHandler.addLevelTag(stack);
        } else {
            if(stack.getDamage() == stack.getMaxDamage() - 1) {
                stack.setDamage(0);
            }
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(SoulToolLevelHandler.hasLevelTags(stack) && !SoulToolLevelHandler.isMaxToolLevel(stack)) {
            if (stack.getItem() == ModItems.SOUL_MATTER_SWORD.get()) {
                if (attacker instanceof PlayerEntity) {
                    SoulToolLevelHandler.addXp(stack, (PlayerEntity) attacker, MathHelper.nextInt(new Random(), 2, 8));
                }
            }

            stack.damageItem(1, attacker, (event) -> {
                event.sendBreakAnimation(attacker.getActiveHand());
            });
        } else {
            stack.damageItem(0, attacker, (event) -> {
                event.sendBreakAnimation(attacker.getActiveHand());
            });
        }

        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (worldIn.isRemote || !(entityLiving instanceof PlayerEntity)) {
            return false;
        }

        if(SoulToolLevelHandler.hasLevelTags(stack)) {
            if(effectiveBlocks.contains(state.getBlock()) || canHarvestBlock(state)) {
                if(!SoulToolLevelHandler.isMaxToolLevel(stack)) {
                    SoulToolLevelHandler.addXp(stack, (PlayerEntity) entityLiving, entityLiving.getRNG().nextInt(6));
                    stack.damageItem(1, entityLiving, (event) -> {
                        event.sendBreakAnimation(entityLiving.getActiveHand());
                    });
                } else {
                    stack.damageItem(0, entityLiving, (event) -> {
                        event.sendBreakAnimation(entityLiving.getActiveHand());
                    });
                }
            } else {
                if(SoulToolLevelHandler.hasLevelTags(stack) && !SoulToolLevelHandler.isMaxToolLevel(stack)) {
                    stack.damageItem(1, entityLiving, (event) -> {
                        event.sendBreakAnimation(entityLiving.getActiveHand());
                    });
                } else {
                    stack.damageItem(0, entityLiving, (event) -> {
                        event.sendBreakAnimation(entityLiving.getActiveHand());
                    });
                }
            }
        }

        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() instanceof BaseSoulTool && repair.getItem() == ModItems.SOUL_MATTER_COMPACT.get();
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if(SoulToolLevelHandler.hasLevelTags(stack)) {
            return getTier().getHarvestLevel() * SoulToolLevelHandler.getToolLevel(stack);
        } else {
            return getTier().getHarvestLevel();
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if(canHarvestBlock(state) && SoulToolLevelHandler.hasLevelTags(stack)) {
            if(SoulToolLevelHandler.isMaxToolLevel(stack)) {
                return getTier().getEfficiency() * SoulToolLevelHandler.getToolLevel(stack);
            } else {
                return getTier().getEfficiency();
            }
        }

        return 1.0F;
    }

    @Override
    public boolean canHarvestBlock(BlockState blockState) {
        switch (toolName) {
            case "pickaxe":
                return effectiveBlocks.contains(blockState.getBlock()) ||
                        blockState.getMaterial() == Material.ROCK ||
                        blockState.getMaterial() == Material.IRON ||
                        blockState.getMaterial() == Material.ANVIL;
            case "axe":
                return effectiveBlocks.contains(blockState.getBlock()) ||
                        blockState.getMaterial() != Material.WOOD &&
                                blockState.getMaterial() != Material.PLANTS &&
                                blockState.getMaterial() != Material.TALL_PLANTS &&
                                blockState.getMaterial() != Material.BAMBOO;
            case "sword":
                return effectiveBlocks.contains(blockState.getBlock()) || blockState.getBlock() == Blocks.COBWEB;
            case "hammer":
                return effectiveBlocks.contains(blockState.getBlock()) ||
                        blockState.getBlock() == Blocks.OBSIDIAN ? this.getTier().getHarvestLevel() == 3 : (blockState.getBlock() != Blocks.DIAMOND_BLOCK && blockState.getBlock() != Blocks.DIAMOND_ORE ? (blockState.getBlock() != Blocks.EMERALD_ORE && blockState.getBlock() != Blocks.EMERALD_BLOCK ? (blockState.getBlock() != Blocks.GOLD_BLOCK && blockState.getBlock() != Blocks.GOLD_ORE ? (blockState.getBlock() != Blocks.IRON_BLOCK && blockState.getBlock() != Blocks.IRON_DOOR ? (blockState.getBlock() != Blocks.LAPIS_BLOCK && blockState.getBlock() != Blocks.LAPIS_ORE ? (blockState.getBlock() != Blocks.REDSTONE_ORE && blockState.getBlock() != Blocks.REDSTONE_ORE ? (blockState.getMaterial() == Material.ROCK ? true : (blockState.getMaterial() == Material.IRON ? true : blockState.getMaterial() == Material.ANVIL)) : this.getTier().getHarvestLevel() >= 2) : this.getTier().getHarvestLevel() >= 1) : this.getTier().getHarvestLevel() >= 1) : this.getTier().getHarvestLevel() >= 2) : this.getTier().getHarvestLevel() >= 2) : this.getTier().getHarvestLevel() >= 2);
            case "shovel":
                return effectiveBlocks.contains(blockState.getBlock()) || blockState.getMaterial() == Material.EARTH || blockState.getMaterial() == Material.ORGANIC || blockState.getMaterial() == Material.SAND || blockState.getMaterial() == Material.SNOW || blockState.getMaterial() == Material.SNOW_BLOCK;
            default:
                return super.canHarvestBlock(blockState);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(SoulToolLevelHandler.hasLevelTags(stack) && !SoulToolLevelHandler.isMaxToolLevel(stack)) {
            tooltip.add(new StringTextComponent("Level: " + TextFormatting.AQUA + SoulToolLevelHandler.getToolLevel(stack)));
            tooltip.add(new StringTextComponent("XP: " + TextFormatting.GREEN + SoulToolLevelHandler.getToolXp(stack)));
        } else if(SoulToolLevelHandler.hasLevelTags(stack) && SoulToolLevelHandler.isMaxToolLevel(stack)) {
            tooltip.add(new StringTextComponent("Level: " + TextFormatting.AQUA + SoulToolLevelHandler.getToolLevel(stack) + " (Max Level)"));
        }
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return ImmutableSet.of(ToolType.get(toolName));
    }
}
