#!/usr/bin/env bash
# Make sure to export your JAVA_HOME !! always use JRE7 or JRE8

LWJGL_LIBS="lwjgl-2.9.0.jar:lwjgl-platform-2.9.0-natives-linux.jar:lwjgl_util-2.9.0.jar"
JINPUT_LIBS="jinput-2.0.5.jar:jutils-1.0.0.jar"
MINECRAFT_LIBS="bin/modpack.jar:bin/minecraft.jar"

LIBS="$LWJGL_LIBS:$JINPUT_LIBS:$MINECRAFT_LIBS"

./gradlew -Plocal clean build :asm-htf-loader:bundleJar || exit

mkdir -p ./client/addons

# Move the newly built jars into client folder
cp -v ../asm-htf/asm-htf-*/build/libs/asm-htf-*-*.jar ./client/ || exit
cp -v ./build/libs/tekkit-client-tools-*.jar ./client/addons || exit

# Start the client
cd ./client || exit

$JAVA_HOME/bin/java \
    -javaagent:"asm-htf-loader-10.0.0.1.jar=/home/xforce/IdeaProjects/tekkit-client-tools/client/addons/tekkit-client-tools-10.0.0.1.jar" \
    -cp "asm-htf-api-10.0.0.1.jar:$LIBS" \
    -Djava.library.path=bin/natives/ \
    net.minecraft.client.Minecraft \
    -Xmx2G -Xms1Gd