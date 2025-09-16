# Tools
JAVAC = javac
JAVA = java
CXX = g++
CXXFLAGS = -std=c++11 -Wall

# Java sources and classes
JAVA_PATH = Frontend/
JAVA_SRCS = $(wildcard $(JAVA_PATH)*.java)
JAVA_CLASSES = $(JAVA_SRCS:.java=.class)

# C++ sources and objects
CXX_PATH = Backend/Engine/
TARGET = $(CXX_PATH)GameEngine.exe
SRCS = $(CXX_PATH)GameEngine.cpp $(CXX_PATH)Player.cpp $(CXX_PATH)Team.cpp \
       $(CXX_PATH)MatchSim.cpp $(CXX_PATH)HelperMethods.cpp $(CXX_PATH)FileIO.cpp
OBJS = $(SRCS:.cpp=.o)

# Targets
all: $(TARGET) $(JAVA_CLASSES) run

# Java build
$(JAVA_PATH)%.class: $(JAVA_PATH)%.java
	$(JAVAC) $<

# C++ build
$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $(OBJS)

# Pattern rule for C++ object files
$(CXX_PATH)%.o: $(CXX_PATH)%.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Run Java frontend (depends on both builds)
run:
	$(JAVA) $(JAVA_PATH)Main

# Clean everything
clean:
	rm -f $(JAVA_PATH)*.class
	rm -f $(CXX_PATH)*.o $(TARGET)

.PHONY: all run clean