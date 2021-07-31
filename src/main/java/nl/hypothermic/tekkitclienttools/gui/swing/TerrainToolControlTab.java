package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.flex.*;
import nl.hypothermic.flex.component.BaseFlexComponent;
import nl.hypothermic.tekkitclienttools.gui.GuiTools;
import nl.hypothermic.tekkitclienttools.transformer.BlockBrightnessTransformer;
import nl.hypothermic.tekkitclienttools.transformer.BlockOpacityTransformer;
import nl.hypothermic.tekkitclienttools.transformer.BlockRenderPassTransformer;
import nl.hypothermic.tekkitclienttools.transformer.MixedBlockBrightnessTransformer;

import javax.swing.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static nl.hypothermic.flex.component.ListContainerFlexComponent.Direction.*;

public class TerrainToolControlTab extends BaseToolControlTab {

	private static final String TAB_TITLE = "Terrain";

	private final ToolControlFrame parentFrame;

	public TerrainToolControlTab(ToolControlFrame parentFrame) {
		this.parentFrame = parentFrame;

		FlexComponent<Void> opacityComponent = FlexComponent
				.create(root -> root
						.list(Boolean.class, BlockRenderPassTransformer.transparencyEnabled, VERTICAL, vertical -> vertical
								.checkbox(checkbox -> checkbox
										.setLabel("Enable Block Opacity Adjustments")
										.setInitialValue(BlockRenderPassTransformer.transparencyEnabled)
										.onChanged(vertical::setState)
										.onChanged(newValue -> BlockRenderPassTransformer.transparencyEnabled = newValue)
										.onChanged(ignored -> GuiTools.reloadTextures())
								)
								.list(String.class, HORIZONTAL, horizontal -> horizontal
										.label(label -> label
												.setText("Block Opacity:")
										)
										.slider(slider -> slider
												.setMinimumValue(BlockOpacityTransformer.MIN_OPACITY)
												.setMaximumValue(BlockOpacityTransformer.MAX_OPACITY)
												.setInitialValue(BlockOpacityTransformer.opacityValue)
												.setEnabled(vertical.getState())
												.onChanging(newValue -> horizontal.setState(newValue + ""))
												.onChanged(newValue -> BlockOpacityTransformer.opacityValue = newValue)
												.onChanged(ignored -> GuiTools.reloadTextures())
										)
										.label(label -> label
												.setText(String.valueOf(BlockOpacityTransformer.opacityValue))
												.setText(horizontal.getState())
										)
								)
								.textField(textField -> textField
										.setInitialValue("14,15,16,31,56,76")
										.setEnabled(vertical.getState())
										.onChanged(newValue -> {
											BlockRenderPassTransformer.EXCLUDED_BLOCKS.clear();
											BlockRenderPassTransformer.EXCLUDED_BLOCKS.addAll(
													Arrays.stream(newValue.split(",", 1000))
															.map(Integer::parseInt)
															.filter(CheckedSliderComponent::isValidItemId)
															.collect(Collectors.toSet())
											);
										})
								)
					)
				);

		FlexComponent<Void> brightnessComponent = FlexComponent
				.create(root -> root
						.list(Boolean.class, BlockBrightnessTransformer.brightnessEnabled, VERTICAL, vertical -> vertical
								.checkbox(checkbox -> checkbox
										.setLabel("Enable Flat Block Brightness Adjustments")
										.setInitialValue(BlockBrightnessTransformer.brightnessEnabled)
										.onChanged(vertical::setState)
										.onChanged(newValue -> BlockBrightnessTransformer.brightnessEnabled = newValue)
										.onChanged(ignored -> GuiTools.reloadTextures())
								)
								.list(String.class, HORIZONTAL, horizontal -> horizontal
										.label(label -> label
												.setText("Flat Block Brightness:")
										)
										.slider(slider -> slider
												.setMinimumValue((int) BlockBrightnessTransformer.MIN_BRIGHTNESS)
												.setMaximumValue((int) BlockBrightnessTransformer.MAX_BRIGHTNESS)
												.setInitialValue((int) BlockBrightnessTransformer.brightnessValue)
												.setEnabled(vertical.getState())
												.onChanging(newValue -> horizontal.setState(newValue + ""))
												.onChanged(newValue -> BlockBrightnessTransformer.brightnessValue = newValue)
												.onChanged(ignored -> GuiTools.reloadTextures())
										)
										.label(label -> label
												.setText(String.valueOf((int) BlockBrightnessTransformer.brightnessValue))
												.setText(horizontal.getState())
										)
								)
						)
				);

		FlexComponent<Void> mixedBrightnessComponent = FlexComponent
				.create(root -> root
						.list(Boolean.class, MixedBlockBrightnessTransformer.brightnessEnabled, VERTICAL, vertical -> vertical
								.checkbox(checkbox -> checkbox
										.setLabel("Enable Flat Block Brightness Adjustments")
										.setInitialValue(MixedBlockBrightnessTransformer.brightnessEnabled)
										.onChanged(vertical::setState)
										.onChanged(newValue -> MixedBlockBrightnessTransformer.brightnessEnabled = newValue)
										.onChanged(ignored -> GuiTools.reloadTextures())
								)
								.list(String.class, HORIZONTAL, horizontal -> horizontal
										.label(label -> label
												.setText("Flat Block Brightness:")
										)
										.slider(slider -> slider
												.setMinimumValue(MixedBlockBrightnessTransformer.MIN_BRIGHTNESS)
												.setMaximumValue(MixedBlockBrightnessTransformer.MAX_BRIGHTNESS)
												.setInitialValue(MixedBlockBrightnessTransformer.brightnessValue)
												.setEnabled(vertical.getState())
												.onChanging(newValue -> horizontal.setState(newValue + ""))
												.onChanged(newValue -> MixedBlockBrightnessTransformer.brightnessValue = newValue)
												.onChanged(ignoredt -> GuiTools.reloadTextures())
										)
										.label(label -> label
												.setText(String.valueOf(MixedBlockBrightnessTransformer.brightnessValue))
												.setText(horizontal.getState())
										)
								)
						)
				);

		BaseFlexComponent listComponent = FlexComponent
				.create(root -> root
						.list(Void.class, VERTICAL, vertical -> {
							vertical.flex(opacityComponent);
							vertical.flex(brightnessComponent);
							vertical.flex(mixedBrightnessComponent);
						})
				).build();

		GroupLayout groupLayout = getBaseLayout();

		groupLayout.setHorizontalGroup(listComponent.createHorizontalGroup(groupLayout));
		groupLayout.setVerticalGroup(listComponent.createVerticalGroup(groupLayout));
	}

	@Override
	public String getTitle() {
		return TAB_TITLE;
	}
}
