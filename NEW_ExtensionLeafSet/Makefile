help:
	@echo "Deux arguments possibles :"
	@echo " - all    : modélise l’extension du LeafSet avec les arguments SIZE-> taille du LeafSet BREAKDOWN->nombre de pannes dans le LeafSet"
	@echo " - test   :  odélise l’extension du LeafSet avec les paramètres SIZE=6 et BREAKDOWN=3"
	@echo " - pretty : formater le PNML de sortie (nom du fichier dans la variable FILE)"
	@echo
	@echo "prévoyez 2 arguments : SIZE ->taille du LeafSet et BREAKDOWN->nombre de pannes dans le LeafSet"

all:
	javac -d bin -classpath ".:./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar\
	:./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar\
	:./lib/fr.lip6.pnml.framework.utils_2.2.14.jar\
	:./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar\
	:./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar\
	:./lib/org.slf4j.api_1.7.30.v20200204-2150.jar" ./src/**/*.java
	java -classpath "./bin/:\
	./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar:\
	./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar:\
	./lib/fr.lip6.pnml.framework.utils_2.2.14.jar:\
	./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar:\
	./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar:\
	./lib/org.slf4j.api_1.7.30.v20200204-2150.jar:\
	./lib/slf4j-jdk14-2.0.0-alpha1.jar" extensionleafset.Main $(SIZE) $(BREAKDOWN) "ExtensionOfLeafSetSize$(SIZE)BreakDown$(BREAKDOWN)"

test:
	@echo test with size=6 and number of breakdown=3
	javac -d bin -classpath ".:./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar\
	:./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar\
	:./lib/fr.lip6.pnml.framework.utils_2.2.14.jar\
	:./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar\
	:./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar\
	:./lib/org.slf4j.api_1.7.30.v20200204-2150.jar" ./src/**/*.java
	java -classpath "./bin/:\
	./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar:\
	./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar:\
	./lib/fr.lip6.pnml.framework.utils_2.2.14.jar:\
	./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar:\
	./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar:\
	./lib/org.slf4j.api_1.7.30.v20200204-2150.jar:\
	./lib/slf4j-jdk14-2.0.0-alpha1.jar" extensionleafset.Main 6 3 "ExtensionOfLeafSetSize6BreakDown3"; 

pretty:
	xmllint -format "testmodel/$(FILE).pnml" > prettypnml/$(FILE).xml