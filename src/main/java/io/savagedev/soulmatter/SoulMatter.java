package io.savagedev.soulmatter;

/*
 * SoulMatter.java
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

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import io.savagedev.savagecore.util.updater.Updater;
import io.savagedev.savagecore.util.updater.UpdaterUtils;
import io.savagedev.soulmatter.handlers.events.MobDeathEventHandler;
import io.savagedev.soulmatter.handlers.events.MobDropsHandler;
import io.savagedev.soulmatter.handlers.events.ModPlayerLoggedInEvent;
import io.savagedev.soulmatter.init.*;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.versions.mcp.MCPVersion;

import java.nio.file.Path;

@Mod(ModReference.MOD_ID)
public class SoulMatter
{
    public static ModContainer MOD_CONTAINER;

    public Updater UPDATER;

    public SoulMatter() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.register(this);
        modEventBus.register(new ModBlocks());
        modEventBus.register(new ModItems());
        modEventBus.register(new ModBlockEntities());
        modEventBus.register(new ModContainerMenus());

        MOD_CONTAINER = ModLoadingContext.get().getActiveContainer();
        UPDATER = new Updater()
                .setModId(ModReference.MOD_ID)
                .setMinecraftVersion(MCPVersion.getMCVersion())
                .setCurrentVersion(MOD_CONTAINER.getModInfo().getVersion().toString());

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER);

        Path configPath = FMLPaths.CONFIGDIR.get().resolve("soulmatter-common.toml");
        CommentedFileConfig configData = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ModConfig.COMMON.setConfig(configData);

        MinecraftForge.EVENT_BUS.register(new ModCommands());
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        ModContainerMenus.onClientSetup();
    }

    public static CreativeModeTab creativeModeTab = new CreativeModeTab(ModReference.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SOUL_MATTER.get());
        }
    };

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MobDeathEventHandler());
        MinecraftForge.EVENT_BUS.register(new MobDropsHandler());

        UpdaterUtils.initializeUpdateCheck(UPDATER);

        MinecraftForge.EVENT_BUS.register(new ModPlayerLoggedInEvent(UPDATER));
    }
}
