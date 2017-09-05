package fr.unice.i3s.sparks.docker.core.guidelines;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class _2RunExecFormWithVariables extends Check<Dockerfile, List<Command>> {
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

        System.out.printf("%s,%s,%s\n",getClass().getSimpleName(), nbOfCommandsImpacted, dockerfilesImpacted );

        return result;
    }

    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        List<RUNCommand> execFormRun = new ArrayList<>();
        for (RUNCommand runCommand : runCommands) {
            if (isExecForm(runCommand)) {
                execFormRun.add(runCommand);
            }
        }

        for (RUNCommand runCommand : execFormRun) {
            if (useVariable(runCommand)) {
                result.add(runCommand);
            }
        }

        return result;
    }

    private static boolean useVariable(RUNCommand runCommand) {

        for (ShellCommand shellCommand : runCommand.getBody()) {
            for (String instruction : shellCommand.getBody()) {
                String s = instruction.trim().toLowerCase();
                Matcher matcher = Pattern.compile("\\$[a-zA-Z0-9_\\-]+").matcher(s);
                if (matcher.matches()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isExecForm(RUNCommand runCommand) {
        return runCommand.getBody().size() > 0 && !runCommand.getBody().get(0).getBody().get(0).trim().toLowerCase().startsWith("/bin");
    }
}
