package ru.shum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PetRegistry implements AutoCloseable {

  private List<Animal> animals = new ArrayList<>();

  private static Counter counter = new Counter();

  public void addNewAnimal(Animal animal) {
    animals.add(animal);
    counter.add();
  }

  public void teachCommand(Animal animal, String command) {
    animal.setCommand(command);

    // Запись данных в базу данных
    try (FileWriter writer = new FileWriter("DataBase.csv", true)) {
      String animalType = getAnimalType(animal);
      String animalName = animal.getName();
      String line = animalType + "," + animalName + "," + command + "\n";
      writer.write(line);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getAnimalType(Animal animal) {
    if (animal instanceof Dog) {
      return "Dog";
    } else if (animal instanceof Cat) {
      return "Cat";
    } else if (animal instanceof Hamster) {
      return "Hamster";
    } else if (animal instanceof Horse) {
      return "Horse";
    } else if (animal instanceof Camel) {
      return "Camel";
    } else if (animal instanceof Donkey) {
      return "Donkey";
    }
    return "";
  }

  public List<String> getCommands(Animal animal) {
    List<String> commands = new ArrayList<>();
    commands.add(animal.getCommand());
    return commands;
  }

  public void readDatabase() {
    // Создание файла базы данных, если он не существует
    File databaseFile = new File("DataBase.csv");
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Чтение данных из базы данных
    try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data.length >= 2) {
          String animalName = data[0];
          String command = data[1];
          Animal animal = animals.stream().filter(a -> a.getName().equals(animalName)).findFirst().orElse(null);
          if (animal != null) {
            animal.setCommand(command);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    try (PetRegistry petRegistry = new PetRegistry()) {
      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.println("1. Add new animal");
        System.out.println("2. Teach command");
        System.out.println("3. Get commands");
        System.out.println("4. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
          case 1:
            System.out.println("Enter animal type: ");
            String type = scanner.nextLine();
            System.out.println("Enter animal name: ");
            String name = scanner.nextLine();
            Animal animal;
            switch (type) {
              case "Dog":
                animal = new Dog(name);
                break;
              case "Cat":
                animal = new Cat(name);
                break;
              case "Hamster":
                animal = new Hamster(name);
                break;
              case "Horse":
                animal = new Horse(name);
                break;
              case "Camel":
                animal = new Camel(name);
                break;
              case "Donkey":
                animal = new Donkey(name);
                break;
              default:
                throw new IllegalStateException("Unexpected value: " + type);
            }
            petRegistry.addNewAnimal(animal);
            break;
          case 2:
            System.out.println("Enter animal name: ");
            String animalName = scanner.nextLine();
            Animal foundAnimal = petRegistry.animals.stream()
                .filter(a -> a.getName().equals(animalName))
                .findFirst()
                .orElse(null);
            if (foundAnimal == null) {
              System.out.println("No such animal");
              break;
            }
            System.out.println("Enter command: ");
            String command = scanner.nextLine();
            petRegistry.teachCommand(foundAnimal, command);
            break;
          case 3:
            System.out.println("Enter animal name: ");
            String aName = scanner.nextLine();
            Animal fAnimal = petRegistry.animals.stream()
                .filter(a -> a.getName().equals(aName))
                .findFirst()
                .orElse(null);
            if (fAnimal == null) {
              System.out.println("No such animal");
              break;
            }
            List<String> commands = petRegistry.getCommands(fAnimal);
            for (String cmd : commands) {
              System.out.println(cmd);
            }
            break;
          case 4:
            return;
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public void close() throws Exception {
    if (counter.getCount() == 0) {
      throw new Exception("Counter was not used in try-with-resources block");
    } else {
      counter.resetCount();
    }
  }

}

class Counter {

  private int count;

  public void add() {
    count++;
  }

  public int getCount() {
    return count;
  }

  public void resetCount() {
    count = 0;
  }

}
