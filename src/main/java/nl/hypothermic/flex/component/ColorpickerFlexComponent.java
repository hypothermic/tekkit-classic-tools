package nl.hypothermic.flex.component;

import nl.hypothermic.flex.FlexComponentBuilder;
import nl.hypothermic.flex.state.GenericObservable;
import nl.hypothermic.tekkitclienttools.gui.swing.JColorPickerButton;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ColorpickerFlexComponent extends BaseFlexComponent {

	private final JColorPickerButton colorPickerButton;

	private ColorpickerFlexComponent(JColorPickerButton colorPickerButton) {
		this.colorPickerButton = colorPickerButton;
	}

	@Override
	protected boolean isSatisfied() {
		return true;
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.colorPickerButton);
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.colorPickerButton);
	}

	public static class Builder extends FlexComponentBuilder<ColorpickerFlexComponent> {

		private final JColorPickerButton colorPickerButton;

		public Builder() {
			this.colorPickerButton = new JColorPickerButton();
		}

		public Builder setEnabled(boolean enabled) {
			this.colorPickerButton.setEnabled(enabled);

			return this;
		}

		public Builder setEnabled(GenericObservable<Boolean> enabled) {
			enabled.addObserver(this.colorPickerButton::setEnabled);

			return this;
		}

		public Builder setInitialValue(Color value) {
			this.colorPickerButton.setSelectedColor(value);

			return this;
		}

		public Builder onChanged(Consumer<Color> consumer) {
			this.colorPickerButton.getSelectedColor().addObserver(consumer::accept);

			return this;
		}

		@Override
		public ColorpickerFlexComponent build() {
			return new ColorpickerFlexComponent(this.colorPickerButton);
		}
	}
}
