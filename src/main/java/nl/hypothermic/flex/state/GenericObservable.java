package nl.hypothermic.flex.state;

import java.util.HashSet;
import java.util.Set;

public abstract class GenericObservable<T> {

	private final Set<GenericObserver<T>> observers;

	{
		this.observers = new HashSet<>();
	}

	protected void onUpdate(T newValue) {
		this.observers.forEach(observer -> observer.onUpdate(newValue));
	}

	public void addObserver(GenericObserver<T> observer) {
		this.observers.add(observer);
	}

	public void removeObserver(GenericObserver<T> observer) {
		this.observers.remove(observer);
	}
}
