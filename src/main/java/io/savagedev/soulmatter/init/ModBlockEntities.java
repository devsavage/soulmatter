package io.savagedev.soulmatter.init;

/*
 * ModBlockEntities.java
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

import io.savagedev.soulmatter.blocks.SoulEnchanterBlock;
import io.savagedev.soulmatter.blocks.SoulEnchanterBlockEntity;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModBlockEntities
{
    public static final List<Supplier<BlockEntityType<?>>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<BlockEntityType<SoulEnchanterBlockEntity>> SOUL_ENCHANTER = register(ModNames.Blocks.SOUL_ENCHANTER, SoulEnchanterBlockEntity::new, SoulEnchanterBlock::new);

    @SubscribeEvent
    public void onRegisterTypes(RegistryEvent.Register<BlockEntityType<?>> event) {
        IForgeRegistry<BlockEntityType<?>> registry = event.getRegistry();

        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> blockEntity, Stream<? extends Supplier<? extends Block>> blocks) {
        ResourceLocation location = new ResourceLocation(ModReference.MOD_ID, name);

        ENTRIES.add(() -> BlockEntityType.Builder.of(blockEntity, blocks.map(Supplier::get).toArray(Block[]::new)).build(null).setRegistryName(name));

        return RegistryObject.of(location, ForgeRegistries.BLOCK_ENTITIES);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> block) {
        ResourceLocation location = new ResourceLocation(ModReference.MOD_ID, name);

        ENTRIES.add(() -> BlockEntityType.Builder.of(supplier, block.get()).build(null).setRegistryName(location));

        return RegistryObject.of(location, ForgeRegistries.BLOCK_ENTITIES);
    }
}
