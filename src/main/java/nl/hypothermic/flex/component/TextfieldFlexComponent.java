package nl.hypothermic.flex.component;

import nl.hypothermic.flex.FlexComponentBuilder;
import nl.hypothermic.flex.state.GenericObservable;
import nl.hypothermic.tekkitclienttools.gui.GuiTools;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public class TextfieldFlexComponent extends BaseFlexComponent {

	private final JTextField textField;

	private TextfieldFlexComponent(JTextField textField) {
		this.textField = textField;
	}

	@Override
	protected boolean isSatisfied() {
		return true;
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(textField);
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup().addComponent(textField);
	}

	public static class Builder extends FlexComponentBuilder<TextfieldFlexComponent> {

		private final JTextField textField;

		public Builder() {
			textField = new JTextField();
		}

		public Builder setInitialValue(String initialValue) {
			textField.setText(initialValue);

			return this;
		}

		public Builder setEnabled(boolean enabled) {
			textField.setEnabled(enabled);

			return this;
		}

		public Builder setEnabled(GenericObservable<Boolean> enabled) {
			enabled.addObserver(textField::setEnabled);

			return this;
		}

		public Builder onChanged(Consumer<String> consumer) {
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					if (e.getSource() == textField) {
						consumer.accept(((JTextField) e.getSource()).getText());
						GuiTools.reloadTextures();
					}
				}
			});

			consumer.accept(textField.getText());

			return this;
		}

		@Override
		public TextfieldFlexComponent build() {
			return new TextfieldFlexComponent(textField);
		}
	}
}
