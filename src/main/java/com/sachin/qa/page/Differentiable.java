package com.sachin.qa.page;

import java.io.Serializable;

public interface Differentiable extends Serializable {
	void apply() throws Exception;

	void differ(DifferenceType type) throws Exception;
}
