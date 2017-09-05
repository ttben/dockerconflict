package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DockerfileTree {
    private Map<String, Dockerfile> nameToDkfMap = new HashMap<>();
    private Map<ImageID, Dockerfile> imageIDToDkfMap = new HashMap<>();
    private Dockerfile scratch = new Dockerfile();

    private Map<Dockerfile, Set<Dockerfile>> parentChildRelationShip = new HashMap<>();
    public void addDockerfile(Dockerfile dockerfile, Set<Dockerfile> children) {
        this.parentChildRelationShip.put(dockerfile, children);
    }


}
