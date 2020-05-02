package io.savagedev.soulmatter.handlers;

/*
 * SoulToolLevelHandler.java
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

import io.savagedev.soulmatter.util.NBTHelper;
import net.minecraft.item.ItemStack;

public class SoulToolLevelHandler
{
    public static final String SOUL_TOOL_TAG = "SoulTool";
    public static final String SOUL_TOOL_TAG_XP = "SoulToolXP";
    public static final String SOUL_TOOL_TAG_LEVEL = "SoulToolLevel";
    public static final int SOUL_TOOL_MAX_LEVEL = 4; //TODO: Make this value configurable

    public static int getToolLevel(ItemStack soulTool) {
        return NBTHelper.getInt(soulTool, SOUL_TOOL_TAG_LEVEL);
    }

    public static boolean hasToolLevel(ItemStack soulTool) {
        return NBTHelper.hasTag(soulTool, SOUL_TOOL_TAG_LEVEL);
    }
}
