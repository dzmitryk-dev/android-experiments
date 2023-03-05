.PHONY: helloshaders

helloshadersbuild:
	echo "Build helloshaders"
	cd helloshaders/;./gradlew buildDebug

helloshadersclean:
	echo "Clean helloshaders"
	cd helloshaders/;./gradlew clean

hellojetpackcomposebuild:
	echo "Build hellojetpackcompose"
	cd hellojetpackcompose/;./gradlew buildDebug

hellojetpackcomposeclean:
	echo "Clean hellojetpackcompose"
	cd hellojetpackcompose/;./gradlew clean	

whitenoiseappbuild:
	echo "Build whitenoiseapp"
	cd whitenoiseapp/;./gradlew buildDebug

whitenoiseappclean:
	echo "Clean whitenoiseapp"
	cd whitenoiseapp/;./gradlew clean	


all: 
	echo "Build all project"
	make helloshadersbuild	

clean:
	make helloshadersclean