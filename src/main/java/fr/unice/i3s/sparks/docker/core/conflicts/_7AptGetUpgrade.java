package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.*;
import java.util.stream.Collectors;

public class _7AptGetUpgrade extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerFiles) {

        List<Command> result = new ArrayList<>();

        ArrayList<RUNCommand> runCommands = dockerFiles.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (RUNCommand runCommand : runCommands) {
            List<ShellCommand> body = runCommand.getBody();
            for (ShellCommand shellCommand : body) {
                List<String> shellCommandBody = shellCommand.getBody();
                ListIterator<String> stringListIterator = shellCommandBody.listIterator();
               while (stringListIterator.hasNext()){
                   String instruction = stringListIterator.next();
                    if (instruction.toLowerCase().trim().replaceAll("\\s+", " ").equals("apt-get")) {
                        if (stringListIterator.hasNext()) {
                            instruction = stringListIterator.next();
                            if (instruction.toLowerCase().trim().replaceAll("\\s+", " ").equals("upgrade")) {
                                result.add(runCommand);
                            }
                            instruction = stringListIterator.previous();
                        }
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("lol")),
                new ENVCommand("e", "e"),
                new RUNCommand(new ShellCommand("apt-get", "upgrade"), new ShellCommand("apt-get", "install", "X"))
        );

        List<Command> conflict = _7AptGetUpgrade.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("lol")),
                new ENVCommand("e", "e"),
                new RUNCommand(new ShellCommand("apt-get", "install", "X"), new ShellCommand("apt-get", "upgrade"))
        );

        conflict = _7AptGetUpgrade.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("lol")),
                new ENVCommand("e", "e"),
                new RUNCommand(new ShellCommand("apt-get", "install", "X"), new ShellCommand("apt-get", "upgrad"))
        );

        conflict = _7AptGetUpgrade.conflict(dockerfile);
        System.out.println(conflict);
    }

    @Override
    public Map<Dockerfile, List<Command>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<Command>> result = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            List<Command> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                result.put(dockerfile, conflict);
            }
        }

        return result;
    }
}