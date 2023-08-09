docker build -f Dockerfiles/${1} --build-arg APP_FILE=./target/aot -t aot:${1} .
