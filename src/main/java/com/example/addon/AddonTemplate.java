package com.example.addon;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import java.util.List;
import java.util.ArrayList;

public class AddonTemplate extends MeteorAddon {
    public static final Category CATEGORY = new Category("Custom");

    @Override
    public void onInitialize() {
        Modules.get().add(new UniversalTracer());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }

    public static class UniversalTracer extends Module {
        private final SettingGroup sgGeneral = settings.getDefaultGroup();

        private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
            .name("items")
            .defaultValue(new ArrayList<>())
            .build()
        );

        private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
            .name("color")
            .defaultValue(new SettingColor(0, 255, 255, 255))
            .build()
        );

        public UniversalTracer() {
            super(CATEGORY, "tracer", "Eşyaları takip eder.");
        }

        @EventHandler
        private void onRender(Render3DEvent event) {
            if (mc.world == null || mc.player == null || event.renderer == null) return;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity item) {
                    if (items.get().contains(item.getStack().getItem())) {
                        event.renderer.line(
                            mc.player.getX(), mc.player.getEyeY(), mc.player.getZ(),
                            item.getX(), item.getY(), item.getZ(),
                            color.get()
                        );
                    }
                }
            }
        }
    }
}
