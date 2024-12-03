# GeneratePomAndCmd

**POM and CMD File Generator**  
Version: `2020-08-08`

## Overview

The `GeneratePomAndCmd` application is a command-line utility designed to automate the generation of Maven POM and CMD files for Java projects. It scans a specified directory for `.jar` files and generates:

1. A `pom.xml` file with Maven dependencies for the discovered JAR files.
2. A `install-jars.cmd` script to install the JAR files into a local Maven repository.

For more information, visit the [GitHub repository](https://github.com/javadev/generate-pom-and-cmd).

---

## Features

- **Automated POM Generation**: Creates a POM file with dependency declarations for all `.jar` files in the source directory.
- **Batch Maven Installation Script**: Generates a `CMD` script to simplify installing JARs into a Maven repository.
- **Recursive File Scanning**: Traverses directories recursively to locate `.jar` files.

---

## Requirements

- Java 8 or higher
- Maven
- Apache Commons CLI
- [Underscore-java](https://github.com/javadev/underscore-java)

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/javadev/generate-pom-and-cmd.git
   ```
2. Compile the project:
   ```bash
   mvn clean compile
   ```

---

## Usage

Run the application using the following command:

```bash
java -jar generate-pom-and-cmd-1.0.jar -s <source-directory>
```

### Options

| Option              | Description                                        |
|---------------------|----------------------------------------------------|
| `-s, --src`         | The source directory containing `.jar` files.      |
| `-?, --help`        | Displays help information.                         |

---

## Example

1. **Generate POM and CMD files**:
   ```bash
   java -jar generate-pom-and-cmd-1.0.jar -s /path/to/jars
   ```

   - **Generated Files**:
     - `generated-pom.xml`: Contains Maven dependency declarations.
     - `install-jars.cmd`: Batch script to install JAR files.

2. **Install JARs Using CMD Script**:
   ```bash
   install-jars.cmd
   ```

---

## Project Structure

```plaintext
src/main/java/com/github/generator
└── GeneratePomAndCmd.java   # Main class to generate POM and CMD files.
```

---

## Contributing

We welcome contributions! Please submit issues or pull requests through the [GitHub repository](https://github.com/javadev/generate-pom-and-cmd).

---

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).

---

Feel free to suggest improvements or add details to fit your project requirements!
