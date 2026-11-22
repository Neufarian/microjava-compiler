# MicroJava Compiler

A Java compiler for a subset of MicroJava, developed for the Program Compilers 1 course at the University of Belgrade, School of Electrical Engineering. It translates a source file into bytecode for the MicroJava virtual machine.

## From source to bytecode

1. JFlex tokenizes the source using `spec/mjlexer.flex`.
2. The CUP grammar in `spec/mjparser.cup` describes the syntax and builds an abstract syntax tree.
3. `SemanticAnalyzer.java` traverses the tree, resolves symbols and checks types.
4. `CodeGenerator.java` emits VM instructions for operations including array access, arithmetic and conditional expressions.

Both visitors are in `src/rs/ac/bg/etf/pp1/`. The command-line entry point is in `test/rs/ac/bg/etf/pp1/Compiler.java`.

## Compile an example

Use a JDK, Apache Ant and the JARs referenced by `build.xml` in `lib/`. Run these commands from the project root in a Linux shell. The Ant target regenerates the lexer, parser and AST classes before compiling the sources.

```sh
ant compile
mkdir -p bin
javac -cp 'src:lib/*' -d bin test/rs/ac/bg/etf/pp1/Compiler.java
java -cp 'bin:src:config:lib/*' rs.ac.bg.etf.pp1.Compiler test/program.mj test/program.obj
ant disasm
```

The final command prints the instructions from `test/program.obj`. Example input programs are in `test/`.
