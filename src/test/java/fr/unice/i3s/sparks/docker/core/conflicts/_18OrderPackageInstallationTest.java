package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.guidelines._18OrderPackageInstallation;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _18OrderPackageInstallationTest {

    @Test
    public void shouldYieldWarning() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "-y", "ruby:203", "quby:203");
        shellCommand.addTag(new AptInstallTag());

        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

        ShellCommand shellCommand1 = new ShellCommand("apt-get", "install", "-y", "ruby:203", "aeuby:203");
        shellCommand1.addTag(new AptInstallTag());

        RUNCommand runCommand1 = new RUNCommand(shellCommand1);
        runCommand.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand1
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

    }
    @Test
    public void shouldNotYieldWarning() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "-y", "java", "ruby");
        shellCommand.addTag(new AptInstallTag());
        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());

        ShellCommand shellCommand1 = new ShellCommand("apt-get", "install", "ruby");
        shellCommand1.addTag(new AptInstallTag());

        RUNCommand runCommand1 = new RUNCommand(shellCommand1);
        runCommand1.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand1
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());

        ShellCommand shellCommand2 = new ShellCommand("apt-get", "install", "ruby:4", "wget");
        shellCommand2.addTag(new AptInstallTag());

        RUNCommand runCommand2 = new RUNCommand(shellCommand2);
        runCommand2.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand2
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());


        ShellCommand shellCommand3 = new ShellCommand("apt-get", "install", "ruby:4", "zeubi");
        shellCommand3.addTag(new AptInstallTag());

        RUNCommand runCommand3 = new RUNCommand(shellCommand3);
        runCommand3.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand3
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}