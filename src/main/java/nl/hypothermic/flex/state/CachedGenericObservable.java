package nl.hypothermic.flex.state;

public abstract class CachedGenericObservable<T> extends GenericObservable<T> {

	private T lastValue;

	@Override
	protected void onUpdate(T newValue) {
		this.lastValue = newValue;
		super.onUpdate(newValue);
	}

	@Override
	public void addObserver(GenericObserver<T> observer) {
		super.addObserver(observer);

		if (null != this.lastValue) {
			observer.onUpdate(this.lastValue);
		}
	}

	public T getLastValue() {
		return this.lastValue;
	}
}
