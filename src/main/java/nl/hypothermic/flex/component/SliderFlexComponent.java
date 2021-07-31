package nl.hypothermic.flex.component;

import nl.hypothermic.flex.FlexComponentBuilder;
import nl.hypothermic.flex.state.GenericObservable;

import javax.swing.*;
import java.util.function.Consumer;

public class SliderFlexComponent extends BaseFlexComponent {

	private final JSlider slider;

	private SliderFlexComponent(JSlider slider) {
		this.slider = slider;
	}

	@Override
	protected boolean isSatisfied() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(slider);
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(slider);
	}

	public static class Builder extends FlexComponentBuilder<SliderFlexComponent> {

		private final JSlider slider;

		public Builder() {
			slider = new JSlider();
		}

		public Builder setInitialValue(int initialValue) {
			slider.setValue(initialValue);

			return this;
		}

		public Builder setMinimumValue(int minimumValue) {
			slider.setMinimum(minimumValue);

			return this;
		}

		public Builder setMaximumValue(int maximumValue) {
			slider.setMaximum(maximumValue);

			return this;
		}

		public Builder setEnabled(boolean enabled) {
			slider.setEnabled(enabled);

			return this;
		}

		public Builder setEnabled(GenericObservable<Boolean> enabled) {
			enabled.addObserver(slider::setEnabled);

			return this;
		}

		public Builder onChanged(Consumer<Integer> consumer) {
			slider.addChangeListener(e -> {
				JSlider slider = (JSlider) e.getSource();

				if (!slider.getValueIsAdjusting()) {
					consumer.accept(slider.getValue());
				}
			});

			return this;
		}

		public Builder onChanging(Consumer<Integer> consumer) {
			slider.addChangeListener(e -> consumer.accept(((JSlider) e.getSource()).getValue()));

			return this;
		}

		@Override
		public SliderFlexComponent build() {
			return new SliderFlexComponent(slider);
		}
	}
}
