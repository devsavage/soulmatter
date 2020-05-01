package io.savagedev.soulmatter.init;

/*
 * ModItems.java
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

import io.savagedev.soulmatter.SoulMatter;
import io.savagedev.soulmatter.items.BaseItem;
import io.savagedev.soulmatter.items.ItemSoulStealer;
import io.savagedev.soulmatter.items.soul.armor.ItemSoulArmor;
import io.savagedev.soulmatter.items.soul.tools.*;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems
{
    public static final List<Supplier<? extends Item>> ENTRIES = new ArrayList<>();
    public static final List<Supplier<? extends Item>> BLOCK_ENTRIES = new ArrayList<>();

    public static final RegistryObject<BaseItem> RAW_SOUL_MATTER = register(ModNames.Items.RAW_SOUL_MATTER);
    public static final RegistryObject<BaseItem> SOUL_MATTER = register(ModNames.Items.SOUL_MATTER);
    public static final RegistryObject<BaseItem> SOUL_STEALER = register(ModNames.Items.SOUL_STEALER, () -> new ItemSoulStealer(p -> p.maxStackSize(1).group(SoulMatter.modGroup)));

    public static final RegistryObject<BaseSoulTool> SOUL_MATTER_SWORD = register(ModNames.Items.SOUL_MATTER_SWORD, () -> new ItemSoulMatterSword(p -> p.group(SoulMatter.modGroup)));
    public static final RegistryObject<BaseSoulTool> SOUL_MATTER_PICKAXE = register(ModNames.Items.SOUL_MATTER_PICKAXE, () -> new ItemSoulMatterPickaxe(p -> p.group(SoulMatter.modGroup).addToolType(ToolType.PICKAXE, ModToolTier.SOUL_MATTER.getHarvestLevel())));
    public static final RegistryObject<BaseSoulTool> SOUL_MATTER_AXE = register(ModNames.Items.SOUL_MATTER_AXE, () -> new ItemSoulMatterAxe(p -> p.group(SoulMatter.modGroup).addToolType(ToolType.AXE, ModToolTier.SOUL_MATTER.getHarvestLevel())));
    public static final RegistryObject<BaseSoulTool> SOUL_MATTER_SHOVEL = register(ModNames.Items.SOUL_MATTER_SHOVEL, () -> new ItemSoulMatterShovel(p -> p.group(SoulMatter.modGroup).addToolType(ToolType.SHOVEL, ModToolTier.SOUL_MATTER.getHarvestLevel())));
    public static final RegistryObject<BaseSoulTool> SOUL_MATTER_HOE = register(ModNames.Items.SOUL_MATTER_HOE, () -> new ItemSoulMatterHoe(p -> p.group(SoulMatter.modGroup)));
    public static final RegistryObject<BaseSoulTool> SOUL_MATTER_HAMMER = register(ModNames.Items.SOUL_MATTER_HAMMER, () -> new ItemSoulMatterHammer(p -> p.group(SoulMatter.modGroup)));

    public static final RegistryObject<ItemSoulArmor> SOUL_MATTER_HELMET = register(ModNames.Items.SOUL_MATTER_HELMET, () -> new ItemSoulArmor(EquipmentSlotType.HEAD, p -> p.group(SoulMatter.modGroup)));
    public static final RegistryObject<ItemSoulArmor> SOUL_MATTER_CHESTPLATE = register(ModNames.Items.SOUL_MATTER_CHESTPLATE, () -> new ItemSoulArmor(EquipmentSlotType.CHEST, p -> p.group(SoulMatter.modGroup)));
    public static final RegistryObject<ItemSoulArmor> SOUL_MATTER_LEGGINGS = register(ModNames.Items.SOUL_MATTER_LEGGINGS, () -> new ItemSoulArmor(EquipmentSlotType.LEGS, p -> p.group(SoulMatter.modGroup)));
    public static final RegistryObject<ItemSoulArmor> SOUL_MATTER_BOOTS = register(ModNames.Items.SOUL_MATTER_BOOTS, () -> new ItemSoulArmor(EquipmentSlotType.FEET, p -> p.group(SoulMatter.modGroup)));
    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        BLOCK_ENTRIES.stream().map(Supplier::get).forEach(registry::register);
        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    private static <T extends Item> RegistryObject<T> register(String name) {
        return register(name, () -> new BaseItem(p -> p.group(SoulMatter.modGroup)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends Item> item) {
        ResourceLocation loc = new ResourceLocation(ModReference.MOD_ID, name);
        ENTRIES.add(() -> item.get().setRegistryName(loc));
        return RegistryObject.of(loc, ForgeRegistries.ITEMS);
    }
}
