package io.savagedev.soulmatter.client.commands;

/*
 * CommandSMTool.java
 * Copyright (C) 2014 - 2022 Savage - github.com/devsavage
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
import io.savagedev.soulmatter.handlers.SMToolLevelHandler;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CommandSMTool
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> smToolCommand = Commands.literal("smtool")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("maxlevel").executes((context) -> handleToolLevel(context.getSource())));

        dispatcher.register(smToolCommand);
    }

    public static int handleToolLevel(CommandSourceStack source) {
        if(source.getEntity() instanceof Player) {
            Player player = (Player) source.getEntity();

            if(!player.isCreative()) {
                source.sendFailure(Component.translatable("command.soulmatter.smtool.maxlevel.fail"));
                return 0;
            }

            if(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if(SMToolLevelHandler.hasLevelTags(heldItemStack)) {
                    if(!SMToolLevelHandler.isMaxToolLevel(heldItemStack)) {
                        SMToolLevelHandler.setMaxLevelCreative(heldItemStack, player);
                        return 1;
                    } else {
                        source.sendFailure(Component.translatable("command.soulmatter.smtool.maxlevel.pass"));
                        return 0;
                    }
                } else {
                    source.sendFailure(Component.translatable("command.soulmatter.smtool.maxlevel.error"));
                    return 0;
                }
            }
        }

        return 0;
    }
}
