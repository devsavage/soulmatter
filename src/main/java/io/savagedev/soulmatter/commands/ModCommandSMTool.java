package io.savagedev.soulmatter.commands;

/*
 * ModCommandSMTool.java
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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.savagedev.soulmatter.handlers.SoulToolLevelHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;

public class ModCommandSMTool
{
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> smToolCommand = Commands.literal("smtool")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.literal("maxlevel").executes((context) -> handleToolLevel(context.getSource()))
        );

        dispatcher.register(smToolCommand);
    }

    private static int handleToolLevel(CommandSource commandSource) {
        if(commandSource.getEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)commandSource.getEntity();

            if(!playerEntity.isCreative()) {
                commandSource.sendErrorMessage(new TranslationTextComponent("command.soulmatter.smtool.maxlevel.fail"));
                return 0;
            }

            if(!playerEntity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                ItemStack heldItemStack = playerEntity.getHeldItem(Hand.MAIN_HAND).getStack();
                if(SoulToolLevelHandler.hasLevelTags(heldItemStack)) {
                    if(!SoulToolLevelHandler.isMaxToolLevel(heldItemStack)) {
                        SoulToolLevelHandler.setMaxLevelCreative(heldItemStack, playerEntity);
                        return 1;
                    } else {
                        commandSource.sendErrorMessage(new TranslationTextComponent("command.soulmatter.smtool.maxlevel.pass"));
                        return 0;
                    }
                } else {
                    commandSource.sendErrorMessage(new TranslationTextComponent("command.soulmatter.smtool.maxlevel.error"));
                    return 0;
                }
            }
        }

        return 0;
    }
}
