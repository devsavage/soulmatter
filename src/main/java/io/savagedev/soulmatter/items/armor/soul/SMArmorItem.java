package io.savagedev.soulmatter.items.armor.soul;

/*
 * SMArmorItem.java
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

import io.savagedev.soulmatter.init.ModArmorMaterial;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SMArmorItem extends ArmorItem
{
    public SMArmorItem(EquipmentSlot slot, ArmorItem.Type type, Function<Properties, Properties> properties) {
        super(ModArmorMaterial.SOUL_MATTER, type, properties.apply(new Properties()));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if(this == ModItems.SOUL_MATTER_HELMET.get() || this == ModItems.SOUL_MATTER_CHESTPLATE.get() || this == ModItems.SOUL_MATTER_BOOTS.get()) {
            return ModReference.MOD_DOMAIN + "textures/models/armor/soul_matter_layer_1.png";
        } else if(this == ModItems.SOUL_MATTER_LEGGINGS.get()) {
            return ModReference.MOD_DOMAIN + "textures/models/armor/soul_matter_layer_2.png";
        } else {
            return null;
        }
    }
}
