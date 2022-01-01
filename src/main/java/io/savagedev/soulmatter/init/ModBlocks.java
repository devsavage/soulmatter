package io.savagedev.soulmatter.init;

/*
 * ModBlocks.java
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

import io.savagedev.soulmatter.SoulMatter;
import io.savagedev.soulmatter.blocks.SoulEnchanterBlock;
import io.savagedev.soulmatter.blocks.SoulPresserBlock;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks
{
    public static final List<Supplier<? extends Block>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<SoulEnchanterBlock> SOUL_ENCHANTER = register(ModNames.Blocks.SOUL_ENCHANTER, SoulEnchanterBlock::new);
    public static final RegistryObject<SoulPresserBlock> SOUL_PRESSER = register(ModNames.Blocks.SOUL_PRESSER, SoulPresserBlock::new);

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, b -> () -> new BlockItem(b.get(), new Item.Properties().tab(SoulMatter.creativeModeTab)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<? extends BlockItem>> item) {
        ResourceLocation loc = new ResourceLocation(ModReference.MOD_ID, name);

        ENTRIES.add(() -> block.get().setRegistryName(loc));

        RegistryObject<T> registryObject = RegistryObject.of(loc, ForgeRegistries.BLOCKS);

        ModItems.BLOCK_ENTRIES.add(() -> item.apply(registryObject).get().setRegistryName(loc));

        return registryObject;
    }
}
