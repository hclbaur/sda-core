package be.baur.sda.util;

import java.util.ArrayList;

/**
 * A convenience class for a list of operational results.
 * 
 * @see Result
 */
@SuppressWarnings("serial")
public class Results<T> extends ArrayList<Result<T>> {

	/**
	 * Appends a result to this list. This method ignores a null argument.
	 * 
	 * @param result a result, may be null
	 * @return true if a result was added
	 */
	@Override
	public boolean add(Result<T> result) {
		if (result == null) return false;
		return super.add(result);
	}
	
	
	/**
	 * Appends an error result to this list. This method ignores both null arguments
	 * and OK results.
	 * 
	 * @param result a result, may be null
	 * @return true if a result was added
	 */
	public boolean addError(Result<T> result) {
		if (result == null || result.isOK()) return false;
		return super.add(result);
	}

}
