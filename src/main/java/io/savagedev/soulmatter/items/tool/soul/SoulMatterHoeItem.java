package io.savagedev.soulmatter.items.tool.soul;

/*
 * SoulMatterHoeItem.java
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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import io.savagedev.soulmatter.SoulMatter;
import io.savagedev.soulmatter.handlers.SMToolLevelHandler;
import io.savagedev.soulmatter.init.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.item.HoeItem.changeIntoState;

public class SoulMatterHoeItem extends SMToolItem
{
//    private static Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{});
protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));
private static final TagKey<Block> EFFECTIVE_ON = BlockTags.MINEABLE_WITH_HOE;
    public SoulMatterHoeItem() {
        super("hoe", 1, EFFECTIVE_ON, properties -> new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(!context.getPlayer().mayUseItemAt(context.getClickedPos(), context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.FAIL;
        } else {
            if(context.getPlayer().isCrouching() && SMToolLevelHandler.isMaxToolLevel(context.getItemInHand())) {
                for(int posX = context.getClickedPos().getX() - 1; posX <= context.getClickedPos().getX() + 1; ++posX) {
                    for(int posZ = context.getClickedPos().getZ() - 1; posZ <= context.getClickedPos().getZ() + 1; ++posZ) {
                        BlockPos targetPos = new BlockPos(posX, context.getClickedPos().getY(), posZ);
                        BlockState targetBlock = context.getLevel().getBlockState(targetPos);

                        if(context.getClickedFace() != Direction.DOWN &&
                                context.getLevel().isEmptyBlock(targetPos.above()) &&
                                targetBlock.getBlock() == Blocks.GRASS_BLOCK || targetBlock.getBlock() == Blocks.DIRT) {
                            if(!context.getLevel().isClientSide()) {
                                context.getLevel().setBlockAndUpdate(targetPos, Blocks.FARMLAND.defaultBlockState());
                            }
                        }
                    }

                    context.getLevel().playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return InteractionResult.SUCCESS;
            }

            Level world = context.getLevel();
            BlockPos blockpos = context.getClickedPos();
            BlockState toolModifiedState = world.getBlockState(blockpos).getToolModifiedState(context, net.minecraftforge.common.ToolActions.HOE_TILL, false);
            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
            if (pair == null)
                return InteractionResult.PASS;
            if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
                BlockState blockstate = HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
                if (blockstate != null) {
                    Player player = context.getPlayer();
                    world.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!world.isClientSide()) {
                        world.setBlock(blockpos, blockstate, 11);
                        if (player != null) {
                            if(!SMToolLevelHandler.isMaxToolLevel(context.getItemInHand())) {
                                SMToolLevelHandler.addXp(context.getItemInHand(), player, Mth.nextInt(RandomSource.create(), 2, 8));

                                context.getItemInHand().hurtAndBreak(1, player, (hand) -> {
                                    hand.broadcastBreakEvent(context.getHand());
                                });
                            } else {
                                if(ModConfig.SHOULD_DAMAGE_MAX_TOOL.get()) {
                                    context.getItemInHand().hurtAndBreak(1, context.getPlayer(), (event) -> {
                                        event.broadcastBreakEvent(context.getPlayer().getUsedItemHand());
                                    });
                                } else {
                                    context.getItemInHand().hurtAndBreak(0, context.getPlayer(), (event) -> {
                                        event.broadcastBreakEvent(context.getPlayer().getUsedItemHand());
                                    });
                                }
                            }
                        }
                    }

                    return InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.PASS;
        }
    }
}
