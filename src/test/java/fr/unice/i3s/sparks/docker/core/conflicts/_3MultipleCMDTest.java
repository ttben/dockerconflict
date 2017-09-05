package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.guidelines._3MultipleCMD;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _3MultipleCMDTest {

    @Test
    public void shouldYieldWarning() {
        Dockerfile aDockerfile = new Dockerfile();
        CMDCommand node = new CMDCommand(aDockerfile, "node");
        CMDCommand node1 = new CMDCommand(aDockerfile, "node");
        aDockerfile.add(node);
        aDockerfile.add(node1);

        assertTrue(_3MultipleCMD.conflict(aDockerfile));
    }

    @Test
    public void shouldNotYieldWarning() {
        Dockerfile aDockerfile = new Dockerfile();
        CMDCommand node = new CMDCommand(aDockerfile, "node");
        aDockerfile.add(node);

        assertFalse(_3MultipleCMD.conflict(aDockerfile));
    }
}