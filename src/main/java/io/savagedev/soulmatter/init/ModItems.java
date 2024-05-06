package io.savagedev.soulmatter.init;

/*
 * ModItems.java
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

import io.savagedev.soulmatter.SoulMatter;
import io.savagedev.soulmatter.items.*;
import io.savagedev.soulmatter.items.armor.soul.SMArmorItem;
import io.savagedev.soulmatter.items.tool.soul.*;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModReference.MOD_ID);
    public static final RegistryObject<SMItem> EMERALD_ROD = register(ModNames.Items.EMERALD_ROD, EmeraldRodItem::new);
    public static final RegistryObject<SMItem> SOUL_MATTER = register(ModNames.Items.SOUL_MATTER, SoulMatterItem::new);
    public static final RegistryObject<SMItem> RAW_SOUL_MATTER = register(ModNames.Items.RAW_SOUL_MATTER, RawSoulMatterItem::new);
    public static final RegistryObject<SMItem> SOUL_MATTER_COMPACT = register(ModNames.Items.SOUL_MATTER_COMPACT, SoulMatterCompactItem::new);
    public static final RegistryObject<SMItem> SOUL_STEALER = register(ModNames.Items.SOUL_STEALER, SoulStealerItem::new);
    public static final RegistryObject<SMItem> COBBLE_ROD = register(ModNames.Items.COBBLE_ROD, CobbleRodItem::new);

    public static final RegistryObject<SMToolItem> SOUL_MATTER_SWORD = register(ModNames.Items.SOUL_MATTER_SWORD, SoulMatterSwordItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_AXE = register(ModNames.Items.SOUL_MATTER_AXE, SoulMatterAxeItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_HAMMER = register(ModNames.Items.SOUL_MATTER_HAMMER, SoulMatterHammerItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_PICKAXE = register(ModNames.Items.SOUL_MATTER_PICKAXE, SoulMatterAxeItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_HOE = register(ModNames.Items.SOUL_MATTER_HOE, SoulMatterHoeItem::new);
    public static final RegistryObject<SMToolItem> SOUL_MATTER_SHOVEL = register(ModNames.Items.SOUL_MATTER_SHOVEL, SoulMatterShovelItem::new);

    public static final RegistryObject<SMArmorItem> SOUL_MATTER_HELMET = register(ModNames.Items.SOUL_MATTER_HELMET, () -> new SMArmorItem(EquipmentSlot.HEAD, ArmorItem.Type.HELMET, properties -> properties));
    public static final RegistryObject<SMArmorItem> SOUL_MATTER_CHESTPLATE = register(ModNames.Items.SOUL_MATTER_CHESTPLATE, () -> new SMArmorItem(EquipmentSlot.CHEST, ArmorItem.Type.CHESTPLATE, properties -> properties));
    public static final RegistryObject<SMArmorItem> SOUL_MATTER_LEGGINGS = register(ModNames.Items.SOUL_MATTER_LEGGINGS, () -> new SMArmorItem(EquipmentSlot.LEGS, ArmorItem.Type.LEGGINGS, properties -> properties));
    public static final RegistryObject<SMArmorItem> SOUL_MATTER_BOOTS = register(ModNames.Items.SOUL_MATTER_BOOTS, () -> new SMArmorItem(EquipmentSlot.FEET, ArmorItem.Type.BOOTS, properties -> properties));

    private static RegistryObject<Item> register(String name) {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}

