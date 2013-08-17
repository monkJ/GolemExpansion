package com.monkj.mods.golemexpansion.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITasks;

public class AIHelper {
    
    public static void clearAITasks(EntityAITasks tasks,
            Class<? extends EntityAIBase>... toRemove) {
        List<EntityAIBase> temp;
        List<Class<? extends EntityAIBase>> classes = Arrays.asList(toRemove);
        if(toRemove==null || toRemove.length==0){
            temp=new ArrayList<EntityAIBase>(tasks.taskEntries);
        }else{
            temp=new ArrayList<EntityAIBase>();
            for (Object aiTemp : tasks.taskEntries) {
                EntityAIBase ai = ((EntityAITaskEntry) aiTemp).action;
                if (classes.contains(ai)) {
                    temp.add(ai);
                }
            }
        }
        for (EntityAIBase ai : temp) {
            tasks.removeTask(ai);
        }
    }
}
