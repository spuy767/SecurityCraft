package net.geforcemods.securitycraft.api;

import net.geforcemods.securitycraft.SecurityCraft;
import net.geforcemods.securitycraft.network.server.UpdateSliderValue;
import net.geforcemods.securitycraft.screen.CustomizeBlockScreen;
import net.geforcemods.securitycraft.screen.components.NamedSlider;
import net.geforcemods.securitycraft.util.ClientUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.client.gui.widget.Slider;
import net.minecraftforge.fml.client.gui.widget.Slider.ISlider;

/**
 * A class that allows blocks that have
 * {@link CustomizableTileEntity}s to have custom, "per-block"
 * options that are separate from the main SecurityCraft
 * configuration options.
 *
 * @author Geforce
 *
 * @param <T> The Class of the type of value this option should use
 */
public abstract class Option<T> {

	private String name;
	protected T value;
	private T defaultValue;
	private T increment;
	private T minimum;
	private T maximum;

	public Option(String optionName, T value) {
		this.name = optionName;
		this.value = value;
		this.defaultValue = value;
	}

	public Option(String optionName, T value, T min, T max, T increment) {
		this.name = optionName;
		this.value = value;
		this.defaultValue = value;
		this.increment = increment;
		this.minimum = min;
		this.maximum = max;
	}

	/**
	 * Called when this option's button in {@link CustomizeBlockScreen} is pressed.
	 * Update the option's value here. <p>
	 *
	 * NOTE: This gets called on the server side, not on the client!
	 * Use TileEntitySCTE.sync() to update values on the client-side.
	 */
	public abstract void toggle();

	public abstract void readFromNBT(CompoundNBT tag);

	public abstract void writeToNBT(CompoundNBT tag);

	public void copy(Option<?> option) {
		value = (T) option.get();
	}

	/**
	 * @return This option's name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return This option's value.
	 */
	public T get() {
		return value;
	}

	/**
	 * Set this option's new value here.
	 *
	 * @param value The new value
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return This option's default value.
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return If this option is some kind of number (integer, float, etc.),
	 *         return the amount the number should increase/decrease every time
	 *         the option is toggled in {@link CustomizeBlockScreen}.
	 */
	public T getIncrement() {
		return increment;
	}

	/**
	 * @return The lowest value this option can be set to.
	 */
	public T getMin() {
		return minimum;
	}

	/**
	 * @return The highest value this option can be set to.
	 */
	public T getMax() {
		return maximum;
	}

	/**
	 * @return Whether this Option should be displayed as a slider
	 */
	public boolean isSlider() {
		return false;
	}

	/**
	 * A subclass of {@link Option}, already setup to handle booleans.
	 */
	public static class BooleanOption extends Option<Boolean>{

		public BooleanOption(String optionName, Boolean value) {
			super(optionName, value);
		}

		@Override
		public void toggle() {
			setValue(!get());
		}

		@Override
		public void readFromNBT(CompoundNBT tag)
		{
			if(tag.contains(getName()))
				value = tag.getBoolean(getName());
			else
				value = getDefaultValue();
		}

		@Override
		public void writeToNBT(CompoundNBT tag)
		{
			tag.putBoolean(getName(), value);
		}

		@Override
		public String toString() {
			return (value) + "";
		}
	}

	/**
	 * A subclass of {@link Option}, already setup to handle integers.
	 */
	public static class IntOption extends Option<Integer> implements ISlider{
		private boolean slider;
		private CustomizableTileEntity tileEntity;

		public IntOption(String optionName, Integer value) {
			super(optionName, value);
		}

		public IntOption(String optionName, Integer value, Integer min, Integer max, Integer increment) {
			super(optionName, value, min, max, increment);
		}

		public IntOption(CustomizableTileEntity te, String optionName, Integer value, Integer min, Integer max, Integer increment, boolean s) {
			super(optionName, value, min, max, increment);
			slider = s;
			tileEntity = te;
		}

		@Override
		public void toggle() {
			if(isSlider())
				return;

			if(get() >= getMax()) {
				setValue(getMin());
				return;
			}

			if((get() + getIncrement()) >= getMax()) {
				setValue(getMax());
				return;
			}

			setValue(get() + getIncrement());
		}

		@Override
		public void readFromNBT(CompoundNBT tag)
		{
			if(tag.contains(getName()))
				value = tag.getInt(getName());
			else
				value = getDefaultValue();
		}

		@Override
		public void writeToNBT(CompoundNBT tag)
		{
			tag.putInt(getName(), value);
		}

		@Override
		public String toString() {
			return (value) + "";
		}

		@Override
		public boolean isSlider()
		{
			return slider;
		}

		@Override
		public void onChangeSliderValue(Slider slider)
		{
			if(!isSlider() || !(slider instanceof NamedSlider))
				return;

			setValue((int)slider.getValue());
			slider.setMessage((ClientUtils.localize("option" + ((NamedSlider)slider).getBlockName() + "." + getName()) + " ").replace("#", toString()));
			SecurityCraft.channel.sendToServer(new UpdateSliderValue(tileEntity.getPos(), ((NamedSlider)slider).id, get()));
		}
	}

	/**
	 * A subclass of {@link Option}, already setup to handle doubles.
	 */
	public static class DoubleOption extends Option<Double> implements ISlider{
		private boolean slider;
		private CustomizableTileEntity tileEntity;

		public DoubleOption(String optionName, Double value) {
			super(optionName, value);
			slider = false;
		}

		public DoubleOption(String optionName, Double value, Double min, Double max, Double increment) {
			super(optionName, value, min, max, increment);
			slider = false;
		}

		public DoubleOption(CustomizableTileEntity te, String optionName, Double value, Double min, Double max, Double increment, boolean s) {
			super(optionName, value, min, max, increment);
			slider = s;
			tileEntity = te;
		}

		@Override
		public void toggle() {
			if(isSlider())
				return;

			if(get() >= getMax()) {
				setValue(getMin());
				return;
			}

			if((get() + getIncrement()) >= getMax()) {
				setValue(getMax());
				return;
			}

			setValue(get() + getIncrement());
		}

		@Override
		public void readFromNBT(CompoundNBT tag)
		{
			if(tag.contains(getName()))
				value = tag.getDouble(getName());
			else
				value = getDefaultValue();
		}

		@Override
		public void writeToNBT(CompoundNBT tag)
		{
			tag.putDouble(getName(), value);
		}

		@Override
		public String toString() {
			return Double.toString(value).length() > 5 ? Double.toString(value).substring(0, 5) : Double.toString(value);
		}

		@Override
		public boolean isSlider()
		{
			return slider;
		}

		@Override
		public void onChangeSliderValue(Slider slider)
		{
			if(!isSlider() || !(slider instanceof NamedSlider))
				return;

			setValue(slider.getValue());
			slider.setMessage((ClientUtils.localize("option" + ((NamedSlider)slider).getBlockName() + "." + getName()) + " ").replace("#", toString()));
			SecurityCraft.channel.sendToServer(new UpdateSliderValue(tileEntity.getPos(), ((NamedSlider)slider).id, get()));
		}
	}

	/**
	 * A subclass of {@link Option}, already setup to handle floats.
	 */
	public static class FloatOption extends Option<Float>{

		public FloatOption(String optionName, Float value) {
			super(optionName, value);
		}

		public FloatOption(String optionName, Float value, Float min, Float max, Float increment) {
			super(optionName, value, min, max, increment);
		}

		@Override
		public void toggle() {
			if(get() >= getMax()) {
				setValue(getMin());
				return;
			}

			if((get() + getIncrement()) >= getMax()) {
				setValue(getMax());
				return;
			}

			setValue(get() + getIncrement());
		}

		@Override
		public void readFromNBT(CompoundNBT tag)
		{
			if(tag.contains(getName()))
				value = tag.getFloat(getName());
			else
				value = getDefaultValue();
		}

		@Override
		public void writeToNBT(CompoundNBT tag)
		{
			tag.putFloat(getName(), value);
		}

		@Override
		public String toString() {
			return Float.toString(value).length() > 5 ? Float.toString(value).substring(0, 5) : Float.toString(value);
		}
	}
}
