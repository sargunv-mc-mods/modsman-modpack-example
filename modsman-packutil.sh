#!/usr/bin/env sh

FILE_NAME="modsman-packutil.jar"
FILE_URL="https://github.com/sargunv/modsman/releases/download/0.32.0/modsman-packutil-0.32.0.jar"

if [ ! -f "$FILE_NAME" ]; then
    echo "Downloading '$FILE_NAME' from '$FILE_URL' ..."
fi

java -jar $FILE_NAME $@