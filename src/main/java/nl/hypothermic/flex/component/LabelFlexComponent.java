package nl.hypothermic.flex.component;

import nl.hypothermic.flex.FlexComponentBuilder;
import nl.hypothermic.flex.state.GenericObservable;

import javax.swing.*;

public class LabelFlexComponent extends BaseFlexComponent {

	private final JLabel label;

	private LabelFlexComponent(JLabel label) {
		this.label = label;
	}

	@Override
	protected boolean isSatisfied() {
		return true;
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.label);
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.label);
	}

	public static class Builder extends FlexComponentBuilder<LabelFlexComponent> {

		private final JLabel label;

		public Builder() {
			this.label = new JLabel();
		}

		public Builder setText(String text) {
			this.label.setText(text);

			return this;
		}

		public Builder setText(GenericObservable<String> textObservable) {
			textObservable.addObserver(this.label::setText);

			return this;
		}

		@Override
		public LabelFlexComponent build() {
			return new LabelFlexComponent(this.label);
		}
	}
}
