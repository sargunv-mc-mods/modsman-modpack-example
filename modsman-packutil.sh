#!/usr/bin/env sh

FILE_NAME="modsman-packutil.jar"
FILE_URL="https://github.com/sargunv/modsman/releases/download/0.32.1/modsman-packutil-0.32.1.jar"

if [ ! -f "$FILE_NAME" ]; then
    echo "Downloading '$FILE_NAME' from '$FILE_URL' ..."
    curl "$FILE_URL" --output "$FILE_NAME" || wget "$FILE_URL" -O "$FILE_NAME"
fi

java -jar $FILE_NAME $@