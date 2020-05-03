package io.savagedev.soulmatter;

/*
 * SoulMatter.java
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

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.authlib.GameProfile;
import io.savagedev.soulmatter.handlers.MobDeathEventHandler;
import io.savagedev.soulmatter.handlers.MobDropsHandler;
import io.savagedev.soulmatter.init.*;
import io.savagedev.soulmatter.proxy.CommonProxy;
import io.savagedev.soulmatter.util.LogHelper;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.UUID;

@Mod(ModReference.MOD_ID)
public class SoulMatter
{
    public static CommonProxy proxy = new CommonProxy();

    public static GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes("soulmatter.common".getBytes()), LogHelper.TAG);

    public static ItemGroup modGroup = new ItemGroup(ModReference.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.SOUL_MATTER.get());
        }
    };

    public SoulMatter() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.register(this);
        modEventBus.register(new ModBlocks());
        modEventBus.register(new ModItems());
        modEventBus.register(new ModTileEntities());
        modEventBus.register(new ModContainers());

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfiguration.CLIENT);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfiguration.COMMON);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModConfiguration.SERVER);

        Path configPath = FMLPaths.CONFIGDIR.get().resolve("soulmatter-common.toml");
        CommentedFileConfig configData = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ModConfiguration.COMMON.setConfig(configData);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        ModContainers.onClientSetup();
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MobDropsHandler());
        MinecraftForge.EVENT_BUS.register(new MobDeathEventHandler());
    }
}
