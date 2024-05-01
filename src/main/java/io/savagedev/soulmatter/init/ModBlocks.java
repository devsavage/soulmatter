package io.savagedev.soulmatter.init;

/*
 * ModBlocks.java
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

import io.savagedev.soulmatter.blocks.SoulEnchanterBlock;
import io.savagedev.soulmatter.blocks.SoulPresserBlock;
import io.savagedev.soulmatter.items.blockitems.SoulEnchanterBlockItem;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModReference.MOD_ID);
    public static final RegistryObject<Block> SOUL_ENCHANTER = register(ModNames.Blocks.SOUL_ENCHANTER, SoulEnchanterBlock::new, block -> new SoulEnchanterBlockItem(block, new Item.Properties()));
    public static final RegistryObject<Block> SOUL_PRESSER = register(ModNames.Blocks.SOUL_PRESSER, SoulPresserBlock::new, block -> new SoulEnchanterBlockItem(block, new Item.Properties()));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return registerBlock(ModBlocks.BLOCKS, ModItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
    public static <T extends Block> RegistryObject<T> registerBlock(DeferredRegister<Block> blocks, DeferredRegister<Item> items, String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        final String actualName = name.toLowerCase(Locale.ROOT);
        final RegistryObject<T> block = blocks.register(actualName, blockSupplier);

        if (blockItemFactory != null) {
            items.register(actualName, () -> blockItemFactory.apply(block.get()));
        }

        return block;
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
