package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Action;
import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptUpdateTag;
import fr.unice.i3s.sparks.docker.core.guidelines.*;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class Main {
    public static boolean SILENT = true;
    // FIXME PLEASE UNZIP THE dataset-dockerfile.zip FILE CONTAINED IN RESOURCE FOLDER
    // AND PUT THE RESULTING dockerfiles FOLDER UNDER src/main/resources/
    public static String PATH_TO_DKF = "";

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            PATH_TO_DKF = args[0];
        } else {
            URL resource = Main.class.getClassLoader().getResource("dockerfiles");
            PATH_TO_DKF = resource.getPath();
        }


        List<Dockerfile> dockerfiles = loadDockerfiles(PATH_TO_DKF);
        Preprocessor<Dockerfile> trivialFilter = new TrivialDkfPreprocessor();
        dockerfiles = trivialFilter.apply(dockerfiles);

        Map<ImageID, Dockerfile> mapSymbolicNameToDockerfiles = SymbolicNameToDockerfileMapper.imageIDDockerfileMap(dockerfiles);
        Map<ImageID, List<Dockerfile>> parentToChildrenMap = ParentToChildrenDockerfileBuilder.buildHierarchy(dockerfiles, mapSymbolicNameToDockerfiles);
        Map<ImageID, ImageID> mappingAliases = SymbolicNameToDockerfileMapper.aliases(dockerfiles);
        Map<ImageID, List<Dockerfile>> parentToChildrenMergedMap = AliasesMerger.mergeWithAliases(parentToChildrenMap, mappingAliases);
        List<Dockerfile> normalizedDockerfiles = normalize(parentToChildrenMergedMap);

        List<Integer> nbOfInstructions = new ArrayList<>();

        for (Dockerfile dockerfile : normalizedDockerfiles) {
            nbOfInstructions.add(dockerfile.getActions().size());
        }

        IntSummaryStatistics collect = nbOfInstructions.stream().collect(Collectors.summarizingInt(value -> value));
        //System.out.println(collect.toString());


        List<Integer> heightOnInheritance = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            Dockerfile parent = dockerfile;
            int height = -1;
            while (parent != null) {
                height++;
                parent = parent.getParent();
            }
            heightOnInheritance.add(height);
        }

        IntSummaryStatistics collectInheritance = heightOnInheritance.stream().collect(Collectors.summarizingInt(value -> value));
        //System.out.println(collectInheritance.toString());

        heightOnInheritance = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            Dockerfile parent = dockerfile;
            int height =0;
            while (parent != null) {
                height += parent.getActions().size();
                parent = parent.getParent();
            }
            heightOnInheritance.add(height);
        }

        collectInheritance = heightOnInheritance.stream().collect(Collectors.summarizingInt(value -> value));
       //System.out.println(collectInheritance.toString());
        //heightOnInheritance.stream().filter(integer -> integer > 100).forEach(System.out::println);

        int nbOfDockerfileThatInstalls = 0 ,nbOfRUNThatInstalls = 0 ,nbOfRUN = 0 ;
        for (Dockerfile dockerfile : dockerfiles) {
            int currentNbOfRUNThatInstalls = 0;
            for (Command command : dockerfile.getActions()) {
                if (command.containsTag(AptInstallTag.class)) {
                    currentNbOfRUNThatInstalls++;
                }

                if (command instanceof RUNCommand) {
                    nbOfRUN++;
                }
            }

            if (currentNbOfRUNThatInstalls > 0) {
                nbOfDockerfileThatInstalls++;
            }
            nbOfRUNThatInstalls += currentNbOfRUNThatInstalls;
        }

        System.out.println("nbOfDockerfileThatInstalls:"+nbOfDockerfileThatInstalls);
        System.out.println("nbOfRUNThatInstalls:"+nbOfRUNThatInstalls);
        System.out.println("nbOfRUN:"+nbOfRUN);


        //entry(dockerfiles);
        executeOnNormalizedDockerfiles(dockerfiles);

    }

    public static List<Dockerfile> entry(List<Dockerfile> dockerfiles) throws IOException {
        dockerfiles = executeGuidelines(dockerfiles);
        return dockerfiles;
    }

    public static List<Dockerfile> executeOnNormalizedDockerfiles(List<Dockerfile> dockerfiles) throws IOException {
        Map<ImageID, Dockerfile> mapSymbolicNameToDockerfiles = SymbolicNameToDockerfileMapper.imageIDDockerfileMap(dockerfiles);
        Map<ImageID, List<Dockerfile>> parentToChildrenMap = ParentToChildrenDockerfileBuilder.buildHierarchy(dockerfiles, mapSymbolicNameToDockerfiles);

        Map<ImageID, Integer> nameToNBMap = new HashMap<>();
        for (Map.Entry<ImageID, List<Dockerfile>> entry : parentToChildrenMap.entrySet()) {
            nameToNBMap.put(entry.getKey(), entry.getValue().size());
        }
        nameToNBMap = sortByValue(nameToNBMap);


        for (Map.Entry<ImageID, Integer> entry : nameToNBMap.entrySet()) {
            //System.out.println(entry.getKey() + "," + entry.getValue());
        }

        if (!SILENT) {
            System.out.println("Handling aliases ...");
        }

        Map<ImageID, ImageID> mappingAliases = SymbolicNameToDockerfileMapper.aliases(dockerfiles);

        Map<ImageID, List<Dockerfile>> parentToChildrenMergedMap = AliasesMerger.mergeWithAliases(parentToChildrenMap, mappingAliases);
        List<Dockerfile> normalizedDockerfiles = normalize(parentToChildrenMergedMap);
        System.out.println(normalizedDockerfiles.size() + " dockerfiles");

        int instructions = 0;
        for (Dockerfile dockerfile : normalizedDockerfiles) {
            instructions += dockerfile.getActions().size();

        }

        System.out.println(instructions + " nb instructions");


        normalizedDockerfiles = executeGuidelines(normalizedDockerfiles);


        return normalizedDockerfiles;

    }

    public static List<Dockerfile> normalize(Map<ImageID, List<Dockerfile>> parentToChildrenMergedMap) {
        List<Dockerfile> normalizedDockerfiles = new ArrayList<>();
        for (Map.Entry<ImageID, List<Dockerfile>> entry : parentToChildrenMergedMap.entrySet()) {
            List<Dockerfile> value = entry.getValue();
            for (Dockerfile dockerfile : value) {
                Dockerfile normalize = NormalizerOperator.normalize(dockerfile);
                normalizedDockerfiles.add(normalize);
            }
        }
        return normalizedDockerfiles;
    }

    public static void displayStatsFiles(List<Dockerfile> dockerfiles) {
        if (!SILENT) {
            System.out.println(dockerfiles.size() + " as input.");
        }
        int nbF = 0, nbC = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            int howMuchNonParse = dockerfile.howMuch(NonParsedCommand.class);
            if (howMuchNonParse > 0) {
                nbF++;
                nbC += howMuchNonParse;
            }
        }

        if (!SILENT) {
            System.out.println(nbC + " 'non-parsed' commands, over " + nbF + " dockerfiles.");
        }
    }

    public static List<Dockerfile> executeGuidelines(List<Dockerfile> dockerfiles) throws IOException {
        //Check<Dockerfile, Boolean> _1 = new _1FromFirst();
        Check<Dockerfile, List<Command>> _2 = new _2RunExecFormWithVariables();
        Check<Dockerfile, Boolean> _3 = new _3MultipleCMD();
        Check<Dockerfile, List<Command>> _5 = new _5CmdExecFormWithVariables();
        Check<Dockerfile, List<List<Command>>> _6 = new _6MergeableLabel();
        Check<Dockerfile, List<Command>> _7 = new _7AptGetUpgrade();
        Check<Dockerfile, List<Command>> _8 = new _8AlwaysUpdateAndInstallOnSameCommand();
        Check<Dockerfile, List<Command>> _9 = new _9PackageInstallationVersionPinning();
        //Check<Dockerfile, List<Command>> _10 = new _10FromVersionPinning();
        Check<Dockerfile, List<Command>> _12 = new _12AddDiscouraged();
        Check<Dockerfile, List<Command>> _13 = new _13AddHttpDiscouraged();
        Check<Dockerfile, List<Command>> _14 = new _14UserRoot();
        Check<Dockerfile, List<Command>> _15 = new _15LessUserCommands();
        Check<Dockerfile, List<Command>> _16 = new _16WorkdirAbsolutePath();
        Check<Dockerfile, List<Command>> _17 = new _17CdInRunCommand();
        Check<Dockerfile, List<Command>> _18 = new _18OrderPackageInstallation();
        Check<Dockerfile, List<Command>> _19 = new _19SpecifyNoInstallRecommends();


        Map<Check, Map<Dockerfile, Object>> apply = new Executor().apply(dockerfiles, Arrays.asList(_2, _3, _5, _6, _7, _8, _9, _12, _13, _14, _15, _16, _17, _18, _19));


        //List<RUNConflict> conflicts = new ArrayList<>();
        //fixAndOptimise(dockerfiles, conflicts);

        // possible interaction: _2, _7, _8, _9, _17, _18, _19

        /*
                _2  _7  _8  _9  _17  _18  _19
            _2  X
            _7      X
            _8          X
            _9              X
            _17                 X
            _18                       X
            _19                            X
         */


        Map<Dockerfile, List<Command>> _2conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_2));
        Map<Dockerfile, List<Command>> _7conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_7));
        Map<Dockerfile, List<Command>> _8conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_8));
        Map<Dockerfile, List<Command>> _9conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_9));
        Map<Dockerfile, List<Command>> _17conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_17));
        Map<Dockerfile, List<Command>> _18conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_18));
        Map<Dockerfile, List<Command>> _19conflicts = (Map<Dockerfile, List<Command>>) ((Object) apply.get(_19));


        int l = 0;
        for (Map.Entry<Dockerfile, List<Command>> entry : _8conflicts.entrySet()) {
            l += entry.getValue().size();
        }

        System.out.println("NBOfRUNCommand that violates:"+ l);
        System.out.println("NBOf dockerfile that violates:"+_8conflicts.keySet().size());

        cartesianProductOfConflictDetection(Arrays.asList(_2conflicts, _7conflicts, _8conflicts, _9conflicts, _17conflicts, _18conflicts, _19conflicts),
                Arrays.asList("1", "5", "6", "7", "13", "14", "15"));
        List<RUNConflict> conflicts = new ArrayList<>();
        fixAndOptimise(dockerfiles, conflicts);


        //  todo draw stacked barplot with: reading and parsing of dockerfiles, normalization, guidelines application
        //  todo check if no other rules modify the input
        /*
        List<RUNConflict> conflicts = new ArrayList<>();

        computeStatistics(dockerfiles);
        fixAndOptimise(dockerfiles, conflicts);
        computeStatistics(dockerfiles);
        */

        return dockerfiles;
    }

    private static void cartesianProductOfConflictDetection(List<Map<Dockerfile, List<Command>>> maps, List<String> names) {
        for (int i = 0; i < maps.size(); i++) {
            Map<Dockerfile, List<Command>> currentBase = maps.get(i);
            Set<Dockerfile> currentBaseDockerfile = currentBase.keySet();

            for (int j = i + 1; j < maps.size(); j++) {
                //System.out.println(i + "--" + j);
                Map<Dockerfile, List<Command>> otherConflict = maps.get(j);
                Set<Dockerfile> currentDockerfiles = otherConflict.keySet();
                Set<Dockerfile> intersection = new HashSet<>(currentBaseDockerfile);
                intersection.retainAll(currentDockerfiles);
                //System.out.printf("Dockerfiles,%s,%s,%s\n", names.get(i), names.get(j), intersection.size());

                int nbInstructionWhichIntersect = 0;
                for (Dockerfile intersected : intersection) {
                    List<Command> baseCommand = currentBase.get(intersected);
                    List<Command> currentCommand = otherConflict.get(intersected);

                    for (Command c : baseCommand) {
                        for (Command c2 : currentCommand) {
                            if (c == c2) {
                                nbInstructionWhichIntersect++;
                            }
                        }
                    }
                }
                System.out.printf("Instruction,%s,%s,%s\n", names.get(i), names.get(j), nbInstructionWhichIntersect);

            }
        }
    }

    private static int getNumberOf(Check check, Map<Check, Map<Dockerfile, Object>> apply) {
        if (!SILENT) System.out.println(check.getClass().getSimpleName());
        if (apply.containsKey(check)) {
            return apply.get(check).size();
        }

        return 0;
    }


    private static int getDeepNumberOf(Check check, Map<Check, Map<Dockerfile, Object>> apply) {
        if (!SILENT) System.out.println(check.getClass().getSimpleName());

        Map<Dockerfile, Object> target = apply.get(check);
        int nb = 0;
        for (Object o : target.values()) {
            if (o instanceof List) {
                if (((List) o).get(0) instanceof List) {
                    List<List> t = (List<List>) o;
                    for (List z : t) {
                        nb += z.size() - 1;
                    }
                } else {
                    nb += ((List) o).size();
                }
            }
        }

        return nb;
    }

    public static List<Dockerfile> loadDockerfiles(String pathToDkf) {
        if (!SILENT) {
            System.out.println("Loading dockerfiles...");
        }
        File[] files = getFiles(pathToDkf);
        try {
            return buildDockerfiles(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
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

    public static File[] getFiles(String pathToDkf) {
        FilenameFilter textFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("-dockerfile") || lowercaseName.endsWith(".dockerfile");
        };


        File folder = new File(pathToDkf);

        return folder.listFiles(textFilter);
    }


    private static void fixAndOptimise(List<Dockerfile> dockerfiles, List<RUNConflict> conflicts) {
        fixSemanticGapIssue(dockerfiles);
        mergeContiguousRun(dockerfiles);
    }

    private static void mergeContiguousRun(List<Dockerfile> dockerfiles) {
        List<List<List<OptimMultipleRun.Issue>>> optim1 = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            OptimMultipleRun optim1Issue = new OptimMultipleRun();
            List<List<OptimMultipleRun.Issue>> optim1IssueApplied = optim1Issue.apply(dockerfile);
            if (!optim1IssueApplied.isEmpty()) {
                optim1.add(optim1IssueApplied);
            }
        }

        int gain = 0;
        for (List<List<OptimMultipleRun.Issue>> dockerfileClusters : optim1) {
            for (List<OptimMultipleRun.Issue> localMerge : dockerfileClusters) {
                gain += localMerge.size();
            }
        }

        if (!SILENT){}
            System.out.println("Rule: merge contiguous run.\n\t-> Number of run commands that can be deleted (by merge operation): " + gain + " commands.");
    }

    private static void fixSemanticGapIssue(List<Dockerfile> dockerfiles) {
        List<List<RunIssue1.Issue>> issues = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            RunIssue1 runIssue1 = new RunIssue1();
            List<RunIssue1.Issue> apply = runIssue1.apply(dockerfile);
            if (!apply.isEmpty()) {
                issues.add(apply);
            }
        }

        int nbOfIssues = 0;
        for (List<RunIssue1.Issue> dockerfileClusters : issues) {
            nbOfIssues += dockerfileClusters.size();
        }
        if (!SILENT)
            System.out.println("Rule: run semantic gap.\n\t-> Number of run commands that have r.s.g. issue : " + nbOfIssues + " commands over " + issues.size() + " dockerfiles.");
    }


    private static void computeStatistics(List<Dockerfile> dockerfiles) {
        List<Dockerfile> dockerfilesWithRUN = new ArrayList<>();
        List<Dockerfile> dockerfilesWithUpdateInstall = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            //System.out.println(dockerfile.getSourceFile());

            if (dockerfile.contains(RUNCommand.class)) {
                dockerfilesWithRUN.add(dockerfile);
            }

            if (dockerfile.containsTag(AptInstallTag.class) && dockerfile.containsTag(AptUpdateTag.class)) {
                dockerfilesWithUpdateInstall.add(dockerfile);
            }
        }


        if (!SILENT) System.out.println("-------------------------------------");
        if (!SILENT) System.out.println(dockerfiles.size() + " dockerfiles parsed into model.");


        Map<Class, Integer> repartitionsOfCommands = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            computeRepartitionsOfCommands(FROMCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(RUNCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ADDCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(COPYCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ENTRYPointCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(WORKDIRCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(VOLUMECommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(CMDCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(EXPOSECommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ENVCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(MAINTAINERCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(USERCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(LABELCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ARGCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ONBUILDCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(NonParsedCommand.class, repartitionsOfCommands, dockerfile);
        }

        printRepartition(sortByValue(repartitionsOfCommands));
        // if (!SILENT) System.out.println(Generator.generatePieChart(repartitionsOfCommands));
        // if (!SILENT) System.out.println(Generator.generateBarChart(repartitionsOfCommands, "Command type", "Number of commands", "Repartitions of commands type"));

        int totalNbOfCommands = 0;
        for (Integer i : repartitionsOfCommands.values()) {
            totalNbOfCommands += i;
        }
        if (!SILENT) System.out.println("Total => " + totalNbOfCommands);


        int expectedTotalNbOfCommands = 0;
        List<Action> actions = new ArrayList<>();
        for (Dockerfile dockerfile : dockerfiles) {
            List<Command> actions1 = dockerfile.getActions();
            actions.addAll(actions1);
            expectedTotalNbOfCommands += actions1.size();
        }
        if (!SILENT) System.out.println("Expected? total => " + expectedTotalNbOfCommands);

        repartitionsOfCommands = new HashMap<>();

        printRepartition(sortByValue(repartitionsOfCommands));


        if (!SILENT) System.out.println("--------------------------------------------------------------------------");

        List<Dockerfile> extract = extract(NonParsedCommand.class, dockerfiles);
        //System.out.println();

        int nbInstallApt = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            List<RUNCommand> actionsOfType = dockerfile.getActionsOfType(RUNCommand.class);
            for (RUNCommand runCommand : actionsOfType) {
                List<ShellCommand> body = runCommand.getBody();
                for (ShellCommand shellCommand : body) {
                    if (shellCommand.containsTag(AptInstallTag.class)) {
                        nbInstallApt++;
                    }
                }
            }
        }

        if (!SILENT) System.out.println("InstallAPT:" + nbInstallApt);
    }


    private static void printRepartition(Map<Class, Integer> repartitionsOfCommands) {
        for (Map.Entry<Class, Integer> entry : repartitionsOfCommands.entrySet()) {
            if (!SILENT) System.out.println("-" + entry.getKey().getSimpleName() + ":" + entry.getValue());
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private static void computeRepartitionsOfCommands(Class clazz, Map<Class, Integer> repartitionsOfCommands, Dockerfile dockerfile) {
        int command = dockerfile.howMuch(clazz);
        if (repartitionsOfCommands.containsKey(clazz)) {
            repartitionsOfCommands.put(clazz, command + repartitionsOfCommands.get(clazz));
        } else {
            repartitionsOfCommands.put(clazz, command);
        }
    }

    private static List<Dockerfile> extract(Class clazz, List<Dockerfile> dockerfiles) {
        List<Dockerfile> result = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            int command = dockerfile.howMuch(clazz);
            if (command > 0) {
                result.add(dockerfile);
            }
        }

        return result;
    }


    public static void percentageOf(int thiz, int overThis, String msg) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        double percentage = ((double) thiz * 100) / ((double) overThis);
        if (!SILENT) System.out.println(df.format(percentage) + "% " + msg + " (" + thiz + "/" + overThis + ")");
    }
}
