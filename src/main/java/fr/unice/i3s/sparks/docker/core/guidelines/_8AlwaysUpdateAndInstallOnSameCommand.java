package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.conflicts.RunIssue1;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptUpdateTag;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _8AlwaysUpdateAndInstallOnSameCommand extends Check<Dockerfile, List<Command>>{

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

    public static List<Command> conflict(Dockerfile dockerfile) {
        List<Command> result = new ArrayList<>();
        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (RUNCommand runCommand : runCommands) {
            boolean flawed = false;

            boolean install = false, update = false;

            List<ShellCommand> body = runCommand.getBody();
            for (ShellCommand shellCommand : body) {
                if (shellCommand.containsTag(AptInstallTag.class)) {
                    install=true;
                }
                if (shellCommand.containsTag(AptUpdateTag.class)) {
                    update = true;
                }
            }

            flawed = !update && install;

            if(flawed) {
                result.add(runCommand);
            }
        }

        return result;
    }

}
