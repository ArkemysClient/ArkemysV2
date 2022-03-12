package com.rastiq.arkemys.features.modules;

import com.rastiq.arkemys.Client;
import com.rastiq.arkemys.features.modules.utils.DefaultModuleRenderer;
import net.minecraft.entity.Entity;

public class ComboCounterMod extends DefaultModuleRenderer
{
    public static ComboCounterMod INSTANCE;
    public long hitTime = -1;
    public int combo;
    public int possibleTarget;

    public ComboCounterMod() {
        super("Combo", 14);
        ComboCounterMod.INSTANCE = this;
    }

    @Override
    public Object getValue() {
        if((System.currentTimeMillis() - ComboCounterMod.INSTANCE.hitTime) > 2000) {
            combo = 0;
        }
        if (combo == 0) return "Aucun combo";
        return combo + " coup" + ((combo > 1) ? "s" : "");
    }

    @Override
    public Object getDummy() {
        return "Aucun combo";
    }

    @Override
    public String getFormat() {
        return "%value%";
    }

    public void onAttack(Entity entity) {
        possibleTarget = entity.getEntityId();
    }

    public void onDamage(Entity entity) {
        if(entity.getEntityId() == possibleTarget) {
            dealHit();
        }
        else if(entity == mc.thePlayer) {
            takeHit();
        }
    }

    public void dealHit() {
        combo++;
        possibleTarget = -1;
        hitTime = System.currentTimeMillis();
    }

    public void takeHit() {
        combo = 0;
    }
}
