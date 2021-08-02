package nl.hypothermic.flex.component;

import nl.hypothermic.flex.FlexComponentBuilder;
import nl.hypothermic.flex.state.GenericObservable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;

public class CheckboxFlexComponent extends BaseFlexComponent {

	private final JCheckBox checkBox;

	private CheckboxFlexComponent(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

	@Override
	protected boolean isSatisfied() {
		return this.checkBox.isSelected();
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.checkBox);
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.checkBox);
	}

	public static class Builder extends FlexComponentBuilder<CheckboxFlexComponent> {

		private final JCheckBox checkBox;

		public Builder() {
			this.checkBox = new JCheckBox();
		}

		public Builder setLabel(String label) {
			this.checkBox.setText(label);

			return this;
		}

		public Builder setEnabled(boolean enabled) {
			this.checkBox.setEnabled(enabled);

			return this;
		}

		public Builder setEnabled(GenericObservable<Boolean> enabled) {
			enabled.addObserver(this.checkBox::setEnabled);

			return this;
		}

		public Builder setInitialValue(boolean initialValue) {
			this.checkBox.setSelected(initialValue);

			return this;
		}

		public Builder onChanged(Consumer<Boolean> consumer) {
//			this.checkBox.addItemListener(e -> consumer.accept(e.getStateChange() == ItemEvent.SELECTED));
			this.checkBox.addItemListener(e -> consumer.accept(((JCheckBox) e.getSource()).isSelected()));

			return this;
		}

		@Override
		public CheckboxFlexComponent build() {
			return new CheckboxFlexComponent(this.checkBox);
		}
	}
}
