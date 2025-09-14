CXX = g++
CXXFLAGS = -std=c++11
FOLDER = Engine/
TARGET = $(FOLDER)GameEngine
SRCS = $(FOLDER)GameEngine.cpp $(FOLDER)Player.cpp $(FOLDER)Team.cpp $(FOLDER)MatchSim.cpp $(FOLDER)HelperMethods.cpp $(FOLDER)FileIO.cpp
OBJS = $(SRCS:.cpp=.o)

$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $(OBJS)

%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f *.o $(TARGET)

rebuild: clean $(TARGET)

run:
	./$(TARGET)

.PHONY: clean rebuild