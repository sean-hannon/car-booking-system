package com.seanhannon.util;

public class SearchCriteria {
  private String key;
  private String operation;
  private Object value;
  private boolean orPredicate;

  public SearchCriteria() {

  }

  public SearchCriteria(final String key, final String operation, final Object value) {
    super();
    this.key = key;
    this.operation = operation;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(final String operation) {
    this.operation = operation;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(final Object value) {
    this.value = value;
  }

  public boolean isOrPredicate() {
    return orPredicate;
  }

  public void setOrPredicate(boolean orPredicate) {
    this.orPredicate = orPredicate;
  }

}
