package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.USERCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _15LessUserCommands extends Check<Dockerfile, List<Command>> {
    public static final int THRESHOLD = 4;

    public static List<Command> conflict(Dockerfile dockerfile) {
        int nbOfUserCommands = dockerfile.howMuch(USERCommand.class);
        if (nbOfUserCommands > THRESHOLD) {
            return dockerfile.getActions()
                    .stream()
                    .filter(c -> c instanceof USERCommand)
                    .map(USERCommand.class::cast)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new ArrayList<>();
    }


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
}
