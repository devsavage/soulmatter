package io.savagedev.soulmatter.client.commands;

/*
 * CommandSMStealer.java
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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.savagedev.savagecore.nbt.NBTHelper;
import io.savagedev.savagecore.util.logger.LogHelper;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.items.SoulStealerItem;
import io.savagedev.soulmatter.util.ModNames;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;

public class CommandSMStealer
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> smStealerCommand = Commands.literal("smstealer")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("setsouls")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, SoulStealerItem.MAX_SOULS)).executes((context) -> handleAddSouls(context.getSource(), IntegerArgumentType.getInteger(context, "amount")))));

        dispatcher.register(smStealerCommand);
    }

    public static int handleAddSouls(CommandSourceStack source, int amount) {
        LogHelper.log(Level.DEBUG, amount);
        LogHelper.log(Level.DEBUG, source);
        if(source.getEntity() instanceof Player) {
            Player player = (Player) source.getEntity();

            if(!player.isCreative()) {
                source.sendFailure(Component.translatable("command.soulmatter.creative.generic.fail"));
                return 0;
            }

            if(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                ItemStack heldItemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if(!(heldItemStack.getItem() == ModItems.SOUL_STEALER.get())) {
                    source.sendFailure(Component.translatable("command.soulmatter.smstealer.addsouls.error"));
                    return 0;
                }

                if(amount <= 0 || amount > SoulStealerItem.MAX_SOULS) {
                    source.sendFailure(Component.translatable("command.soulmatter.smstealer.addsouls.invalid"));
                    return 0;
                }

                if(!NBTHelper.hasTag(heldItemStack, ModNames.Tags.SOULS_TAKEN)) {
                    NBTHelper.setInt(heldItemStack, ModNames.Tags.SOULS_TAKEN, amount);
                } else {
                    NBTHelper.setInt(heldItemStack, ModNames.Tags.SOULS_TAKEN, amount);
                }

                source.sendSuccess(() -> Component.translatable("command.soulmatter.smstealer.addsouls.success", amount), true);

                return 1;
            }
        }

        return 0;
    }
}
