package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.flex.state.GenericObservable;
import nl.hypothermic.flex.state.StandaloneCachedGenericObservable;

import javax.swing.*;
import java.awt.*;

public class JColorPickerButton extends JButton {

	private final StandaloneCachedGenericObservable<Color> selectedColor;

	public JColorPickerButton() {
		this.selectedColor = new StandaloneCachedGenericObservable<>();
		this.selectedColor.addObserver(this::setBackground);

		this.addActionListener(e -> {
			Color newValue = JColorChooser.showDialog(
					JColorPickerButton.this,
					"Select a color",
					selectedColor.getLastValue()
			);

			// the user can actually set "no color" which results in null...
			if (newValue != null) {
				selectedColor.setValue(newValue);
			}
		});
	}

	public void setSelectedColor(Color color) {
		this.selectedColor.setValue(color);
	}

	public GenericObservable<Color> getSelectedColor() {
		return this.selectedColor;
	}
}
