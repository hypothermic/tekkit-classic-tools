package nl.hypothermic.flex.component;

import nl.hypothermic.flex.FlexComponentBuilder;

import javax.swing.*;
import java.awt.*;

public class SeparatorFlexComponent extends BaseFlexComponent {

	private static final int INFINITY = Integer.MAX_VALUE;

	private final JSeparator separator;

	private SeparatorFlexComponent(JSeparator separator) {
		this.separator = separator;
	}

	@Override
	protected boolean isSatisfied() {
		return true;
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.separator);
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(this.separator);
	}

	public static class Builder extends FlexComponentBuilder<SeparatorFlexComponent> {

		private final JSeparator separator;

		public Builder() {
			this.separator = new JSeparator();
		}

		public Builder setOrientation(int orientation) {
			this.separator.setOrientation(orientation);

			// some hardcoded shit because grouplayout is dog poop
			switch (orientation) {
				case SwingConstants.HORIZONTAL:
					this.separator.setMaximumSize(new Dimension(INFINITY, 10));
					break;
				case SwingConstants.VERTICAL:
					this.separator.setMaximumSize(new Dimension(10, INFINITY));
			}

			return this;
		}

		@Override
		public SeparatorFlexComponent build() {
			return new SeparatorFlexComponent(this.separator);
		}
	}
}
