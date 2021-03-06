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
"          <plugin>\n" +
"              <groupId>org.apache.maven.plugins</groupId>\n" +
"              <artifactId>maven-jar-plugin</artifactId>\n" +
"              <version>2.4</version>\n" +
"              <configuration>\n" +
"                  <archive>\n" +
"                      <manifest>\n" +
"                          <addClasspath>true</addClasspath>\n" +
"                          <mainClass>com.mycompany.databasediff.DatabaseDiff</mainClass>\n" +
"                          <classpathPrefix>jars/</classpathPrefix>\n" +
"                      </manifest>\n" +
"                  </archive>\n" +
"              </configuration>\n" +
"          </plugin>\n" +
"          <plugin>\n" +
"              <groupId>org.apache.maven.plugins</groupId>\n" +
"              <artifactId>maven-dependency-plugin</artifactId>\n" +
"              <version>2.5.1</version>\n" +
"              <executions>\n" +
"                  <execution>\n" +
"                      <id>copy-dependencies</id>\n" +
"                      <phase>package</phase>\n" +
"                      <goals>\n" +
"                          <goal>copy-dependencies</goal>\n" +
"                      </goals>\n" +
"                      <configuration>\n" +
"                          <outputDirectory>\n" +
"                              ${project.build.directory}/jars/\n" +
"                          </outputDirectory>\n" +
"                      </configuration>\n" +
"                  </execution>\n" +
"              </executions>\n" +
"          </plugin>\n" +
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
    private static final String CMD_TEMPLATE = "call mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file"
            + " -Dfile={0} -DgroupId=com.oracle -DartifactId={1} -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true\n";

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
            result.append(U.format(pomBlock, file.getName().replace(".jar", "").replace(".", "-").toLowerCase()));
        });
        String resultPom = U.format(POM_TEMPLATE, result.toString());
        Files.write(Paths.get("generated-pom.xml"), resultPom.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
        StringBuilder resultCmd = new StringBuilder();
        foundFiles.forEach((file) -> {
            resultCmd.append(java.text.MessageFormat.format(CMD_TEMPLATE, file.getAbsolutePath().replace("\\", "/"), file.getName().replace(".jar", "").replace(".", "-").toLowerCase()));
        });        
        Files.write(Paths.get("install-jars.cmd"), resultCmd.toString().getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
    }
}
