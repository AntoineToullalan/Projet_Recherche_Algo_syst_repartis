help:
	@echo " - test   :  compile la bibliothèque pnmlframework et exécute le code pour générer un réseau"
	@echo " - pretty : formater le PNML de sortie (nom du fichier dans la variable FILE)"

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
	./lib/slf4j-jdk14-2.0.0-alpha1.jar" insertioncan.Main $(SIZE); 

pretty:
	xmllint -format $(FILE) > /tmp/prettypmnl
	mv /tmp/prettypmnl $(FILE)