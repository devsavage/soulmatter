package io.savagedev.soulmatter.helpers;

/*
 * ModFakePlayer.java
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

import com.mojang.authlib.GameProfile;
import io.savagedev.soulmatter.SoulMatter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;

@SuppressWarnings("EntityConstructor")
public class ModFakePlayer extends FakePlayer
{
    private static ModFakePlayer INSTANCE;

    public ModFakePlayer(ServerWorld world, GameProfile name) {
        super(world, name);
    }

    public static WeakReference<PlayerEntity> getInstance(ServerWorld world) {
        if (INSTANCE == null) {
            INSTANCE = new ModFakePlayer(world, SoulMatter.gameProfile);
        }
        INSTANCE.world = world;
        return new WeakReference<>(INSTANCE);
    }

    public static WeakReference<PlayerEntity> getInstance(ServerWorld world, double x, double y, double z) {
        if (INSTANCE == null) {
            INSTANCE = new ModFakePlayer(world, SoulMatter.gameProfile);
        }

        INSTANCE.world = world;
        INSTANCE.setRawPosition(x, y, z);
        return new WeakReference<>(INSTANCE);
    }

    public static void releaseInstance(IWorld world) {
        if (INSTANCE != null && INSTANCE.world == world) {
            INSTANCE = null;
        }
    }

    @Override
    public boolean isPotionApplicable(@Nonnull EffectInstance effect) {
        return false;
    }
}
