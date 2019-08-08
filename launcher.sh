#! /bin/bash

netid=mxc123530

## Root directory of your project
#PROJDIR=/people/cs/s/sxg122830/TestProj

# Directory where the config file is located on your local system
CONFIGLOCAL=config/configuration.txt


n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    j=$(echo $i | head -n1 | cut -d " " -f1)
    echo $j
    while [[ $n -lt $j ]]
    do
    	read line
    	p=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )

        gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -cp $BINDIR $PROG $p; exec bash" &

        n=$(( n + 1 ))
    done
)