#!/bin/sh

# pkg="com.training" ; ./run.sh $pkg ; adb shell am force-stop $pkg

set -e

appPkg="$1"

clear && printf '\e[3J' && gradle :app:assembleDebug && gradle :app:installDebug || exit -111

clear && printf '\e[3J' && adb shell am start -n "$appPkg/$appPkg.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER || exit -111

clear && printf '\e[3J' && adb logcat -c && adb logcat $appPkg:V AndroidRuntime:E *:S -v raw