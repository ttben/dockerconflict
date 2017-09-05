package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.unice.i3s.sparks.docker.core.conflicts.Main.SILENT;

public class ParentToChildrenDockerfileBuilder {

    public static Map<ImageID, List<Dockerfile>> buildHierarchy(List<Dockerfile> dockerfiles, Map<ImageID, Dockerfile> mapSymbolicNameToDockerfiles) {
        Map<ImageID, List<Dockerfile>> parentToChildrenMap = new HashMap<>();

        int nbOfParentChildRelationShipEstablished = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            List<FROMCommand> listOfFrom = dockerfile.getActionsOfType(FROMCommand.class);

            if (listOfFrom.size() > 0) {
                FROMCommand fromCommand = listOfFrom.get(0); //  respect docker semantic
                ImageID parent = fromCommand.getParent();

                //  If a mapping exists for this parent dockerfile
                if (mapSymbolicNameToDockerfiles.containsKey(parent)) {
                    nbOfParentChildRelationShipEstablished++;

                    //  Set parent of current dockerfile
                    Dockerfile parentDockerfile = mapSymbolicNameToDockerfiles.get(parent);
                    dockerfile.setParent(parentDockerfile);
                }

                // Save this relation, the other way around
                List<Dockerfile> children = new ArrayList<>();
                if (parentToChildrenMap.containsKey(parent)) {
                    children = parentToChildrenMap.get(parent);
                }
                children.add(dockerfile);
                parentToChildrenMap.put(parent, children);
            } else {
                ImageID fakeParent = new ImageID("null");
                List<Dockerfile> children = new ArrayList<>();
                if (parentToChildrenMap.containsKey(fakeParent)) {
                    children = parentToChildrenMap.get(fakeParent);
                }
                children.add(dockerfile);
                parentToChildrenMap.put(fakeParent, children);
            }
        }

        if (!SILENT) {
            System.out.println("Nb. of parent/child relationship established:" + nbOfParentChildRelationShipEstablished);
            System.out.println("This represents " + ((nbOfParentChildRelationShipEstablished * 100) / dockerfiles.size()) + "% of the whole dataset.");
        }
        return parentToChildrenMap;
    }
}
