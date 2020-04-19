#!/usr/bin/env sh

FILE_NAME="modsman-packutil.jar"
FILE_URL="https://github.com/sargunv/modsman/releases/download/0.32.1/modsman-packutil-0.32.1.jar"

if [ ! -f "$FILE_NAME" ]; then
    echo "Downloading '$FILE_NAME' from '$FILE_URL' ..."
    if [ -x "$(which wget)" ] ; then
        wget "$FILE_URL" -O "$FILE_NAME"
    elif [ -x "$(which curl)" ]; then
        curl "$FILE_URL" -o "$FILE_NAME" -L
    else
        echo "Could not find curl or wget, please install one." >&2
    fi
fi

java -jar $FILE_NAME $@
