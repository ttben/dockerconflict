package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.LinkedList;
import java.util.List;

public class NormalizerOperator {
    public static Dockerfile normalize(Dockerfile dockerfile) {
        Dockerfile result = new Dockerfile(dockerfile.getSourceFile());

        List<FROMCommand> actionsOfType = dockerfile.getActionsOfType(FROMCommand.class);
        if (actionsOfType.size() > 0) {
            if (actionsOfType.get(0).getParent().equals(new ImageID("ubuntu:16.04"))) {
                dockerfile.getActions();
            }
        }

        LinkedList<Dockerfile> hierarchy = new LinkedList<>();

        Dockerfile parent = dockerfile;

        // While there is parent which is NOT SCRATCH
        while (parent != null && parent.getActions().size() > 0) {
            hierarchy.addFirst(parent);
            parent = parent.getParent();
        }

        for (int i = 0 ; i < hierarchy.size() ; i++) {
            Dockerfile dockerfile1 = hierarchy.get(i);
                List<Command> actions = dockerfile1.getActions();
                for (int j = 0 ; j < actions.size() ; j++) {
                    if (j == 0 && actions.get(j) instanceof FROMCommand) {
                        // do nothing
                    } else {
                        result.add(actions.get(j));
                    }
                }

        }


        return result;
    }
}
