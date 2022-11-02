#!/bin/bash

RED='\033[1;31m'
GREEN='\033[1;32m'
NC="\033[0m" # No Color
pass=0
fail=0


export CLASSPATH="${PWD}/prr-core/prr-core.jar:${PWD}/prr-app/prr-app.jar:${PWD}/po-uilib/po-uilib.jar"
make



for x in tests/*.in; do
    if [ -e ${x%.in}.import ]; then
        java -Dimport=${x%.in}.import -Din=$x -Dout=${x%.in}.outhyp prr.app.App;
    else
        java -Din=$x -Dout=${x%.in}.outhyp prr.app.App;
    fi

    diff -cB -w ${x%.in}.out ${x%.in}.outhyp > ${x%.in}.diff ;
    if [ -s ${x%.in}.diff ]; then
        echo -e "${RED} FAIL: $x. See file ${x%.in}.diff ${NC}"
        tempzzzT=${x:6}
        grep "${tempzzzT%.in}" README
        fail=$((fail+1))
    else
        echo -e "${GREEN} PASS: $x ${NC}"
        rm -f ${x%.in}.diff ${x%.in}.outhyp ;
        pass=$((pass+1))
    fi
done

#rm -f saved*


echo -e "${GREEN} Passed ${NC}: $pass"
echo -e "${RED} Failed ${NC}: $fail"
echo "Done."

make clean > /dev/null
rm *.dat
