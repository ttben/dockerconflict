package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _19SpecifyNoInstallRecommends extends Check<Dockerfile, List<Command>> {

    public static <T extends Command> List<T> lol(Dockerfile dockerfile) {
        Class<T> l = null;
        List<T> listOfCommand = (List<T>) dockerfile.getActions();
        List<T> collect = new ArrayList<>();
        for (T t : listOfCommand) {
            if (l.isInstance(t)) {
                collect.add(t);
            }
        }

        return collect;
    }

    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands =
                dockerfile.getActions()
                        .stream()
                        .filter(c -> c instanceof RUNCommand)
                        .map(RUNCommand.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (RUNCommand runCommand : runCommands) {
            for (ShellCommand shellCommand : runCommand.getBody()) {

                if (shellCommand.containsTag(AptInstallTag.class)) {

                    List<String> body = shellCommand.getBody();

                    if (!body.contains("--no-install-recommends")) {
                        result.add(runCommand);
                    }

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
        if (!Main.SILENT) System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );

        return result;
    }
}
