if [ $# -ne 2 ]
then
	echo "Usage: ./cliser_os nb_clients nb_serveurs";
elif [ $1 -eq 0 ] || [ $2 -eq 0 ]
then
	echo "Must have at least one client and one server";
else
	java -classpath ".\bin\;\
.\lib\fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar;\
.\lib\fr.lip6.pnml.framework.ptnet_2.2.14.jar;\
.\lib\fr.lip6.pnml.framework.utils_2.2.14.jar;\
.\lib\org.eclipse.emf.common_2.17.0.v20190920-0401.jar;\
.\lib\org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar;\
.\lib\org.slf4j.api_1.7.30.v20200204-2150.jar;\
.\lib\slf4j-jdk14-2.0.0-alpha1.jar" clientserver.TestGenerator $1 $2;
fi