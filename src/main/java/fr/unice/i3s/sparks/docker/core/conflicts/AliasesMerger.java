package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliasesMerger {
    public static Map<ImageID, List<Dockerfile>> mergeWithAliases(Map<ImageID, List<Dockerfile>> parentToChildrenMap, Map<ImageID, ImageID> mappingAliases) {
        Map<ImageID, List<Dockerfile>> parentToChildrenMergedMap = new HashMap<>();
        for (Map.Entry<ImageID, List<Dockerfile>> entry : parentToChildrenMap.entrySet()) {
            ImageID currentImageID = entry.getKey();

            //  Check if an alias is recorded
            if (mappingAliases.containsKey(currentImageID)) {
                // Replace the current image id by its alias
                currentImageID = mappingAliases.get(currentImageID);
            }

            List<Dockerfile> children = new ArrayList<>();
            if (parentToChildrenMergedMap.containsKey(currentImageID)) {
                children = parentToChildrenMergedMap.get(currentImageID);
            }

            children.addAll(entry.getValue());
            parentToChildrenMergedMap.put(currentImageID, children);
        }
        return parentToChildrenMergedMap;
    }
}
