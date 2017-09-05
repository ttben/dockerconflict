package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.Main;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _3MultipleCMD extends Check<Dockerfile, Boolean> {

    public static int conflict(Dockerfile dockerfile) {
        return dockerfile.howMuch(CMDCommand.class);
    }


    @Override
    public Map<Dockerfile, Boolean> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, Boolean> result = new HashMap<>();

        int dockerfilesImpacted = 0, nbOfCommandsImpacted = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            int conflict = conflict(dockerfile);
            if (conflict > 0) {
                dockerfilesImpacted++;
                nbOfCommandsImpacted += conflict;

                result.put(dockerfile, true);
            }
        }

        if (!Main.SILENT) System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );

        return result;
    }
}
