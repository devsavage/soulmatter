package io.savagedev.soulmatter.items.soul.armor;

/*
 * ItemSoulArmor.java
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

import io.savagedev.soulmatter.init.ModArmorMaterial;
import io.savagedev.soulmatter.init.ModItems;
import io.savagedev.soulmatter.util.ModReference;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ItemSoulArmor extends ArmorItem
{
    public ItemSoulArmor(EquipmentSlotType slot, Function<Properties, Properties> properties) {
        super(ModArmorMaterial.SOUL_MATTER, slot, properties.apply(new Properties()));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if(this == ModItems.SOUL_MATTER_HELMET.get() || this == ModItems.SOUL_MATTER_CHESTPLATE.get() || this == ModItems.SOUL_MATTER_BOOTS.get()) {
            return ModReference.MOD_DOMAIN + "textures/models/armor/soul_matter_layer_1.png";
        } else if(this == ModItems.SOUL_MATTER_LEGGINGS.get()) {
            return ModReference.MOD_DOMAIN + "textures/models/armor/soul_matter_layer_2.png";
        } else {
            return null;
        }
    }
}
