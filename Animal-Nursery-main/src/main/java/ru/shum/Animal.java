package ru.shum;
public class Animal {
  private String name;
  private String command;

  public Animal(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }
}