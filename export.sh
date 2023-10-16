mkdir -p export
cp -r src/ export/src
cp pom.xml export/pom.xml
sed '/<build>/,/<\/build>/d' export/pom.xml > export/pom.xml.tmp
mv export/pom.xml.tmp export/pom.xml
cp -r config/ export/config
zip -r export.zip export
rm -rf export