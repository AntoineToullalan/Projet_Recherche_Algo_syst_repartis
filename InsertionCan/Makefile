help:
	@echo "Deux arguments possibles :"
	@echo " - all    : compile la bibliothèque pnmlframework"
	@echo " - test   :  compile la bibliothèque pnmlframework et exécute le code dans ReseauSimple pour générer un réseau avec les paramètres num=2 size=3 x=40 y=40"
	@echo " - pretty : formater le PNML de sortie (nom du fichier dans la variable FILE)"
	@echo
	@echo "prévoyez 4 arguments : size size->taille du LeafSet"

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
	./lib/slf4j-jdk14-2.0.0-alpha1.jar" insertioncan.Main $(NUM) $(SIZE) $(X) $(Y)

test:
	@echo test with determinated size 
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
	./lib/slf4j-jdk14-2.0.0-alpha1.jar" insertioncan.Main 1; 

pretty:
	xmllint -format $(FILE) > /tmp/prettypmnl
	mv /tmp/prettypmnl $(FILE)