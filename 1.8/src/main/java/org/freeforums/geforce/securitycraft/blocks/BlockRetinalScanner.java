package org.freeforums.geforce.securitycraft.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.freeforums.geforce.securitycraft.interfaces.IHelpInfo;
import org.freeforums.geforce.securitycraft.main.Utils;
import org.freeforums.geforce.securitycraft.tileentity.TileEntityOwnable;
import org.freeforums.geforce.securitycraft.tileentity.TileEntityRetinalScanner;

public class BlockRetinalScanner extends BlockContainer implements IHelpInfo {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockRetinalScanner(Material par1) {
		super(par1);
	}
	
	public int getRenderType(){
		return 3;
	}

	/**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, BlockPos pos, IBlockState state, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack){
        ((TileEntityOwnable)par1World.getTileEntity(pos)).setOwner(((EntityPlayer) par5EntityLivingBase).getGameProfile().getId().toString(), par5EntityLivingBase.getName());

        Block block = par1World.getBlockState(pos.north()).getBlock();
        Block block1 = par1World.getBlockState(pos.south()).getBlock();
        Block block2 = par1World.getBlockState(pos.west()).getBlock();
        Block block3 = par1World.getBlockState(pos.east()).getBlock();
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
        {
            enumfacing = EnumFacing.SOUTH;
        }
        else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
        {
            enumfacing = EnumFacing.NORTH;
        }
        else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
        {
            enumfacing = EnumFacing.EAST;
        }
        else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
        {
            enumfacing = EnumFacing.WEST;
        }

        par1World.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
      
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, BlockPos pos, IBlockState state, Random par5Random)
    {
        if (!par1World.isRemote && ((Boolean) state.getValue(POWERED)).booleanValue()){
        	String ownerUUID = ((TileEntityRetinalScanner) par1World.getTileEntity(pos)).getOwnerUUID();
        	String ownerName = ((TileEntityRetinalScanner) par1World.getTileEntity(pos)).getOwnerName();
        	Utils.setBlockProperty(par1World, pos, POWERED, false);
        	((TileEntityRetinalScanner) par1World.getTileEntity(pos)).setOwner(ownerUUID, ownerName);
        }                       
    }
    
    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }
    
    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, BlockPos pos, IBlockState state, EnumFacing side)
    {
    	if(((Boolean) state.getValue(POWERED)).booleanValue()){
    		return 15;
    	}else{
    		return 0;
    	}
    }
    
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, false);
    }
    
    @SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        if(meta <= 5){
        	return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta].getAxis() == EnumFacing.Axis.Y ? EnumFacing.NORTH : EnumFacing.values()[meta]).withProperty(POWERED, false);
        }else{
        	return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta - 6]).withProperty(POWERED, true);
        }
    }

    public int getMetaFromState(IBlockState state)
    {
    	if(((Boolean) state.getValue(POWERED)).booleanValue()){
    		return (((EnumFacing) state.getValue(FACING)).getIndex() + 6);
    	}else{
    		return ((EnumFacing) state.getValue(FACING)).getIndex();
    	}
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, POWERED});
    }
    
    public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityRetinalScanner();
	}

	public String getHelpInfo() {
		return "The retinal scanner emits a 15-block redstone signal when the owner of the block stands directly in front of it.";
	}

	public String[] getRecipe() {
		return new String[]{"The retinal scanner requires: 8 stone, 1 eye of ender", "XXX", "XYX", "XXX", "X = stone, Y = eye of ender"};
	}

}
