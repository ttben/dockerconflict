package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.*;
import java.util.stream.Collectors;

public class _9PackageInstallationVersionPinning extends Check<Dockerfile, List<Command>> {

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

        if (!Main.SILENT) System.out.printf("%s,%s,%s\n", getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted);
        return result;
    }

    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (RUNCommand runCommand : runCommands) {
            if (!runCommand.containsTag(AptInstallTag.class)) {
                continue;
            }

            for (ShellCommand shellCommand : runCommand.getBody()) {

                if (shellCommand.containsTag(AptInstallTag.class)) {
                    List<String> body = shellCommand.getBody();
                    ListIterator<String> stringListIterator = body.listIterator();

                    String current = null;
                    while (stringListIterator.hasNext()) {
                        current = stringListIterator.next();
                        if (current.equals("apt-get")) {
                            continue;
                        }

                        if (current.equals("install")) {
                            continue;
                        }

                        if (current.equals("-y")) {
                            continue;
                        }

                        if (current.split(":").length == 1) {
                            result.add(runCommand);
                            break;  // FIXME THIS MAKES ME LOST TWO F********** HOURS
                        }
                    }
                }
            }
        }

        return result;
    }
}
