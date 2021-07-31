package nl.hypothermic.flex.component;

import nl.hypothermic.flex.ContainerFlexComponentBuilder;

import javax.swing.*;
import java.util.List;

public class OverlapContainerFlexComponent extends ContainerFlexComponent {

	public OverlapContainerFlexComponent(List<BaseFlexComponent> children) {
		super(children);
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		GroupLayout.Group group = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);

		// TODO do this with stream.reduce
		getChildren().forEach(child -> group.addGroup(child.createHorizontalGroup(groupLayout)));

		return group;
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		GroupLayout.Group group = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);

		// TODO do this with stream.reduce
		getChildren().forEach(child -> group.addGroup(child.createVerticalGroup(groupLayout)));

		return group;
	}

	public static class Builder extends ContainerFlexComponentBuilder<OverlapContainerFlexComponent> {

		@Override
		public OverlapContainerFlexComponent build() {
			return new OverlapContainerFlexComponent(buildChildren());
		}
	}
}
