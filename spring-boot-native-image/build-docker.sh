docker build -f docker_${1} --build-arg APP_FILE=./target/aot -t aot:${1} .
