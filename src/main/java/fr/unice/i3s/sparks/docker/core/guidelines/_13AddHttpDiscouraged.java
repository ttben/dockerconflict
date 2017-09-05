package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ADDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _13AddHttpDiscouraged extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerfile) {
        List<Command> result = new ArrayList<>();

        ArrayList<ADDCommand> addCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof ADDCommand)
                .map(ADDCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (ADDCommand addCommand : addCommands) {
            for (String string : addCommand.getBody()) {
                if (string.trim().startsWith("http")) {
                    result.add(addCommand);
                }
            }
        }

        return result;
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
        if (!Main.SILENT)System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );

        return result;
    }
}
