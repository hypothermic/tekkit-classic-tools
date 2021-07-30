#!/usr/bin/env bash
# Make sure to export your JAVA_HOME !! always use JRE7 or JRE8
# Make sure to export your TECHNIC_LAUNCHER_PATH !! this is the /path/to/your/.technic

#ASM_HTF_PATH="../asm-htf"
TEKKIT_PATH="$TECHNIC_LAUNCHER_PATH/modpacks/tekkit"

./gradlew -Plocal clean build || exit

# Move the newly built jars into technic tekkit folder
#cp -v ../asm-htf/asm-htf-*/build/libs/asm-htf-*-*.jar "$TEKKIT_PATH" || exit
#cp "$ASM_HTF_PATH/asm-htf-loader/build/libs/asm-htf-bundle-10.0.0.1.jar" "$TEKKIT_PATH" || exit
cp ./build/libs/tekkit-client-tools-*.jar "$TEKKIT_PATH" || exit

# enter the following JVM args in the technic launcher:
#
# -javaagent:/path/to/.technic/modpacks/tekkit/asm-htf-bundle-10.0.0.1.jar=/path/to/.technic/modpacks/tekkit/tekkit-client-tools-10.0.0.1.jar
#
# --- OR ---
#
# -javaagent:/path/to/.technic/modpacks/tekkit/asm-htf-loader-10.0.0.1.jar=/path/to/.technic/modpacks/tekkit/tekkit-client-tools-10.0.0.1.jar
# -cp "/path/to/.technic/modpacks/tekkit/asm-htf-api-10.0.0.1.jar"
#
# now, manually start Tekkit from the technic launcher