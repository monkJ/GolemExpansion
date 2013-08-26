package com.monkj.mods.golemexpansion.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;

public class PotionFreeze extends Potion {
    
    public static PotionFreeze instance;
    
    public PotionFreeze(int par1, boolean par2, int par3) {
        super(par1, par2, par3);
    }
    
    public Potion setIconIndex(int i, int j){
        super.setIconIndex(i, j);
        return this;
    }
    
    public static void init(){
        instance=(PotionFreeze) new PotionFreeze(32, true, 0).setIconIndex(0, 0).setPotionName("potion.freeze");
        instance.func_111184_a(SharedMonsterAttributes.field_111263_d, "6a80c830-745d-4edd-8a17-e580f813bf20", -1D, 2);
    }
}
