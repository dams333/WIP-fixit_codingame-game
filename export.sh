rm -f export.zip
mkdir -p export
cp -r src/ export/src
cp pom.xml export/pom.xml
sed '/<build>/,/<\/build>/d' export/pom.xml > export/pom.xml.tmp
mv export/pom.xml.tmp export/pom.xml
cp -r config/ export/config
cd export
zip -r ../export.zip *
cd ..
rm -rf export