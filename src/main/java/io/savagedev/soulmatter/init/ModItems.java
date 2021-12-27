package io.savagedev.soulmatter.init;

/*
 * ModItems.java
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
import io.savagedev.soulmatter.items.*;
import io.savagedev.soulmatter.items.armor.soul.SMArmorItem;
import io.savagedev.soulmatter.items.tool.soul.*;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems
{
    public static final List<Supplier<? extends Item>> ENTRIES = new ArrayList<>();
    public static final List<Supplier<? extends Item>> BLOCK_ENTRIES = new ArrayList<>();

    public static final RegistryObject<SMItem> EMERALD_ROD = register(ModNames.Items.EMERALD_ROD, EmeraldRodItem::new);
    public static final RegistryObject<SMItem> SOUL_MATTER = register(ModNames.Items.SOUL_MATTER, SoulMatterItem::new);
    public static final RegistryObject<SMItem> RAW_SOUL_MATTER = register(ModNames.Items.RAW_SOUL_MATTER, RawSoulMatterItem::new);
    public static final RegistryObject<SMItem> SOUL_MATTER_COMPACT = register(ModNames.Items.SOUL_MATTER_COMPACT, SoulMatterCompactItem::new);
    public static final RegistryObject<SMItem> SOUL_STEALER = register(ModNames.Items.SOUL_STEALER, SoulStealerItem::new);

    public static final RegistryObject<SMToolItem> SOUL_MATTER_SWORD = register(ModNames.Items.SOUL_MATTER_SWORD, SoulMatterSwordItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_AXE = register(ModNames.Items.SOUL_MATTER_AXE, SoulMatterAxeItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_HAMMER = register(ModNames.Items.SOUL_MATTER_HAMMER, SoulMatterHammerItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_PICKAXE = register(ModNames.Items.SOUL_MATTER_PICKAXE, SoulMatterAxeItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_HOE = register(ModNames.Items.SOUL_MATTER_HOE, SoulMatterHoeItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_SHOVEL = register(ModNames.Items.SOUL_MATTER_SHOVEL, SoulMatterShovelItem::new);

    public static final RegistryObject<SMArmorItem> SOUL_MATTER_HELMET = register(ModNames.Items.SOUL_MATTER_HELMET, () -> new SMArmorItem(EquipmentSlot.HEAD, properties -> properties.tab(SoulMatter.creativeModeTab)));
    public static final RegistryObject<SMArmorItem> SOUL_MATTER_CHESTPLATE = register(ModNames.Items.SOUL_MATTER_CHESTPLATE, () -> new SMArmorItem(EquipmentSlot.CHEST, properties -> properties.tab(SoulMatter.creativeModeTab)));
    public static final RegistryObject<SMArmorItem> SOUL_MATTER_LEGGINGS = register(ModNames.Items.SOUL_MATTER_LEGGINGS, () -> new SMArmorItem(EquipmentSlot.LEGS, properties -> properties.tab(SoulMatter.creativeModeTab)));
    public static final RegistryObject<SMArmorItem> SOUL_MATTER_BOOTS = register(ModNames.Items.SOUL_MATTER_BOOTS, () -> new SMArmorItem(EquipmentSlot.FEET, properties -> properties.tab(SoulMatter.creativeModeTab)));

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        BLOCK_ENTRIES.stream().map(Supplier::get).forEach(registry::register);
        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends Item> item) {
        ResourceLocation loc = new ResourceLocation(ModReference.MOD_ID, name);

        ENTRIES.add(() -> item.get().setRegistryName(loc));

        return RegistryObject.of(loc, ForgeRegistries.ITEMS);
    }
}

