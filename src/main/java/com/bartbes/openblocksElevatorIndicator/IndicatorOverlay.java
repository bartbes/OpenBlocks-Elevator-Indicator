package com.bartbes.openblocksElevatorIndicator;

import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.EntityClientPlayerMP;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class IndicatorOverlay extends Gui
{
	private static final int ICON_HEIGHT = 32;
	private static final int ICON_WIDTH = 32;
	private static final ResourceLocation ICON_RES =
		new ResourceLocation("openblockselevatorindicator:gui/obelevindicator.png");

	private static int elevatorRange;

	private Minecraft minecraft;
	private boolean onElevator = false;
	private boolean elevatorAbove = false;
	private boolean elevatorBelow = false;

	private int xPos = 0;
	private int yPos = 0;
	private int zPos = 0;
	private boolean moved = true;

	IndicatorOverlay()
	{
		super();

		minecraft = Minecraft.getMinecraft();

		// Find the OpenBlocks config
		boolean found = false;
		try
		{
			ClassLoader loader = this.getClass().getClassLoader();
			Class<?> confClass = loader.loadClass("openblocks.Config");

			Field rangeField = confClass.getField("elevatorTravelDistance");
			elevatorRange = rangeField.getInt(confClass);
			found = true;
		}
		catch(ClassNotFoundException e) {}
		catch(NoSuchFieldException e) {}
		catch(IllegalAccessException e) {}

		if (!found)
		{
			// Use the default value
			elevatorRange = 20;
			System.out.println("Failed to determine OpenBlocks elevator distance configuration value");
		}
	}

	private void updatePosition()
	{
		EntityClientPlayerMP player = minecraft.thePlayer;

		int x = MathHelper.floor_double(player.posX);
		int y = MathHelper.floor_double(player.posY-player.height);
		int z = MathHelper.floor_double(player.posZ);

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

	private void findElevators()
	{
		World world = minecraft.thePlayer.worldObj;
		Block elevatorBlock = IndicatorMod.instance.elevatorBlock;
		elevatorAbove = elevatorBelow = false;

		for (int i = yPos-1; i >= yPos - elevatorRange; i--)
			if (world.getBlock(xPos, i, zPos) == elevatorBlock)
			{
				elevatorBelow = true;
				break;
			}

		for (int i = yPos+1; i <= yPos + elevatorRange; i++)
			if (world.getBlock(xPos, i, zPos) == elevatorBlock)
			{
				elevatorAbove = true;
				break;
			}
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

		if (onElevator)
			findElevators();
	}

	// Drawing stuff
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
		int xPos = event.resolution.getScaledWidth()/2;
		// Represents bottom y
		int yPos = event.resolution.getScaledHeight();

		// Now position it just above the experience level
		yPos -= 40;

		// Our image is a bit large, so we'll scale it down
		GL11.glPushMatrix();
		GL11.glTranslatef(xPos, yPos, 0.0f);
		GL11.glScalef(0.25f, 0.25f, 1.0f);

		if (elevatorAbove)
			drawTexturedModalRect(-ICON_WIDTH, -ICON_HEIGHT,
					0, 0,
					ICON_WIDTH, ICON_HEIGHT);

		if (elevatorBelow)
		{
			GL11.glRotatef(180.0f, 0, 0, 0);
			drawTexturedModalRect(-ICON_WIDTH, 0,
					0, 0,
					ICON_WIDTH, ICON_HEIGHT);
		}

		// Now undo our transformation
		GL11.glPopMatrix();
	}
}
