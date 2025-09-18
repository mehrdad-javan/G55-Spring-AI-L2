package se.lexicon.g55springail2.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppToolCalling {

    List<String> storageSimulation = new ArrayList<>();

    public AppToolCalling() {
        storageSimulation.addAll(List.of("Mehrdad", "Elnaz", "John", "Simon", "Marcus"));
        System.out.println("storageSimulation = " + storageSimulation);
    }


    @Tool(description = "Fetch all names from the application")
    public List<String> fetchAllNames() {
        System.out.println("Fetching names...");
        return storageSimulation.stream().toList();
    }

    @Tool(description = "Add a new name to the application")
    public String addNewName(String name) {
        System.out.println("Adding a new name...");
        storageSimulation.add(name);
        return "Operation successful. New name added: " + name;
    }

    @Tool(description = "Find names that contain a specific keyword")
    public String findByName(String name) {
        System.out.println("Finding a name...");
        List<String> result = storageSimulation.stream()
                .filter(n -> n.toLowerCase().contains(name.toLowerCase()))
                .toList();
        if (result.isEmpty()) {
            return "Name not found: " + name;
        } else {
            return "Name found: " + String.join(", ", result);
        }
    }

}
