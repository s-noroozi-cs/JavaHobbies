docker build -f docker_${1} --build-arg APP_FILE=aot -t aot:${1} .
