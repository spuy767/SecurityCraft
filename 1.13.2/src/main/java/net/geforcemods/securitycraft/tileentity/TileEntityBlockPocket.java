package net.geforcemods.securitycraft.tileentity;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.api.CustomizableSCTE;
import net.geforcemods.securitycraft.api.Option;
import net.geforcemods.securitycraft.misc.EnumCustomModules;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class TileEntityBlockPocket extends CustomizableSCTE
{
	private TileEntityBlockPocketManager manager;

	public TileEntityBlockPocket()
	{
		super(SCContent.teTypeBlockPocket);
	}

	public void setManager(TileEntityBlockPocketManager manager)
	{
		this.manager = manager;
	}

	public void removeManager()
	{
		manager = null;
	}

	public TileEntityBlockPocketManager getManager()
	{
		return manager;
	}

	@Override
	public void remove()
	{
		super.remove();

		if(manager != null)
			manager.disableMultiblock();
	}

	@Override
	public void onTileEntityDestroyed()
	{
		super.onTileEntityDestroyed();

		if(manager != null)
			manager.disableMultiblock();
	}

	@Override
	public NBTTagCompound write(NBTTagCompound tag)
	{
		if(manager != null)
			tag.putLong("ManagerPos", manager.getPos().toLong());
		return super.write(tag);
	}

	@Override
	public void read(NBTTagCompound tag)
	{
		super.read(tag);

		if(tag.contains("ManagerPos"))
		{
			TileEntity te = world.getTileEntity(BlockPos.fromLong(tag.getLong("ManagerPos")));

			if(te instanceof TileEntityBlockPocketManager)
				manager = (TileEntityBlockPocketManager)te;
		}
	}

	@Override
	public EnumCustomModules[] acceptedModules()
	{
		return new EnumCustomModules[] {};
	}

	@Override
	public Option<?>[] customOptions()
	{
		return null;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getCustomName()
	{
		return null;
	}
}