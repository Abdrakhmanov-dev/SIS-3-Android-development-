#!/bin/zsh
cd "$(dirname "$0")"
if [ -d '/Applications/Android Studio.app' ]; then
 open -a 'Android Studio' "$PWD"
else
 open 'https://developer.android.com/studio'
 open README.md
fi
