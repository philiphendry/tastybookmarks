package uk.philiphendry.delicious.entities;

import java.io.Serializable;

public class Tag implements Serializable {
	private static final long serialVersionUID = -576663988868317909L;
	
	public static final String NAME = "name";
	public static final String COUNT = "count";

	private String name;
	private int count;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
	
}
