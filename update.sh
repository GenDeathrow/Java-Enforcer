########################################
# Updates Version numbers and builds ForgeGradle
########################################

#Replace all ver keys with new Number
grep -lRr -e $ver_key * | xargs sed -i "s/$ver_key/$newversion/g"

#Build Forge
./gradlew clean setupCIWorkSpace build

. ./make_logs




