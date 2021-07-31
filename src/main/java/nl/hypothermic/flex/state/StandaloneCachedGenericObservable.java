package nl.hypothermic.flex.state;

public class StandaloneCachedGenericObservable<T> extends CachedGenericObservable<T> {

	public void setValue(T value) {
		onUpdate(value);
	}

	public void castAndSetValue(Object value) {
		//noinspection unchecked
		onUpdate((T) value);
	}
}
