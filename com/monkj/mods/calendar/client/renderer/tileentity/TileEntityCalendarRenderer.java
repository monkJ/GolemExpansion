/**
 * File: TileEntityCalendarRenderer.java
 * created: 15.04.2013 - 10:31:32
 * by: RoboMat
 */

package com.monkj.mods.calendar.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.monkj.mods.calendar.Calendar;
import com.monkj.mods.calendar.client.model.ModelCalendar;
import com.monkj.mods.calendar.tileentity.TileEntityCalendar;
import com.monkj.mods.calendar.utility.DateHandler;

public class TileEntityCalendarRenderer extends TileEntitySpecialRenderer
{
    private ModelCalendar modelCal = new ModelCalendar();
    
    private ResourceLocation calendarTexture = new ResourceLocation("calendar:textures/entity/entityCalendar.png");

    public void renderTileEntityCalendarAt(TileEntityCalendar tileEntityCalendar, double x, double y, double z, float par8)
    {
        /* Save current matrix by creating a new matrix on top of the stack */
        GL11.glPushMatrix();
        
        float factor = 0.6666667F;
        
        /* Rotation */
        float angle;		// angle to rotate
        float rotX = 0.0F;	// x pos
        float rotY = 1.0F;	// y pos
        float rotZ = 0.0F;	// z pos
        
        /* Type cast parameters
         * X: right (+) and left (-) of display
         * Y: top (+) and bottom (-) of display
         * Z: towards(+) and from (-) viewer of display */
        float posX = (float) x + 0.5F;
        float posY = (float) y + 0.75F * factor;
        float posZ = (float) z + 0.5F;
        
        /* Rotate calendar when placed on block */
        int blockMetaData = tileEntityCalendar.getBlockMetadata();
        angle = 0.0F;

        if (blockMetaData == 2)
        {
            angle = 180.0F;
        }

        if (blockMetaData == 4)
        {
            angle = -90.0F;
        }

        if (blockMetaData == 5)
        {
            angle = 90.0F;
        }

        /* First position */
        GL11.glTranslatef(posX, posY, posZ);
        
        /* Rotate entity */
        GL11.glRotatef(angle, rotX, rotY, rotZ);
        
        /* Second position relative to first position */
        GL11.glTranslatef(0.0F, -0.3125F, -0.4985F);

        /**Texture of the tile entity**/
        Minecraft.getMinecraft().func_110434_K().func_110577_a(calendarTexture);
        /* create new matrix */
        GL11.glPushMatrix();
        GL11.glScalef(factor, -factor, -factor);
        this.modelCal.renderCalendar();
        GL11.glPopMatrix();
        
        /* Related to the text of the calendar */
        FontRenderer fontRenderer = this.getFontRenderer();
        angle = 0.016666668F * factor;
        GL11.glTranslatef(0.0F, 0.5F * factor, 0.07F * factor);
        GL11.glScalef(angle, -angle, angle);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * angle);
        GL11.glDepthMask(false);
        int colour = 0;	// black
        DateHandler calc = Calendar.instance.getCalculator();
        String[] drawDate = calc.returnDateForCalendarBlock();

        for (int i = 0; i < drawDate.length; i++)
        {
            String strToDraw = drawDate[i];
            fontRenderer.drawString(strToDraw, -fontRenderer.getStringWidth(strToDraw) / 2, (i * 9 - drawDate.length * 10) + 40, colour);
        }

        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    /**
     * Abstract render method from TileEntitySpecialRenderer call render method
     * from this renderer.
     */
    public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderTileEntityCalendarAt((TileEntityCalendar) tileEntity, par2, par4, par6, par8);
    }
    
}
