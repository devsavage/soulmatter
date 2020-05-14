package io.savagedev.soulmatter.helpers;

/*
 * ToolHelper.java
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

import com.google.common.collect.ImmutableList;
import io.savagedev.soulmatter.items.soul.tools.ItemSoulMatterHammer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

public class ToolHelper
{
    public static ImmutableList<BlockPos> calcAOEBlocks(ItemStack stack, World world, PlayerEntity player, BlockPos origin, int width, int height, int depth) {
        return calcAOEBlocks(stack, world, player, origin, width, height, depth, -1);
    }

    public static ImmutableList<BlockPos> calcAOEBlocks(ItemStack stack, World world, PlayerEntity player, BlockPos origin, int width, int height, int depth, int distance) {
        if (stack == null || !(stack.getItem() instanceof ItemSoulMatterHammer))
            return ImmutableList.of();

        BlockState state = world.getBlockState(origin);
        Block block = state.getBlock();

        if (block.getMaterial(state) == Material.AIR) {
            return ImmutableList.of();
        }

        RayTraceResult mop = rayTrace(world, player, RayTraceContext.FluidMode.ANY);
        if (mop == null) {
            return ImmutableList.of();
        }

        int x, y, z;
        BlockPos start = origin;

        if(mop.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) mop;

            switch (blockRayTraceResult.getFace()) {
                case DOWN:
                case UP:
                    Vec3i vec = player.getHorizontalFacing().getDirectionVec();
                    x = vec.getX() * height + vec.getZ() * width;
                    y = blockRayTraceResult.getFace().getAxisDirection().getOffset() * -depth;
                    z = vec.getX() * width + vec.getZ() * height;
                    start = start.add(-x / 2, 0, -z / 2);
                    if (x % 2 == 0) {
                        if (x > 0 && blockRayTraceResult.getHitVec().getX() - blockRayTraceResult.getPos().getX() > 0.5d) start = start.add(1, 0, 0);
                        else if (x < 0 && blockRayTraceResult.getHitVec().getX() - blockRayTraceResult.getPos().getX() < 0.5d) start = start.add(-1, 0, 0);
                    }
                    if (z % 2 == 0) {
                        if (z > 0 && blockRayTraceResult.getHitVec().getZ() - blockRayTraceResult.getPos().getZ() > 0.5d) start = start.add(0, 0, 1);
                        else if (z < 0 && blockRayTraceResult.getHitVec().getZ() - blockRayTraceResult.getPos().getZ() < 0.5d) start = start.add(0, 0, -1);
                    }
                    break;
                case NORTH:
                case SOUTH:
                    x = width;
                    y = height;
                    z = blockRayTraceResult.getFace().getAxisDirection().getOffset() * -depth;
                    start = start.add(-x / 2, -y / 2, 0);
                    if (x % 2 == 0 && blockRayTraceResult.getHitVec().getX() - blockRayTraceResult.getPos().getX() > 0.5d) start = start.add(1, 0, 0);
                    if (y % 2 == 0 && blockRayTraceResult.getHitVec().getY() - blockRayTraceResult.getPos().getY() > 0.5d) start = start.add(0, 1, 0);
                    break;
                case WEST:
                case EAST:
                    x = blockRayTraceResult.getFace().getAxisDirection().getOffset() * -depth;
                    y = height;
                    z = width;
                    start = start.add(-0, -y / 2, -z / 2);
                    if (y % 2 == 0 && blockRayTraceResult.getHitVec().getY() - blockRayTraceResult.getPos().getY() > 0.5d) start = start.add(0, 1, 0);
                    if (z % 2 == 0 && blockRayTraceResult.getHitVec().getZ() - blockRayTraceResult.getPos().getZ() > 0.5d) start = start.add(0, 0, 1);
                    break;
                default:
                    x = y = z = 0;
            }

            ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
            for (int xp = start.getX(); xp != start.getX() + x; xp += x / MathHelper.abs(x)) {
                for (int yp = start.getY(); yp != start.getY() + y; yp += y / MathHelper.abs(y)) {
                    for (int zp = start.getZ(); zp != start.getZ() + z; zp += z / MathHelper.abs(z)) {
                        if (xp == origin.getX() && yp == origin.getY() && zp == origin.getZ()) {
                            continue;
                        }
                        if (distance > 0 && MathHelper.abs(xp - origin.getX()) + MathHelper.abs(yp - origin.getY()) + MathHelper.abs(
                                zp - origin.getZ()) > distance) {
                            continue;
                        }
                        BlockPos pos = new BlockPos(xp, yp, zp);
                        if (isToolHammerAndEffective(stack, world.getBlockState(pos))) {
                            builder.add(pos);
                        }
                    }
                }
            }

            return builder.build();
        }

        return ImmutableList.of();
    }

    public static boolean isToolEffective(ItemStack stack, BlockState state) {
        for (ToolType type : stack.getItem().getToolTypes(stack)) {
            if (state.getBlock().isToolEffective(state, type)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isToolHammerAndEffective(ItemStack stack, BlockState state) {
        if (isToolEffective(stack, state))
            return true;

        return stack.getItem() instanceof ItemSoulMatterHammer && (stack.getItem()).canHarvestBlock(state);
    }

    public static void destroyExtraAOEBlocks(ItemStack stack, World world, PlayerEntity player, BlockPos pos, BlockPos refPos) {
        if (world.isAirBlock(pos))
            return;

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!isToolHammerAndEffective(stack, state)) {
            return;
        }

        BlockState refState = world.getBlockState(refPos);
        float refStrength = blockStrength(refState, player, world, refPos);
        float strength = blockStrength(state, player, world, pos);

        if (!ForgeHooks.canHarvestBlock(state, player, world, pos) || refStrength / strength > 10f)
            return;

        if (player.isCreative()) {
            block.onBlockHarvested(world, pos, state, player);
            if(block.removedByPlayer(state, world, pos, player, false, state.getFluidState()))
                block.onPlayerDestroy(world, pos, state);

            if (!world.isRemote) {
                ((ServerPlayerEntity) player).connection.sendPacket(new SChangeBlockPacket(world, pos));
            }

            return;
        }

        stack.onBlockDestroyed(world, state, pos, player);

        if (!world.isRemote) {
            int xp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, pos);

            if (xp == -1) {
                return;
            }

            block.onBlockHarvested(world, pos, state, player);

            if (block.removedByPlayer(state, world, pos, player, true, state.getFluidState())) {
                block.onPlayerDestroy(world, pos, state);
                block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
                block.dropXpOnBlockBreak(world, pos, xp);
            }

            ServerPlayerEntity mpPlayer = (ServerPlayerEntity) player;
            mpPlayer.connection.sendPacket(new SChangeBlockPacket(world, pos));
        } else {
            world.playBroadcastSound(2001, pos, Block.getStateId(state));

            if (block.removedByPlayer(state, world, pos, player, true, state.getFluidState())) {
                block.onPlayerDestroy(world, pos, state);
            }

            stack.onBlockDestroyed(world, state, pos, player);

            if (stack.getMaxStackSize() == 0 && stack == player.getHeldItemMainhand()) {
                ForgeEventFactory.onPlayerDestroyItem(player, stack, Hand.MAIN_HAND);
                player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            }

            Minecraft.getInstance().getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN));
        }
    }

    public static RayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        Vec3d vec3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();;
        Vec3d vec3d1 = vec3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return worldIn.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }

    public static float blockStrength(BlockState state, PlayerEntity player, World world, BlockPos pos)
    {
        float hardness = state.getBlockHardness(world, pos);
        if (hardness < 0.0F) {
            return 0.0F;
        }

        if (!ForgeHooks.canHarvestBlock(state, player, world, pos)) {
            return player.getDigSpeed(state, pos) / hardness / 100F;
        } else {
            return player.getDigSpeed(state, pos) / hardness / 30F;
        }
    }
}
