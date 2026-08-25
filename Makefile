# Canonical command aliases for the android-experiments monorepo.
#
# Usage:
#   make help           List available targets
#   make build          Build everything (./gradlew buildAll)
#   make lint           Run Android lint on every subproject
#   make clean          Clean every subproject
#   make <project>      Build a single subproject, e.g. `make memeviewer`
#
# Projects are listed under PROJECTS below. Add new ones in two places when you
# add a subproject: PROJECTS and the includeBuild line in settings.gradle.

GRADLE     ?= ./gradlew
PROJECTS   := memeviewer hellojetpackcompose helloshaders hellodreamservice networkdiscoverydemo recyclerview whitenoiseapp

.DEFAULT_GOAL := help

.PHONY: help
help: ## Show this help.
	@awk 'BEGIN {FS = ":.*##"; printf "Targets:\n"} \
		/^[a-zA-Z_.-]+:.*?##/ { printf "  \033[36m%-22s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)
	@printf "\nPer-project targets (e.g. \033[36mmake memeviewer\033[0m):\n"
	@for p in $(PROJECTS); do printf "  \033[36m%-22s\033[0m Build $$p (./gradlew :$$p:assembleDebug)\n" $$p; done
	@printf "\nScoped helpers (e.g. \033[36mmake install-memeviewer\033[0m):\n"
	@printf "  \033[36minstall-<name>          \033[0m Install debug APK on connected device\n"
	@printf "  \033[36mtest-<name>             \033[0m Run unit tests for the named subproject\n"
	@printf "  \033[36mlint-<name>             \033[0m Run lint for the named subproject\n"

.PHONY: build
build: ## Build every included build (./gradlew buildAll).
	$(GRADLE) buildAll

.PHONY: assemble
assemble: ## Assemble every included build (debug + release APKs).
	$(GRADLE) assembleAll

.PHONY: check
check: ## Run tests in every included build.
	$(GRADLE) checkAll

.PHONY: lint
lint: ## Run Android lint in every included build.
	$(GRADLE) lintAll

.PHONY: clean
clean: ## Clean every included build.
	$(GRADLE) cleanAll

.PHONY: projects
projects: ## List included builds.
	$(GRADLE) projects

# Per-project targets — `./gradlew :<name>:assembleDebug`
.PHONY: $(PROJECTS)
$(PROJECTS):
	$(GRADLE) :$@:assembleDebug

.PHONY: install-%
install-%: ## Install the named subproject's debug APK on the connected device.
	$(GRADLE) :$*:installDebug

.PHONY: test-%
test-%: ## Run the unit tests for the named subproject.
	$(GRADLE) :$*:testDebugUnitTest

.PHONY: lint-%
lint-%: ## Run lint for the named subproject.
	$(GRADLE) :$*:lintDebug
