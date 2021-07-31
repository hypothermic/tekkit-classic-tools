package nl.hypothermic.flex;

import nl.hypothermic.flex.component.BaseFlexComponent;

public abstract class FlexComponentBuilder<T extends BaseFlexComponent> {

	public abstract T build();

}
