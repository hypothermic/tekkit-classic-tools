package nl.hypothermic.flex.component;

import javax.swing.*;

public abstract class BaseFlexComponent {

	protected abstract boolean isSatisfied();

	public abstract GroupLayout.Group createHorizontalGroup(GroupLayout groupLayout);

	public abstract GroupLayout.Group createVerticalGroup(GroupLayout groupLayout);

}
