package io.savagedev.soulmatter.items.tool.soul;

/*
 * SoulMatterHammerItem.java
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.savagedev.savagecore.item.ItemHelper;
import io.savagedev.soulmatter.SoulMatter;
import io.savagedev.soulmatter.handlers.SMToolLevelHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;

import java.util.Set;

public class SoulMatterHammerItem extends SMToolItem
{
//    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of(Blocks.NETHER_GOLD_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.COPPER_BLOCK, Blocks.COPPER_ORE, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.PETRIFIED_OAK_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_STONE, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB);
    private static final TagKey<Block> EFFECTIVE_ON = BlockTags.MINEABLE_WITH_PICKAXE;
    public SoulMatterHammerItem() {
        super("hammer", 3, EFFECTIVE_ON, properties -> new Properties());
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if(SMToolLevelHandler.isMaxToolLevel(itemstack) && ForgeHooks.isCorrectToolForDrops(player.level().getBlockState(pos), player)) {
            for(BlockPos extraPos : (this).getAOEBlocks(itemstack, player.level(), player, pos)) {
                ItemHelper.destroyExtraAOEBlocks(itemstack, player.level(), player, extraPos, pos);
            }
        }

        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer().isCrouching()) {
            Level world = context.getLevel();
            BlockPos blockPos = new BlockPos(context.getClickedPos());
            BlockEntity blockEntity = world.getBlockEntity(blockPos);

//            if(blockEntity instanceof SoulPresserBlockEntity) {
//                ((SoulPresserBlockEntity) blockEntity).setActiveStatus(true);
//
//                return InteractionResult.SUCCESS;
//            }
        }

        return InteractionResult.PASS;
    }

    public ImmutableList<BlockPos> getAOEBlocks(ItemStack tool, Level world, Player playerEntity, BlockPos origin) {
        if(!ForgeHooks.isCorrectToolForDrops(world.getBlockState(origin), playerEntity)) {
            return ImmutableList.of();
        }

        return ItemHelper.calcAOEBlocks(tool, world, playerEntity, origin, 3, 3, 1);
    }
}
