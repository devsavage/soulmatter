package io.savagedev.soulmatter.init;

/*
 * ModConfiguration.java
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

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfiguration
{
    public static final ForgeConfigSpec CLIENT;
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec SERVER;

    static {
        final ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();

        CLIENT = client.build();
    }

    public static final ForgeConfigSpec.IntValue MAX_SOUL_TOOL_LEVEL;
    public static final ForgeConfigSpec.BooleanValue SHOULD_DAMAGE_MAX_TOOL;

    static {
        final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();

        common.comment("General configuration options.").push("General");

        MAX_SOUL_TOOL_LEVEL = common
                .comment("This number handles the Soul Matter Tool max tool level. When the tool reaches this number, it will have max abilities.")
                .translation("configGui.soulmatter.max_soul_tool_level")
                .defineInRange("maxSoulToolLevel", 3, 2, 8);
        SHOULD_DAMAGE_MAX_TOOL = common.comment("Damage Soul Matter Tools that have reached max level")
                .translation("configGui.soulmatter.should_damage_max_tool_level")
                .define("shouldDamageMaxTool", false);

        common.pop();

        COMMON = common.build();
    }

    static {
        final ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();

        SERVER = server.build();
    }
}
