# Makefile — Java coin-chest experiment

JAVAC := javac
JAVA  := java
BUILD_DIR := out
MAIN := Main

SOURCES := $(wildcard *.java)

# Default run args: [Contention] [players] [CLH|TTAS] [chests]
# Override like: make run ARGS="HIGH 8 CLH 3"
ARGS ?= HIGH 8 CLH 3

.PHONY: all run clean re lint

all: $(BUILD_DIR)/.stamp

# Compile all sources in one go (safer for interdependent classes)
$(BUILD_DIR)/.stamp: $(SOURCES) | $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SOURCES)
	@touch $@

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

run: all
	$(JAVA) -cp $(BUILD_DIR) $(MAIN) $(ARGS)

lint:
	$(JAVAC) -Xlint:all -d $(BUILD_DIR) $(SOURCES)

# Remove build dir AND any stray .class files in the repo
clean:
	rm -rf $(BUILD_DIR)
	find . -type f -name '*.class' -delete

re: clean all
