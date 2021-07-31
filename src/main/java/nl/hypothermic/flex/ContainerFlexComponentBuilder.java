package nl.hypothermic.flex;

import nl.hypothermic.flex.component.BaseFlexComponent;
import nl.hypothermic.flex.component.ContainerFlexComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ContainerFlexComponentBuilder<T extends ContainerFlexComponent> extends FlexComponentBuilder<T> {

	protected final List<FlexComponentBuilder<?>> children = new ArrayList<>();

	public void addChild(FlexComponentBuilder<?> child) {
		this.children.add(child);
	}

	protected List<BaseFlexComponent> buildChildren() {
		return this.children.stream()
				.map(FlexComponentBuilder::build)
				.collect(Collectors.toList());
	}
}
