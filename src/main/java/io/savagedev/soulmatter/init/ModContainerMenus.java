package io.savagedev.soulmatter.init;

/*
 * ModContainerMenus.java
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

import io.savagedev.soulmatter.menus.SoulEnchanterContainerMenu;
import io.savagedev.soulmatter.client.gui.SoulEnchanterScreen;
import io.savagedev.soulmatter.base.BaseContainerMenu;
import io.savagedev.soulmatter.util.ModNames;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmllegacy.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModContainerMenus
{
    public static final List<Supplier<MenuType<?>>> ENTRIES = new ArrayList<>();

    public static final RegistryObject<MenuType<SoulEnchanterContainerMenu>> SOUL_ENCHANTER = registerBlock(ModNames.Blocks.SOUL_ENCHANTER, ModBlockEntities.SOUL_ENCHANTER, SoulEnchanterContainerMenu::create);

    @SubscribeEvent
    public void onRegisterMenuTypes(RegistryEvent.Register<MenuType<?>> event) {
        IForgeRegistry<MenuType<?>> registry = event.getRegistry();

        ENTRIES.stream().map(Supplier::get).forEach(registry::register);
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup() {
        SOUL_ENCHANTER.ifPresent(menu -> MenuScreens.register(menu, SoulEnchanterScreen::new));
    }

    private static <T extends BlockEntity, C extends BaseContainerMenu> RegistryObject<MenuType<C>> registerBlock(String name, Supplier<BlockEntityType<T>> type, BaseContainerMenu.Factory<T, C> factory)
    {
        return register(name, (windowId, playerInventory, buffer) -> {
            final Level world = playerInventory.player.level;
            final BlockPos pos = buffer.readBlockPos();
            final T entity = world.getBlockEntity(pos, type.get()).orElseThrow();

            return factory.create(entity, playerInventory, windowId);
        });
    }

    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(String name, IContainerFactory<C> menu) {
        ResourceLocation location = new ResourceLocation(ModReference.MOD_ID, name);

        ENTRIES.add(() -> IForgeContainerType.create(menu).setRegistryName(location));

        return RegistryObject.of(location, ForgeRegistries.CONTAINERS);
    }
}
