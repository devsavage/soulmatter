package io.savagedev.soulmatter.init;

/*
 * ModContainerMenus.java
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


import io.savagedev.soulmatter.base.BaseContainerMenu;
import io.savagedev.soulmatter.blocks.entity.SoulEnchanterBlockEntity;
import io.savagedev.soulmatter.client.gui.SoulEnchanterScreen;
import io.savagedev.soulmatter.client.gui.SoulPresserScreen;
import io.savagedev.soulmatter.menus.SoulEnchanterContainerMenu;
import io.savagedev.soulmatter.menus.SoulPresserContainerMenu;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModContainerMenus
{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ModReference.MOD_ID);
    public static final RegistryObject<MenuType<SoulEnchanterContainerMenu>> SOUL_ENCHANTER = registerBlock(ModNames.Blocks.SOUL_ENCHANTER, ModBlockEntities.SOUL_ENCHANTER, SoulEnchanterContainerMenu::create);
    public static final RegistryObject<MenuType<SoulPresserContainerMenu>> SOUL_PRESSER = registerBlock(ModNames.Blocks.SOUL_PRESSER, ModBlockEntities.SOUL_PRESSER, SoulPresserContainerMenu::create);

    private static <T extends BaseContainerBlockEntity, C extends BaseContainerMenu> RegistryObject<MenuType<C>> registerBlock(String name, Supplier<BlockEntityType<T>> type, BaseContainerMenu.Factory<T, C> factory)
    {
        return register(MENUS, name, (windowId, playerInventory, buffer) -> {
            final Level level = playerInventory.player.level();
            final BlockPos pos = buffer.readBlockPos();
            final T entity = level.getBlockEntity(pos, type.get()).orElseThrow();

            return factory.create(entity, playerInventory, windowId);
        });
    }
    public static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(DeferredRegister<MenuType<?>> containers, String name, IContainerFactory<C> factory)
    {
        return containers.register(name, () -> IForgeMenuType.create(factory));
    }
}
