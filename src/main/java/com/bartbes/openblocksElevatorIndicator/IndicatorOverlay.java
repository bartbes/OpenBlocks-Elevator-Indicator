package com.bartbes.openblocksElevatorIndicator;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.EntityClientPlayerMP;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class IndicatorOverlay extends Gui
{
	private static final int ICON_SIZE = 32;
	private static final int HALF_ICON_SIZE = ICON_SIZE/2;
	private static final ResourceLocation ICON_RES =
		new ResourceLocation("openblockselevatorindicator:gui/obelevindicator.png");

	private Minecraft minecraft;
	private boolean onElevator = false;

	private int xPos = 0;
	private int yPos = 0;
	private int zPos = 0;
	private boolean moved = true;

	IndicatorOverlay()
	{
		super();

		minecraft = Minecraft.getMinecraft();
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.phase != Phase.START)
			return;

		updatePosition();
		if (!moved)
			return;

		Block b = getBlockUnderPlayer();
		onElevator = (b == IndicatorMod.instance.elevatorBlock);
	}

	@SubscribeEvent
	public void onRenderIndicator(RenderGameOverlayEvent.Post event)
	{
		if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE)
			return;

		if (!onElevator)
			return;

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(GL11.GL_LIGHTING);
		minecraft.renderEngine.bindTexture(ICON_RES);

		// Represents center x
		int xpos = event.resolution.getScaledWidth()/2;
		// Represents bottom y
		int ypos = event.resolution.getScaledHeight() - 31;

		// Our image is a bit large, so we'll scale it down
		GL11.glPushMatrix();
		GL11.glTranslatef(xpos, ypos, 0.0f);
		GL11.glScalef(0.25f, 0.25f, 1.0f);

		drawTexturedModalRect(-HALF_ICON_SIZE, -ICON_SIZE,
				0, 0,
				ICON_SIZE, ICON_SIZE);

		// Now undo our transformation
		GL11.glPopMatrix();
	}

	private int round(double x)
	{
		// Round away from 0
		int y = (int) x;
		if (y < 0)
			return y-1;
		return y;
	}

	private void updatePosition()
	{
		EntityClientPlayerMP player = minecraft.thePlayer;

		int x = round(player.posX);
		int y = round(player.posY-player.height);
		int z = round(player.posZ);

		moved = (x != xPos || y != yPos || z != zPos);

		xPos = x;
		yPos = y;
		zPos = z;
	}

	private Block getBlockUnderPlayer()
	{
		World world = minecraft.thePlayer.worldObj;
		return world.getBlock(xPos, yPos, zPos);
	}
}
