package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.flex.FlexComponent;
import nl.hypothermic.flex.component.BaseFlexComponent;
import nl.hypothermic.tekkitclienttools.transformer.*;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static nl.hypothermic.flex.component.ListContainerFlexComponent.Direction.*;

public class EspToolControlTab extends BaseToolControlTab {

	private static final String TAB_TITLE = "ESP";

	private final ToolControlFrame parentFrame;

	public EspToolControlTab(ToolControlFrame parentFrame) {
		this.parentFrame = parentFrame;

		FlexComponent<Void> tileEspComponent = FlexComponent
				.create(root -> root
						.list(Boolean.class, TileEntityRenderPassTransformer.isEnabled, VERTICAL, vertical -> vertical
								.checkbox(checkbox -> checkbox
										.setLabel("Enable TileEntity ESP")
										.setInitialValue(TileEntityRenderPassTransformer.isEnabled)
										.onChanged(newValue -> TileEntityRenderPassTransformer.isEnabled = newValue)
										.onChanged(vertical::setState)
								)
								.list(HORIZONTAL, horizontal -> horizontal
										.checkbox(checkbox -> checkbox
												.setLabel("Draw Block Outline")
												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
												.setEnabled(vertical.getState())
												.setInitialValue(TileEntityRenderPassTransformer.tileOutline.isEnabled())
												.onChanged(newValue -> TileEntityRenderPassTransformer.tileOutline.setEnabled(newValue))
										)
										.colorPicker(colorPicker -> colorPicker
												.setInitialValue(TileEntityRenderPassTransformer.tileOutline.getColor())
												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
												.setEnabled(vertical.getState())
												.onChanged(newValue -> TileEntityRenderPassTransformer.tileOutline.setColor(newValue))
										)

								)
								.list(HORIZONTAL, horizontal -> horizontal
										.checkbox(checkbox -> checkbox
												.setLabel("Draw Line to Cursor")
												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
												.setEnabled(vertical.getState())
												.setInitialValue(TileEntityRenderPassTransformer.tileCursorLine.isEnabled())
												.onChanged(newValue -> TileEntityRenderPassTransformer.tileCursorLine.setEnabled(newValue))
										)
										.colorPicker(colorPicker -> colorPicker
												.setInitialValue(TileEntityRenderPassTransformer.tileCursorLine.getColor())
												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
												.setEnabled(vertical.getState())
												.onChanged(newValue -> TileEntityRenderPassTransformer.tileCursorLine.setColor(newValue))
										)
								)
								.textField(textField -> textField
										.setInitialValue("23,54,95,128,178,181,225")
										.setEnabled(vertical.getState())
										.onChanged(newValue -> {
											TileEntityRenderPassTransformer.INCLUDED_BLOCKS.clear();
											TileEntityRenderPassTransformer.INCLUDED_BLOCKS.addAll(
													Arrays.stream(newValue.split(",", 1000))
															.map(Integer::parseInt)
															.filter(CheckedSliderComponent::isValidItemId)
															.collect(Collectors.toSet())
											);
										})
								)
					)
				);

//		FlexComponent<Void> entityEspComponent = FlexComponent
//				.create(root -> root
//						.list(Boolean.class, TileEntityRenderPassTransformer.isEnabled, VERTICAL, vertical -> vertical
//								.checkbox(checkbox -> checkbox
//										.setLabel("Enable Entity ESP")
//										.setInitialValue(TileEntityRenderPassTransformer.isEnabled)
//										.onChanged(newValue -> TileEntityRenderPassTransformer.isEnabled = newValue)
//										.onChanged(vertical::setState)
//								)
//								.list(HORIZONTAL, horizontal -> horizontal
//										.checkbox(checkbox -> checkbox
//												.setLabel("Draw Entity Outline")
//												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
//												.setEnabled(vertical.getState())
//												.setInitialValue(TileEntityRenderPassTransformer.tileOutline.isEnabled())
//												.onChanged(newValue -> TileEntityRenderPassTransformer.tileOutline.setEnabled(newValue))
//										)
//										.colorPicker(colorPicker -> colorPicker
//												.setInitialValue(TileEntityRenderPassTransformer.tileOutline.getColor())
//												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
//												.setEnabled(vertical.getState())
//												.onChanged(newValue -> TileEntityRenderPassTransformer.tileOutline.setColor(newValue))
//										)
//
//								)
//								.list(HORIZONTAL, horizontal -> horizontal
//										.checkbox(checkbox -> checkbox
//												.setLabel("Draw Line to Cursor")
//												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
//												.setEnabled(vertical.getState())
//												.setInitialValue(TileEntityRenderPassTransformer.tileCursorLine.isEnabled())
//												.onChanged(newValue -> TileEntityRenderPassTransformer.tileCursorLine.setEnabled(newValue))
//										)
//										.colorPicker(colorPicker -> colorPicker
//												.setInitialValue(TileEntityRenderPassTransformer.tileCursorLine.getColor())
//												.setEnabled(TileEntityRenderPassTransformer.isEnabled)
//												.setEnabled(vertical.getState())
//												.onChanged(newValue -> TileEntityRenderPassTransformer.tileCursorLine.setColor(newValue))
//										)
//								)
//								.textField(textField -> textField
//										.setInitialValue("")
//										.setEnabled(vertical.getState())
//										.onChanged(newValue -> {
//											TileEntityRenderPassTransformer.INCLUDED_BLOCKS.clear();
//											TileEntityRenderPassTransformer.INCLUDED_BLOCKS.addAll(
//													Arrays.stream(newValue.split(",", 1000))
//															.map(Integer::parseInt)
//															.filter(CheckedSliderComponent::isValidItemId)
//															.collect(Collectors.toSet())
//											);
//										})
//								)
//						)
//				);

		BaseFlexComponent listComponent = FlexComponent
				.create(root -> root
						.list(VERTICAL, vertical -> {
							vertical.flex(tileEspComponent);
//							vertical.flex(entityEspComponent);
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
