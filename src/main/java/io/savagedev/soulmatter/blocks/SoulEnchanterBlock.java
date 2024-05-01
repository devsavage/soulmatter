package io.savagedev.soulmatter.blocks;

/*
 * SoulEnchanterBlock.java
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


import io.savagedev.soulmatter.blocks.entity.SoulEnchanterBlockEntity;
import io.savagedev.soulmatter.init.ModBlockEntities;
import io.savagedev.soulmatter.menus.SoulEnchanterContainerMenu;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class SoulEnchanterBlock extends BaseEntityBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SoulEnchanterBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.ANVIL).strength(4.0F).noOcclusion().explosionResistance(10.0F));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void onRemove(BlockState blockState, Level world, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (!blockState.is(newBlockState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(blockPos);
            if (blockentity instanceof SoulEnchanterBlockEntity) {
                if (world instanceof ServerLevel) {
                    Containers.dropContents(world, blockPos, (SoulEnchanterBlockEntity) blockentity);
                }

                world.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(blockState, world, blockPos, newBlockState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            final SoulEnchanterBlockEntity soulEnchanter = world.getBlockEntity(blockPos, ModBlockEntities.SOUL_ENCHANTER.get()).orElse(null);

            if(soulEnchanter != null) {
                if (player instanceof ServerPlayer serverPlayer) {
                    NetworkHooks.openScreen(serverPlayer, soulEnchanter, blockPos);
                }

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.SOUL_ENCHANTER.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createFurnaceTicker(world, blockEntityType, ModBlockEntities.SOUL_ENCHANTER.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level world, BlockEntityType<T> blockEntityType, BlockEntityType<? extends SoulEnchanterBlockEntity> soulEnchanterEntity) {
        return world.isClientSide ? null : createTickerHelper(blockEntityType, soulEnchanterEntity, SoulEnchanterBlockEntity::serverTick);
    }
}
