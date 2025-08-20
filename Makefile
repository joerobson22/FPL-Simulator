#THIS IS A SEPERATE MAKEFILE to the one in CPP because
#idk i installed gcc on my windows machine and it doesn't reliably compile my .cpp file into an executable
#therefore i have 2 makefiles, one to run in git bash and one to run in the MSYS2 MINGW64 Shell


# Tools
JAVAC = javac
JAVA = java

# Java sources and classes
JAVA_SRCS = $(wildcard Java/*.java)
JAVA_CLASSES = $(JAVA_SRCS:.java=.class)

# Targets
all: java_build run

# Java build (incremental)
java_build: $(JAVA_CLASSES)

# Rule: compile .java to .class only if needed
Java/%.class: Java/%.java
	$(JAVAC) $<

# Run Java frontend
run:
	$(JAVA) Java.Main

#clean
clean:
	rm -f Java/*.class
