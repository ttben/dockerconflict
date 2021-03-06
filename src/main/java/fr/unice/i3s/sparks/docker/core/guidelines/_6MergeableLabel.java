package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.LABELCommand;

import java.util.*;

public class _6MergeableLabel extends Check<Dockerfile, List<List<Command>>> {
    @Override
    public Map<Dockerfile, List<List<Command>>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<List<Command>>> result = new HashMap<>();

        int dockerfilesImpacted = 0, nbOfCommandsImpacted = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            List<List<Command>> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                dockerfilesImpacted++;

                for (List<Command> cluster : conflict) {
                    nbOfCommandsImpacted += cluster.size() - 1;
                }

                result.put(dockerfile, conflict);
            }
        }
        if (!Main.SILENT) System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );

        return result;

    }

    public static List<List<Command>> conflict(Dockerfile dockerfile) {
        List<List<Command>> result = new ArrayList<>();

        List<Command> listOfCommand = dockerfile.getActions();

        ListIterator<Command> commandListIterator = listOfCommand.listIterator();

        while (commandListIterator.hasNext()) {
            Command currentCommand = commandListIterator.next();
            List<Command> currentCluster = new ArrayList<>();
            while (currentCommand instanceof LABELCommand && commandListIterator.hasNext()) {
                currentCluster.add(currentCommand);
                currentCommand = commandListIterator.next();
            }

            if (currentCommand instanceof LABELCommand) {
                currentCluster.add(currentCommand);
            }

            if (currentCluster.size() > 1) {
                result.add(currentCluster);
            }
        }


        return result;
    }
}