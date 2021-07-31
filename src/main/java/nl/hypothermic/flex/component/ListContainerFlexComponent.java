package nl.hypothermic.flex.component;

import nl.hypothermic.flex.ContainerFlexComponentBuilder;

import javax.swing.*;
import java.util.List;

public class ListContainerFlexComponent extends ContainerFlexComponent {

	private final Direction direction;

	public ListContainerFlexComponent(Direction direction, List<BaseFlexComponent> children) {
		super(children);
		this.direction = direction;
	}

	@Override
	public GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout) {
		GroupLayout.Group group;

		if (this.direction == Direction.HORIZONTAL)
			group = groupLayout.createSequentialGroup();
		else
			group = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);

		GroupLayout.Group finalGroup = group;

		getChildren().stream()
				.map(child -> child.createHorizontalGroup(groupLayout))
				.forEach(finalGroup::addGroup);

		return group;
	}

	@Override
	public GroupLayout.Group createVerticalGroup(GroupLayout groupLayout) {
		GroupLayout.Group group;

		if (this.direction == Direction.VERTICAL)
			group = groupLayout.createSequentialGroup();
		else
			group = groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);

		GroupLayout.Group finalGroup = group;

		getChildren().stream()
				.map(child -> child.createVerticalGroup(groupLayout))
				.forEach(finalGroup::addGroup);

		return group;
	}

	public static enum Direction {
		HORIZONTAL,
		VERTICAL,
	}

	public static class Builder extends ContainerFlexComponentBuilder<ListContainerFlexComponent> {

		private Direction direction;

		public Builder setDirection(Direction direction) {
			this.direction = direction;

			return this;
		}

		@Override
		public ListContainerFlexComponent build() {
			return new ListContainerFlexComponent(this.direction, buildChildren());
		}
	}
}
