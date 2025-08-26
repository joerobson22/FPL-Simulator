# Tools
JAVAC = javac
JAVA = java

# Java sources and classes
JAVA_SRCS = $(wildcard Frontend/*.java)
JAVA_CLASSES = $(JAVA_SRCS:.java=.class)

# Targets
all: java_build run

# Java build (incremental)
java_build: $(JAVA_CLASSES)

# Rule: compile .java to .class only if needed
Frontend/%.class: Frontend/%.java
	$(JAVAC) $<

# Run Java frontend
run:
	$(JAVA) -cp . Frontend.Main

# Clean
clean:
	rm -f Frontend/*.class
