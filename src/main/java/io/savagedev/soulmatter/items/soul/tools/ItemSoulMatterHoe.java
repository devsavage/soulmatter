package io.savagedev.soulmatter.items.soul.tools;

/*
 * ItemSoulMatterHoe.java
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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.savagedev.soulmatter.handlers.SoulToolLevelHandler;
import io.savagedev.soulmatter.init.ModConfiguration;
import io.savagedev.soulmatter.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class ItemSoulMatterHoe extends BaseSoulTool
{
    private static Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{});
    protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(), Blocks.GRASS_PATH, Blocks.FARMLAND.getDefaultState(), Blocks.DIRT, Blocks.FARMLAND.getDefaultState(), Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState()));

    public ItemSoulMatterHoe(Function<Properties, Properties> properties) {
        super("hoe", 1, EFFECTIVE_ON, properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(!context.getPlayer().canPlayerEdit(context.getPos(), context.getFace(), context.getItem())) {
            return ActionResultType.FAIL;
        } else {
            if(context.getPlayer().isSneaking() && SoulToolLevelHandler.hasLevelTags(context.getItem()) && SoulToolLevelHandler.isMaxToolLevel(context.getItem())) {
                for(int posX = context.getPos().getX() - 1; posX <= context.getPos().getX() + 1; ++posX) {
                    for(int posZ = context.getPos().getZ() - 1; posZ <= context.getPos().getZ() + 1; ++posZ) {
                        BlockPos targetPos = new BlockPos(posX, context.getPos().getY(), posZ);
                        BlockState targetBlock = context.getWorld().getBlockState(targetPos);

                        if(context.getFace() != Direction.DOWN && context.getWorld().isAirBlock(targetPos.up()) && targetBlock.getBlock() == Blocks.GRASS_BLOCK || targetBlock.getBlock() == Blocks.DIRT) {
                            if(!context.getWorld().isRemote()) {
                                context.getWorld().setBlockState(targetPos, Blocks.FARMLAND.getDefaultState());
                            }
                        }
                    }

                    context.getWorld().playSound(context.getPlayer(), context.getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.SUCCESS;
            }

            World world = context.getWorld();
            BlockPos blockpos = context.getPos();
            int hook = ForgeEventFactory.onHoeUse(context);
            if (hook != 0)
                return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
            if (context.getFace() != Direction.DOWN && world.isAirBlock(blockpos.up())) {
                BlockState blockstate = HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
                if (blockstate != null) {
                    PlayerEntity playerentity = context.getPlayer();
                    world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockstate, 11);
                        if (playerentity != null) {
                            if(!SoulToolLevelHandler.isMaxToolLevel(context.getItem())) {
                                SoulToolLevelHandler.addXp(context.getItem(), playerentity, MathHelper.nextInt(new Random(), 2, 8));

                                context.getItem().damageItem(1, playerentity, (hand) -> {
                                    hand.sendBreakAnimation(context.getHand());
                                });
                            } else {
                                if(ModConfiguration.SHOULD_DAMAGE_MAX_TOOL.get()) {
                                    context.getItem().damageItem(1, context.getPlayer(), (event) -> {
                                        event.sendBreakAnimation(context.getPlayer().getActiveHand());
                                    });
                                } else {
                                    context.getItem().damageItem(0, context.getPlayer(), (event) -> {
                                        event.sendBreakAnimation(context.getPlayer().getActiveHand());
                                    });
                                }
                            }

                        }
                    }

                    return ActionResultType.SUCCESS;
                }
            }

            return ActionResultType.PASS;
        }
    }
}
