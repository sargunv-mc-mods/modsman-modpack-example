$FILE_NAME = "modsman-packutil.jar"
$FILE_URL = "https://github.com/sargunv/modsman/releases/download/0.32.0/modsman-packutil-0.32.0.jar"

if (-Not (Test-Path($FILE_NAME))) {
    Write-Output "Downloading '$FILE_NAME' from '$FILE_URL' ..."
    Invoke-WebRequest $FILE_URL -OutFile $FILE_NAME
}

java -jar $FILE_NAME @args