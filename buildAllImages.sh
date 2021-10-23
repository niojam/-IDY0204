#!/bin/bash

BACKOFFICE='./backoffice'
PUBLIC_APP='./public-application'

flags=""


while getopts x: flag
	do
	    case "${flag}" in
	        *) flags+="-${flag} ${OPTARG} ";;
	    esac
	done

# build backoffice
./gradlew :backoffice:build $flags
cd $BACKOFFICE || exit
docker build -t kahoot-backoffice .

cd ..

# build public-application
./gradlew :public-application:build $flags
cd $PUBLIC_APP || exit
docker build -t kahoot-public-app .

docker rmi $(docker images -f 'dangling=true' -q) -f || true