#!/bin/bash -e

echo "    git pull"
git pull

arg_messge="$1"

echo "    git add . -A"
git add . -A
append_message="$(git status --short)"

if [ -z "$arg_messge" ]
then
    echo "    git commit -m [$(date +%Y-%m-%d)] modified source."
    git commit -m "[$(date +%Y-%m-%d)] modified source." -m "$append_message"
else
    echo "    git commit -m [$(date +%Y-%m-%d)] $arg_messge"
    git commit -m "[$(date +%Y-%m-%d)] $arg_messge" -m "$append_message"
fi

echo "    git push"
git push