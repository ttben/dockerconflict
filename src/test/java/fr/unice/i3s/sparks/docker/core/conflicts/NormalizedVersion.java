package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fr.unice.i3s.sparks.docker.core.conflicts.AliasesMerger.mergeWithAliases;
import static fr.unice.i3s.sparks.docker.core.conflicts.Main.PATH_TO_DKF;
import static junit.framework.TestCase.assertEquals;

public class NormalizedVersion {
    private static List<Dockerfile> dockerfiles;
    private static Map<ImageID, List<Dockerfile>> parentToChildrenMergedMap;

    @BeforeClass
    public static void loadDockerfiles() {
        File[] files = getFiles();
        try {
            dockerfiles = buildDockerfiles(files);
            Map<ImageID, Dockerfile> mapSymbolicNameToDockerfiles = SymbolicNameToDockerfileMapper.imageIDDockerfileMap(dockerfiles);
            Map<ImageID, List<Dockerfile>> parentToChildrenMap = ParentToChildrenDockerfileBuilder.buildHierarchy(dockerfiles, mapSymbolicNameToDockerfiles);
            Map<ImageID, ImageID> mappingAliases = SymbolicNameToDockerfileMapper.aliases(dockerfiles);
            parentToChildrenMergedMap = mergeWithAliases(parentToChildrenMap, mappingAliases);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Dockerfile> buildDockerfiles(File[] files) throws IOException {
        List<Dockerfile> dockerfiles = new ArrayList<>();
        for (File f : files) {
            //System.out.println("Handling file:" + f.getAbsolutePath());
            Dockerfile dockerfile = DockerFileParser.parse(f);
            Dockerfile enrichedDockerfile = Enricher.enrich(dockerfile);
            dockerfiles.add(enrichedDockerfile);
        }

        return dockerfiles;
    }

    public static File[] getFiles() {
        FilenameFilter textFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("-dockerfile") || lowercaseName.endsWith(".dockerfile");
        };


        String folderThatContainsDockerfiles = PATH_TO_DKF;
        File folder = new File(folderThatContainsDockerfiles);

        return folder.listFiles(textFilter);
    }


    @Test
    public void test() {
        for (Dockerfile dockerfile : dockerfiles) {
            int expectedNbOfRun = 0, expectedNbOfAdd = 0, expectedNbOfCopy = 0, expectedNbOfCmd = 0, expectedNbOfWorkdir = 0, expectedNbOfExpose = 0, expectedNbOfEnv = 0;

            Dockerfile parent = dockerfile;

            while (parent != null) {

                expectedNbOfRun += parent.howMuch(RUNCommand.class);
                expectedNbOfAdd += parent.howMuch(ADDCommand.class);
                expectedNbOfCopy += parent.howMuch(COPYCommand.class);
                expectedNbOfCmd += parent.howMuch(CMDCommand.class);
                expectedNbOfWorkdir += parent.howMuch(WORKDIRCommand.class);
                expectedNbOfExpose += parent.howMuch(EXPOSECommand.class);
                expectedNbOfEnv += parent.howMuch(ENVCommand.class);

                parent = parent.getParent();
            }


            Dockerfile normalizedDockerfile = NormalizerOperator.normalize(dockerfile);

            assertEquals(expectedNbOfRun, normalizedDockerfile.howMuch(RUNCommand.class));
            assertEquals(expectedNbOfAdd, normalizedDockerfile.howMuch(ADDCommand.class));
            assertEquals(expectedNbOfCopy, normalizedDockerfile.howMuch(COPYCommand.class));
            assertEquals(expectedNbOfCmd, normalizedDockerfile.howMuch(CMDCommand.class));
            assertEquals(expectedNbOfWorkdir, normalizedDockerfile.howMuch(WORKDIRCommand.class));
            assertEquals(expectedNbOfExpose, normalizedDockerfile.howMuch(EXPOSECommand.class));
            assertEquals(expectedNbOfEnv, normalizedDockerfile.howMuch(ENVCommand.class));
        }


    }
}
