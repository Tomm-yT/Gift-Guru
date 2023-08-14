#!/bin/sh

# pkg="com.training" ; ./run.sh $pkg ; adb shell am force-stop $pkg

set -e

appPkg="$1"

clear && printf '\e[3J' && gradle :app:compileDebugAndroidTestKotlin && gradle :app:installDebug || exit -111

clear && printf '\e[3J' && gradle connectedDebugAndroidTest || adb pull "/storage/emulated/0/Android/data/$1/test_failures"