package nl.hypothermic.flex.component;

import java.util.List;

public abstract class ContainerFlexComponent extends BaseFlexComponent {

	private final List<BaseFlexComponent> children;

	public ContainerFlexComponent(List<BaseFlexComponent> children) {
		this.children = children;
	}

	@Override
	protected boolean isSatisfied() {
		return this.children.stream().allMatch(BaseFlexComponent::isSatisfied);
	}

	public List<BaseFlexComponent> getChildren() {
		return this.children;
	}
}
