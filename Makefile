#-----------------------------
# Tools
#-----------------------------
CPP = g++
JAVAC = javac
JAVA = java

#-----------------------------
# Directories
#-----------------------------
JAVA_DIR = Java
CPP_DIR = CPP

#-----------------------------
# Java sources and classes
#-----------------------------
JAVA_SRCS = $(wildcard $(JAVA_DIR)/*.java)
JAVA_CLASSES = $(JAVA_SRCS:.java=.class)

#-----------------------------
# Targets
#-----------------------------
all: java_build cpp_build run

#-----------------------------
# Java build (incremental)
#-----------------------------
java_build: $(JAVA_CLASSES)

# Rule: compile .java to .class only if needed
$(JAVA_DIR)/%.class: $(JAVA_DIR)/%.java
	$(JAVAC) $<

#-----------------------------
# C++ build
#-----------------------------
cpp_build: $(CPP_DIR)/GameEngine.cpp
	$(CPP) $(CPP_DIR)/GameEngine.cpp -o $(CPP_DIR)/GameEngine.exe

#-----------------------------
# Run Java frontend
#-----------------------------
run:
	$(JAVA) Java.Main

#-----------------------------
# Clean (optional)
#-----------------------------
clean:
	rm -f $(JAVA_DIR)/*.class GameEngine.exe
