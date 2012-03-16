package net.minecraft.src;

import java.awt.Color;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class RenderMyPlayer extends RenderPlayer{
	

	protected void renderPlayerScale(EntityPlayer entityplayer, float f)
    {
		EntityMyPerson myPerson = (EntityMyPerson)entityplayer;
        float f1 = 0.9375F * myPerson.scale;
        GL11.glScalef(f1, f1, f1);
    }
	
    protected void renderName(EntityPlayer entityplayer, double d, double d1, double d2)
    {
        if (Minecraft.isGuiEnabled() && entityplayer != renderManager.livingPlayer)
        {
    		EntityMyPerson myPerson = (EntityMyPerson)entityplayer;
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            float f2 = entityplayer.getDistanceToEntity(renderManager.livingPlayer);
            float f3 = entityplayer.isSneaking() ? 32F : 64F;
            if (f2 < f3)
            {
                String s = ((EntityMyPerson)entityplayer).name;
                if (!entityplayer.isSneaking())
                {
                    if (entityplayer.isPlayerSleeping())
                    {
                        renderLivingLabel(entityplayer, s, d, d1 - 1.5D, d2, 64);
                    }
                    else
                    {
                        renderLivingLabel(entityplayer, s, d, d1+(1.9f*myPerson.scale)-1.9f, d2, 64);
                    }
                }
                else
                {
                    FontRenderer fontrenderer = getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(2896 /*GL_LIGHTING*/);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(3042 /*GL_BLEND*/);
                    GL11.glBlendFunc(770, 771);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex(-i - 1, -1D, 0.0D);
                    tessellator.addVertex(-i - 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, -1D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 0x20ffffff);
                    GL11.glEnable(2896 /*GL_LIGHTING*/);
                    GL11.glDisable(3042 /*GL_BLEND*/);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
            }
        }
    }
    
    protected void renderLivingLabel(EntityLiving entityliving, String s, double d, double d1, double d2, int i)
    {
    	float healthPercent = ((float)entityliving.health)/((float)entityliving.getMaxHealth());
    	float red = (float)1f*(1f-healthPercent);
    	float green = (float)1*healthPercent;
    	Color c;
    	if(red >= 0 && green >= 0){
    		c = new Color(red, green, 0, .5f);
    	}else{
    		c = new Color(1, 0, 0, .5f);
    	}
    	int colour = c.getRGB();
    	String health = "("+entityliving.health+"/"+entityliving.getMaxHealth()+")";
        float f = entityliving.getDistanceToEntity(renderManager.livingPlayer);
        if (f > (float)i)
        {
            return;
        }
        FontRenderer fontrenderer = getFontRendererFromRenderManager();
        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f2, -f2, f2);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDepthMask(false);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        byte byte0 = 0;
        if (s.equals("deadmau5"))
        {
            byte0 = -10;
        }
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        tessellator.startDrawingQuads();
        int j = fontrenderer.getStringWidth(s) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-j - 1, -1 + byte0, 0.0D);
        tessellator.addVertex(-j - 1, 8 + byte0, 0.0D);
        tessellator.addVertex(j + 1, 8 + byte0, 0.0D);
        tessellator.addVertex(j + 1, -1 + byte0, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, 0x20ffffff);
        GL11.glTranslatef(0, -6.5f, 0);
        fontrenderer.drawString(health, -fontrenderer.getStringWidth(health) / 2, byte0, colour/*0xaa00cc00*/);
        GL11.glTranslatef(0, 6.5f, 0);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDepthMask(true);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, -1);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    
    
	

}
