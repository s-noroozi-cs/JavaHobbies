docker run --rm -d --name consul -p 8500:8500 consul:1.13.3

docker run  --name rabbitmq --rm -d -p 5672:5672 -p 15672:15672 rabbitmq:3.11-alpine

docker exec rabbitmq rabbitmq-plugins enable rabbitmq_management