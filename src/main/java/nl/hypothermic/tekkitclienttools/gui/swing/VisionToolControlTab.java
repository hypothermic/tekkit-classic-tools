package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.tekkitclienttools.transformer.BlockBrightnessTransformer;
import nl.hypothermic.tekkitclienttools.transformer.BlockOpacityTransformer;
import nl.hypothermic.tekkitclienttools.transformer.BlockRenderPassTransformer;
import nl.hypothermic.tekkitclienttools.transformer.MixedBlockBrightnessTransformer;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class VisionToolControlTab extends BaseToolControlTab {

	private final ToolControlFrame parentFrame;

	public VisionToolControlTab(ToolControlFrame parentFrame) {
		this.parentFrame = parentFrame;

		GroupLayout groupLayout = getBaseLayout();

		CheckedSliderComponent opacityComponent = new CheckedSliderComponent.Builder()
				.name("Block Opacity")
				.checkboxInitialValue(BlockRenderPassTransformer.transparencyEnabled)
				.onCheckboxUpdate(result -> BlockRenderPassTransformer.transparencyEnabled = result)
				.sliderMinValue(BlockOpacityTransformer.MIN_OPACITY)
				.sliderMaxValue(BlockOpacityTransformer.MAX_OPACITY)
				.sliderInitialValue(BlockOpacityTransformer.opacityValue)
				.onSliderUpdate(result -> BlockOpacityTransformer.opacityValue = result)
				.textFieldLabel("Excluded Blocks")
				.textFieldValue("14,15,16,31,56,76")
				.onTextUpdate(text -> {
					BlockRenderPassTransformer.EXCLUDED_BLOCKS.clear();
					BlockRenderPassTransformer.EXCLUDED_BLOCKS.addAll(
							Arrays.stream(text.split(",", 1000))
									.map(Integer::parseInt)
									.filter(CheckedSliderComponent::isValidItemId)
									.collect(Collectors.toSet())
					);
				})
				.build();

		CheckedSliderComponent brightnessComponent = new CheckedSliderComponent.Builder()
				.name("Flat Block Brightness")
				.checkboxInitialValue(BlockBrightnessTransformer.brightnessEnabled)
				.onCheckboxUpdate(result -> BlockBrightnessTransformer.brightnessEnabled = result)
				.sliderMinValue((int) BlockBrightnessTransformer.MIN_BRIGHTNESS)
				.sliderMaxValue((int) BlockBrightnessTransformer.MAX_BRIGHTNESS)
				.sliderInitialValue((int) BlockBrightnessTransformer.brightnessValue)
				.onSliderUpdate(result -> BlockBrightnessTransformer.brightnessValue = result)
				.textFieldLabel("Excluded Blocks")
				.textFieldValue("")
				.onTextUpdate(text -> {})
				.build();

		CheckedSliderComponent mixedBrightnessComponent = new CheckedSliderComponent.Builder()
				.name("Mixed Block Brightness")
				.checkboxInitialValue(MixedBlockBrightnessTransformer.brightnessEnabled)
				.onCheckboxUpdate(result -> MixedBlockBrightnessTransformer.brightnessEnabled = result)
				.sliderMinValue(MixedBlockBrightnessTransformer.MIN_BRIGHTNESS)
				.sliderMaxValue(MixedBlockBrightnessTransformer.MAX_BRIGHTNESS)
				.sliderInitialValue(MixedBlockBrightnessTransformer.brightnessValue)
				.onSliderUpdate(result -> MixedBlockBrightnessTransformer.brightnessValue = result)
				.textFieldLabel("Excluded Blocks")
				.textFieldValue("")
				.onTextUpdate(text -> {})
				.build();



		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(opacityComponent.createHorizontalGroups(groupLayout))
						.addGroup(brightnessComponent.createHorizontalGroups(groupLayout))
						.addGroup(mixedBrightnessComponent.createHorizontalGroups(groupLayout))
		);

		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
						.addGroup(opacityComponent.createVerticalGroups(groupLayout))
						.addGroup(brightnessComponent.createVerticalGroups(groupLayout))
						.addGroup(mixedBrightnessComponent.createVerticalGroups(groupLayout))
		);
	}

	@Override
	public String getTitle() {
		return "Vision";
	}
}
