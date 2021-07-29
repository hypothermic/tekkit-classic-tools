package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.tekkitclienttools.gui.GuiTools;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

public class CheckedSliderComponent {

	public static boolean isValidItemId(int possibleItemId) {
		/*
		 * TODO check in minecraft item registry if the item is registered
		 */
		return possibleItemId > 0 && possibleItemId < 33000;
	}

	public static class Builder {

		private String name;
		private boolean startIsChecked;
		private int minValue, maxValue, startValue;
		private Consumer<Boolean> onCheckboxUpdate;
		private Consumer<Integer> onSliderUpdate;
		private String textFieldLabel, startTextValue;
		private Consumer<String> onTextUpdate;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder checkboxInitialValue(boolean isChecked) {
			this.startIsChecked = isChecked;
			return this;
		}

		public Builder sliderMinValue(int minValue) {
			this.minValue = minValue;
			return this;
		}

		public Builder sliderMaxValue(int maxValue) {
			this.maxValue = maxValue;
			return this;
		}

		public Builder sliderInitialValue(int initialValue) {
			this.startValue = initialValue;
			return this;
		}

		public Builder onCheckboxUpdate(Consumer<Boolean> onCheckboxUpdate) {
			this.onCheckboxUpdate = onCheckboxUpdate;
			return this;
		}

		public Builder onSliderUpdate(Consumer<Integer> onSliderUpdate) {
			this.onSliderUpdate = onSliderUpdate;
			return this;
		}

		public Builder textFieldLabel(String textFieldLabel) {
			this.textFieldLabel = textFieldLabel;
			return this;
		}

		public Builder textFieldValue(String startTextValue) {
			this.startTextValue = startTextValue;
			return this;
		}

		public Builder onTextUpdate(Consumer<String> onTextUpdate) {
			this.onTextUpdate = onTextUpdate;
			return this;
		}

		public CheckedSliderComponent build() {
			return new CheckedSliderComponent(
					name, startIsChecked, onCheckboxUpdate,
					minValue, maxValue, startValue, onSliderUpdate,
					textFieldLabel, startTextValue, onTextUpdate
			);
		}
	}

	private boolean isChecked;

	private final JCheckBox checkBox;
	private final JLabel prefix, suffix;
	private final JSlider slider;
	private final JLabel opacityExcludedLabel;
	private final JTextField opacityExcludedField;

	public CheckedSliderComponent(String name, boolean startIsChecked, Consumer<Boolean> onCheckboxUpdate,
								  int minValue, int maxValue, int startValue, Consumer<Integer> onSliderUpdate,
								  String textFieldLabel, String startTextValue, Consumer<String> onTextUpdate) {
		this.isChecked = startIsChecked;
		this.checkBox = new JCheckBox("Enable " + name + " Adjustments");
		this.checkBox.setSelected(startIsChecked);
		this.checkBox.addItemListener(e -> {
			isChecked = !isChecked;
			onCheckboxUpdate.accept(isChecked);
			GuiTools.reloadTextures();
		});

		this.prefix = new JLabel(name + ":");
		this.suffix = new JLabel("" + startValue);

		this.slider = new JSlider(minValue, maxValue, startValue);
		this.slider.addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();

			if (!slider.getValueIsAdjusting()) {
				onSliderUpdate.accept(slider.getValue());
				GuiTools.reloadTextures();
			}
			suffix.setText("" + slider.getValue());
		});

		this.opacityExcludedLabel = new JLabel(textFieldLabel);
		this.opacityExcludedField = new JTextField(startTextValue);
		this.opacityExcludedField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (e.getSource() == opacityExcludedField) {
					onTextUpdate.accept(((JTextField) e.getSource()).getText());
					GuiTools.reloadTextures();
				}
			}
		});
		onTextUpdate.accept(startTextValue);
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public JLabel getPrefix() {
		return prefix;
	}

	public JLabel getSuffix() {
		return suffix;
	}

	public JSlider getSlider() {
		return slider;
	}

	public JLabel getExcludedLabel() {
		return opacityExcludedLabel;
	}

	public JTextField getExcludedField() {
		return opacityExcludedField;
	}

	public GroupLayout.Group createHorizontalGroups(GroupLayout groupLayout) {
		return groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(getCheckBox())
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(getPrefix())
						.addComponent(getSlider())
						.addComponent(getSuffix())
				)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(getExcludedLabel())
						.addComponent(getExcludedField())
				);
	}

	public GroupLayout.Group createVerticalGroups(GroupLayout groupLayout) {
		return groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(getCheckBox())
				)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(getPrefix())
						.addComponent(getSlider())
						.addComponent(getSuffix())
				)
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(getExcludedLabel())
						.addComponent(getExcludedField())
				);
	}
}
