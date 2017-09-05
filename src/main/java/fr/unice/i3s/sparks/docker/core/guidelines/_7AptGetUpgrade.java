package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.*;
import java.util.stream.Collectors;

public class _7AptGetUpgrade extends Check<Dockerfile, List<Command>> {

    @Override
    public Map<Dockerfile, List<Command>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<Command>> result = new HashMap<>();
        int dockerfilesImpacted = 0, nbOfCommandsImpacted = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            List<Command> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                dockerfilesImpacted++;
                nbOfCommandsImpacted += conflict.size();

                result.put(dockerfile, conflict);
            }
        }
        if (!Main.SILENT) System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );

        return result;
    }

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

}