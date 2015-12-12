package de.johoop.findbugs4sbt;

import java.util.List;

public class TestClass {

  private List<String> strings;
  private int n;

  public TestClass(int n) {
    this.n = n;
  }

  public static void main(String[] args) {
    System.out.println("Hello, world!");
  }

  public List<String> getStrings() {
    return strings;
  }

  public void setN(int n) {
    n = n;
  }

}
