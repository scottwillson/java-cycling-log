package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortTool {

  public SortTool() { }

  /**
   * @TODO: Use in Velocity template
   */
  public List natural(Collection collection) {
  	if (collection == null) {
  		throw new RuntimeException("Cannot sort null collection");
  	}
  	List list = new ArrayList(collection);
	Collections.sort(list);
	return list;
  }

}