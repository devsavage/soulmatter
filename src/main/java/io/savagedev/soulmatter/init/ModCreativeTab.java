package io.savagedev.soulmatter.init;

/*
 * ModCreativeTab.java
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

import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModCreativeTab
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModReference.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> SM_CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> SM_CREATIVE_TAB = TABS.register(ModNames.Tabs.SM_TAB, () -> CreativeModeTab.builder()
            .title(Component.translatable(ModNames.Tabs.SM_TAB_TITLE))
            .icon(ModItems.SOUL_MATTER.get()::getDefaultInstance)
            .displayItems((displayParams, output) -> {
        SM_CREATIVE_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get()));
    }).withSearchBar().build());

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        SM_CREATIVE_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == SM_CREATIVE_TAB.get()) {
            event.accept(ModItems.EMERALD_ROD.get());
            event.accept(ModItems.SOUL_MATTER.get());
            event.accept(ModItems.RAW_SOUL_MATTER.get());
            event.accept(ModItems.SOUL_MATTER_COMPACT.get());
            event.accept(ModItems.SOUL_STEALER.get());
            event.accept(ModItems.SOUL_MATTER_SWORD.get());
            event.accept(ModItems.SOUL_MATTER_AXE.get());
            event.accept(ModItems.SOUL_MATTER_HAMMER.get());
            event.accept(ModItems.SOUL_MATTER_PICKAXE.get());
            event.accept(ModItems.SOUL_MATTER_HOE.get());
            event.accept(ModItems.SOUL_MATTER_SHOVEL.get());
            event.accept(ModItems.SOUL_MATTER_HELMET.get());
            event.accept(ModItems.SOUL_MATTER_CHESTPLATE.get());
            event.accept(ModItems.SOUL_MATTER_LEGGINGS.get());
            event.accept(ModItems.SOUL_MATTER_BOOTS.get());
            event.accept(ModBlocks.SOUL_PRESSER.get());
            event.accept(ModBlocks.SOUL_ENCHANTER.get());
        }
    }
}
