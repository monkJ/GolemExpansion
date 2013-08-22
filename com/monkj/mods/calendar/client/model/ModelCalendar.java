/**
 * File: ModelCalendar.java
 * created: 15.04.2013 - 10:45:52
 * by: RoboMat
 */

package com.monkj.mods.calendar.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCalendar extends ModelBase
{
    /** The calendar board on which to display the current date. */
    private ModelRenderer calendarBoard;

    public ModelCalendar()
    {
        float orgX = -10.0F;
        float orgY = -18.0F;
        float orgZ = -1.0F;
        int width = 20;
        int height = 20;
        int depth = 1;
        float scale = 0.0F;
        this.calendarBoard = new ModelRenderer(this, "calendar");
        this.calendarBoard.addBox(orgX, orgY, orgZ, width, height, depth, scale);
    }

    /**
     * Renders the calendar model through TileEntityCalendarRenderer
     */
    public void renderCalendar()
    {
        this.calendarBoard.render(0.0625F);
    }
}
