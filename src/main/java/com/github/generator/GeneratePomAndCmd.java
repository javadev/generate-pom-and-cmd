package com.github.generator;

import com.github.underscore.U;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GeneratePomAndCmd {
    private static final String HEADER = "POM and CMD file generator, version 2020-08-08";
    private static final String MESSAGE = "For docs, license, tests, and downloads, see: https://github.com/javadev/generate-pom-and-cmd";
    private static final String POM_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
"    <modelVersion>4.0.0</modelVersion>\n" +
"    <groupId>com.mycompany</groupId>\n" +
"    <artifactId>DatabaseDiff</artifactId>\n" +
"    <version>1.0-SNAPSHOT</version>\n" +
"    <packaging>jar</packaging>\n" +
"    <properties>\n" +
"        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
"        <maven.compiler.source>1.8</maven.compiler.source>\n" +
"        <maven.compiler.target>1.8</maven.compiler.target>\n" +
"    </properties>\n" +
"    <build>\n" +
"         <plugins>\n" +
"            <plugin>\n" +
"                <groupId>org.apache.maven.plugins</groupId>\n" +
"                <artifactId>maven-shade-plugin</artifactId>\n" +
"                <version>2.4.3</version>\n" +
"                <executions>\n" +
"                    <execution>\n" +
"                        <phase>package</phase>\n" +
"                        <goals>\n" +
"                            <goal>shade</goal>\n" +
"                        </goals>\n" +
"                        <configuration>\n" +
"                            <filters>\n" +
"                                <filter>\n" +
"                                    <artifact>*:*</artifact>\n" +
"                                    <excludes>\n" +
"                                        <exclude>META-INF/maven/**</exclude>\n" +
"                                        <exclude>META-INF/COPYRIGHT.html</exclude>\n" +
"                                        <exclude>META-INF/LICENSE*</exclude>\n" +
"                                        <exclude>META-INF/NOTICE*</exclude>\n" +
"                                        <exclude>META-INF/README.txt</exclude>\n" +
"                                        <exclude>META-INF/DEPENDENCIES*</exclude>\n" +
"                                        <exclude>LICENSE.txt</exclude>\n" +
"                                        <exclude>rhinoDiff.txt</exclude>\n" +
"                                        <exclude>license/**</exclude>\n" +
"                                        <exclude>checkstyle/**</exclude>\n" +
"                                        <exclude>findbugs/**</exclude>\n" +
"                                        <exclude>pmd/**</exclude>\n" +
"                                    </excludes>\n" +
"                                </filter>\n" +
"                            </filters>\n" +
"                            <dependencyReducedPomLocation>${project.build.directory}/dependency-reduced-pom.xml</dependencyReducedPomLocation>\n" +
"                            <transformers>\n" +
"                                <!-- add Main-Class to manifest file -->\n" +
"                                <transformer implementation=\"org.apache.maven.plugins.shade.resource.ManifestResourceTransformer\">\n" +
"                                    <mainClass>com.mycompany.databasediff.DatabaseDiff</mainClass>\n" +
"                                </transformer>\n" +
"                            </transformers>\n" +
"                        </configuration>\n" +
"                    </execution>\n" +
"                </executions>\n" +
"            </plugin>\n" +
"         </plugins>\n" +
"    </build>\n" +
"    <dependencies>\n" +
            "{}\n" +
"        <dependency>\n" +
"            <groupId>org.swinglabs</groupId>\n" +
"            <artifactId>swing-layout</artifactId>\n" +
"            <version>1.0.3</version>\n" +
"        </dependency>\n" +
"    </dependencies>\n" +
"</project>";
    private static final String CMD_TEMPLATE = "start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file"
            + " -Dfile=lib/{} -DgroupId=com.oracle -DartifactId={} -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true\n";

    private static void fillFilesRecursively(Path directory, final String fileMask, final List<File> resultFiles)
            throws IOException {
        Files.walkFileTree(directory, new java.nio.file.SimpleFileVisitor<Path>() {
            @Override
            public java.nio.file.FileVisitResult visitFile(Path file, java.nio.file.attribute.BasicFileAttributes attrs)
                    throws IOException {
                final String filePath = file.toString().toLowerCase();
                if (filePath.endsWith(fileMask)) {
                    resultFiles.add(file.toFile());
                }
                return java.nio.file.FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        Options options = new Options();
        options.addOption("s", "src", true, "The source directory with jar files.");
        options.addOption("?", "help", false, "This help text.");

        if (args.length > 0) {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = null;
            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                help(options);
                return;
            }

            if (cmd.hasOption("?")) {
                help(options);
                return;
            }

            processCommandLine(cmd);
            return;

        } else {
            help(options);
        }
    }

    private static void processCommandLine(CommandLine cmd) throws IOException, IOException, InterruptedException {
        String sourcePath = ".";
        if (cmd.hasOption("s")) {
            sourcePath = cmd.getOptionValue("s");
        }
        List<File> foundFiles = new ArrayList<>();
        System.out.println(HEADER);
        System.out.println(MESSAGE);
        fillFilesRecursively(Paths.get(sourcePath), "jar", foundFiles);
        processFiles(foundFiles);
    }

    private static void help(Options options) {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp(100, "generate-pom-and-cmd-1.0.jar", HEADER, options, MESSAGE);
    }

    private static void processFiles(List<File> foundFiles) throws IOException {
        String pomBlock = "        <dependency>\n" +
"            <groupId>com.oracle</groupId>\n" +
"            <artifactId>{}</artifactId>\n" +
"            <version>1.0</version>\n" +
"        </dependency>\n";
        StringBuilder result = new StringBuilder();
        foundFiles.forEach((file) -> {
            result.append(U.format(pomBlock, file.getName().replace(".jar", "")));
        });
        String resultPom = U.format(POM_TEMPLATE, result.toString());
        Files.write(Paths.get("pom.xml"), resultPom.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
        
    }
}
