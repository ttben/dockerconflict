package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.RunIssue1;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _8AlwaysUpdateAndInstallOnSameCommand extends Check<Dockerfile, List<RunIssue1.Issue>>{

    @Override
    public Map<Dockerfile, List<RunIssue1.Issue>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<RunIssue1.Issue>> result = new HashMap<>();
        int dockerfilesImpacted = 0, nbOfCommandsImpacted = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            List<RunIssue1.Issue> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                dockerfilesImpacted++;
                nbOfCommandsImpacted += conflict.size();

                result.put(dockerfile, conflict);
            }
        }

        System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );
        return result;
    }

    public static List<RunIssue1.Issue> conflict(Dockerfile dockerfile) {
        RunIssue1 o = new RunIssue1();
        List<RunIssue1.Issue> apply = o.apply(dockerfile);
        return apply;
    }

}
