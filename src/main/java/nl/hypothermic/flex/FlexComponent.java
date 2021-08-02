package nl.hypothermic.flex;

import nl.hypothermic.flex.component.*;
import nl.hypothermic.flex.state.GenericObservable;
import nl.hypothermic.flex.state.StandaloneCachedGenericObservable;

import java.util.function.Consumer;

/**
 * A convenience structure for recursively creating FlexComponents through FlexComponentBuilders,
 * which is also able to store a local state that may be accessed by child components.<br /><br />
 *
 * The type, T, is the type of the local stored state.
 */
public final class FlexComponent<T> {

	private final ContainerFlexComponentBuilder<?> builder;
	private final StandaloneCachedGenericObservable<T> state;

	protected FlexComponent(ContainerFlexComponentBuilder<?> builder) {
		this.builder = builder;
		this.state = new StandaloneCachedGenericObservable<>();
	}

	public static FlexComponent<Void> create(Consumer<FlexComponent<Void>> consumer) {
		FlexComponent<Void> flexComponent = new FlexComponent<>(new OverlapContainerFlexComponent.Builder());

		consumer.accept(flexComponent);

		return flexComponent;
	}

	public static <T> FlexComponent<T> create(Class<T> stateType, Consumer<FlexComponent<T>> consumer) {
		FlexComponent<T> flexComponent = new FlexComponent<>(new OverlapContainerFlexComponent.Builder());

		consumer.accept(flexComponent);

		return flexComponent;
	}

	public FlexComponent<T> overlap(Consumer<FlexComponent<Void>> consumer) {
		return overlap(Void.class, consumer);
	}

	public <A> FlexComponent<T> overlap(Class<A> stateType, Consumer<FlexComponent<A>> consumer) {
		return overlap(stateType, null, consumer);
	}

	public <A> FlexComponent<T> overlap(Class<A> stateType, A initialState, Consumer<FlexComponent<A>> consumer) {
		FlexComponent<A> flexComponent = new FlexComponent<>(
				new OverlapContainerFlexComponent.Builder()
		);

		if (null != initialState) {
			flexComponent.state.setValue(initialState);
		}

		consumer.accept(flexComponent);
		this.builder.addChild(flexComponent.builder);

		return this;
	}

	public FlexComponent<T> list(ListContainerFlexComponent.Direction direction, Consumer<FlexComponent<Void>> consumer) {
		return list(Void.class, direction, consumer);
	}

	public <A> FlexComponent<T> list(Class<A> stateType, ListContainerFlexComponent.Direction direction, Consumer<FlexComponent<A>> consumer) {
		return list(stateType, null, direction, consumer);
	}

	public <A> FlexComponent<T> list(Class<A> stateType, A initialState, ListContainerFlexComponent.Direction direction, Consumer<FlexComponent<A>> consumer) {
		FlexComponent<A> flexComponent = new FlexComponent<>(
				new ListContainerFlexComponent.Builder().setDirection(direction)
		);

		if (null != initialState) {
			flexComponent.state.setValue(initialState);
		}

		consumer.accept(flexComponent);
		this.builder.addChild(flexComponent.builder);

		return this;
	}

	public <A> FlexComponent<T> flex(FlexComponent<A> flexComponent) {
		this.builder.addChild(flexComponent.builder);
		return this;
	}

	public FlexComponent<T> checkbox(Consumer<CheckboxFlexComponent.Builder> consumer) {
		CheckboxFlexComponent.Builder builder = new CheckboxFlexComponent.Builder();

		consumer.accept(builder);
		this.builder.addChild(builder);
		return this;
	}

	public FlexComponent<T> slider(Consumer<SliderFlexComponent.Builder> consumer) {
		SliderFlexComponent.Builder builder = new SliderFlexComponent.Builder();

		consumer.accept(builder);
		this.builder.addChild(builder);

		return this;
	}

	public FlexComponent<T> label(Consumer<LabelFlexComponent.Builder> consumer) {
		LabelFlexComponent.Builder builder = new LabelFlexComponent.Builder();

		consumer.accept(builder);
		this.builder.addChild(builder);

		return this;
	}

	public FlexComponent<T> textField(Consumer<TextfieldFlexComponent.Builder> consumer) {
		TextfieldFlexComponent.Builder builder = new TextfieldFlexComponent.Builder();

		consumer.accept(builder);
		this.builder.addChild(builder);

		return this;
	}

	public FlexComponent<T> colorPicker(Consumer<ColorpickerFlexComponent.Builder> consumer) {
		ColorpickerFlexComponent.Builder builder = new ColorpickerFlexComponent.Builder();

		consumer.accept(builder);
		this.builder.addChild(builder);

		return this;
	}

	public GenericObservable<T> getState() {
		return this.state;
	}

	public void setState(T newState) {
		this.state.setValue(newState);
	}

	public BaseFlexComponent build() {
		return this.builder.build();
	}
}
